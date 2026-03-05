package DAO;

import java.sql.*;
import java.util.*;
import entity.Item;
import dataaccess.DBConnection;

/**
 * Data Access Object for Item entities
 * Handles database operations for lost and found items
 */
public class ItemDataAccess {

    /**
     * Default constructor
     */
    public ItemDataAccess() {
    }

    /**
     * Find an item by its ID
     * 
     * @param id
     * @return Item object or null if not found
     */
    public Item findItemByID(int id) {
        String sql = "SELECT ItemID, Title, Description, Category, Location, Photo, DateUploaded, Status FROM FOUND_ITEM WHERE ItemID = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setItemID(rs.getInt("ItemID"));
                    item.setTitle(rs.getString("Title"));
                    item.setDescription(rs.getString("Description"));
                    item.setCategory(rs.getString("Category"));
                    item.setLocation(rs.getString("Location"));
                    item.setPhotoPath(rs.getString("Photo"));
                    item.setDateFound(rs.getDate("DateUploaded"));
                    item.setStatus(rs.getString("Status"));
                    return item;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding item by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Remove an item from the database
     * 
     * @param id Item ID to remove
     */
    public void removeItem(int id) {
        String sql = "DELETE FROM FOUND_ITEM WHERE ItemID = ?";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Delete associated claims first (FK constraint)
                try (PreparedStatement claimStmt = conn.prepareStatement("DELETE FROM CLAIM WHERE ItemID = ?")) {
                    claimStmt.setInt(1, id);
                    claimStmt.executeUpdate();
                }

                // Delete the item
                try (PreparedStatement itemStmt = conn.prepareStatement(sql)) {
                    itemStmt.setInt(1, id);
                    int rowsAffected = itemStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Item " + id + " deleted successfully.");
                    } else {
                        System.out.println("Item " + id + " not found.");
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error removing item: " + e.getMessage());
        }
    }

    /**
     * Get all items from the database
     * 
     * @return List of all items
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT ItemID, Title, Description, Category, Location, Photo, DateUploaded, Status FROM FOUND_ITEM ORDER BY ItemID DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Item item = new Item();
                item.setItemID(rs.getInt("ItemID"));
                item.setTitle(rs.getString("Title"));
                item.setDescription(rs.getString("Description"));
                item.setCategory(rs.getString("Category"));
                item.setLocation(rs.getString("Location"));
                item.setPhotoPath(rs.getString("Photo"));
                item.setDateFound(rs.getDate("DateUploaded"));
                item.setStatus(rs.getString("Status"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all items: " + e.getMessage());
        }
        return items;
    }

    /**
     * Save a new item to the database
     * 
     * @param item the item to save
     * @return the generated item ID if successful, 0 if failed
     */
    public int saveItem(Item item) {
        // Ensure default officer exists to avoid FK error
        ensureDefaultOfficerExists(item.getOfficerID());

        // ItemID is an IDENTITY column in the DB, so we don't insert it manually.
        // The database will generate it automatically.
        // The database column for officer/uploader is 'UploadedByUserID'.
        String sql = "INSERT INTO FOUND_ITEM (Title, Description, Category, Color, Location, Photo, Status, DateUploaded, UploadedByUserID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getDescription());
            stmt.setString(3, item.getCategory());
            stmt.setString(4, "Unknown"); // Default for Color
            stmt.setString(5, item.getLocation());
            stmt.setString(6, item.getPhotoPath());
            stmt.setString(7, item.getStatus());
            stmt.setDate(8, new java.sql.Date(item.getDateFound().getTime()));
            stmt.setInt(9, item.getOfficerID()); // Maps to UploadedByUserID column in DB

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Error saving item to database: " + e.getMessage());
            return 0;
        }
    }

    /**
     * @param item
     * @return
     */
    public void updateItemRecord(Item item) {
        String sql = "UPDATE FOUND_ITEM SET Title=?, Description=?, Category=?, Color=?, Location=?, Photo=?, Status=? WHERE ItemID=?";

        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getDescription());
            stmt.setString(3, item.getCategory());
            stmt.setString(4, "Unknown");
            stmt.setString(5, item.getLocation());
            stmt.setString(6, item.getPhotoPath());
            stmt.setString(7, item.getStatus());
            stmt.setInt(8, item.getItemID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating item: " + e.getMessage());
        }
    }

    /**
     * @param query
     * @return
     */
    public List<Item> searchByKeywords(String query) {
        return searchByKeywordsAndCategory(query, "All Categories");
    }

    /**
     * Search items by keywords and category
     * 
     * @param keywords search keywords
     * @param category category filter ('All Categories' for no filter)
     * @return list of matching items
     */
    public List<Item> searchByKeywordsAndCategory(String keywords, String category) {
        List<Item> items = new ArrayList<>();
        // Maps DB Columns to Entity fields
        String sql = "SELECT ItemID, Title, Description, Category, Location, Photo, DateUploaded, Status FROM FOUND_ITEM WHERE 1=1";

        // Add category filter if not 'All Categories'
        if (!"All Categories".equals(category) && category != null && !category.trim().isEmpty()) {
            sql += " AND Category LIKE ?";
        }

        // Add keyword search
        if (keywords != null && !keywords.trim().isEmpty()) {
            sql += " AND (Description LIKE ? OR Title LIKE ?)";
        }

        // Add status filter for available items
        sql += " AND Status IN ('Found', 'Processing Claim')";

        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            int paramIndex = 1;

            // Set category parameter
            if (!"All Categories".equals(category) && category != null && !category.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + category + "%");
            }

            // Set keyword parameters
            if (keywords != null && !keywords.trim().isEmpty()) {
                String keywordPattern = "%" + keywords.trim() + "%";
                stmt.setString(paramIndex++, keywordPattern);
                stmt.setString(paramIndex++, keywordPattern);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setItemID(rs.getInt("ItemID"));
                    item.setTitle(rs.getString("Title"));
                    item.setDescription(rs.getString("Description"));
                    item.setCategory(rs.getString("Category"));
                    item.setLocation(rs.getString("Location"));
                    item.setPhotoPath(rs.getString("Photo")); // Map Photo -> photoPath
                    item.setDateFound(rs.getDate("DateUploaded")); // Map DateUploaded -> dateFound
                    item.setStatus(rs.getString("Status"));

                    items.add(item);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error searching items: " + e.getMessage());
        }

        return items;
    }

    /**
     * @param threshold
     * @return
     */
    public List<Item> fetchOldItems(java.util.Date threshold) {
        // TODO implement here
        return null;
    }

    /**
     * Helper to ensure the officer exists before insertion
     */
    private void ensureDefaultOfficerExists(int officerID) {
        if (officerID <= 0)
            officerID = 1; // Default

        String checkSql = "SELECT UserID FROM OFFICER WHERE UserID = ?";
        // Correcting for IDENTITY column [USER]
        String insertUser = "SET IDENTITY_INSERT [User] ON; " +
                "INSERT INTO [User] (UserID, Name, Email, PasswordHash, Role) VALUES (?, 'Default Officer', 'officer@example.com', 'password', 'Officer'); "
                +
                "SET IDENTITY_INSERT [User] OFF;";
        String insertOfficer = "INSERT INTO OFFICER (UserID, SecurityBadgeNumber) VALUES (?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            boolean exists = false;
            // Check if officer exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, officerID);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next())
                        exists = true;
                }
            }

            if (!exists) {
                // Insert into USER first (Parent)
                try (PreparedStatement userStmt = conn.prepareStatement(insertUser)) {
                    userStmt.setInt(1, officerID);
                    userStmt.executeUpdate();
                } catch (SQLException ignored) {
                }

                // Insert into OFFICER (Child)
                try (PreparedStatement officerStmt = conn.prepareStatement(insertOfficer)) {
                    officerStmt.setInt(1, officerID);
                    officerStmt.setString(2, "BADGE-" + officerID);
                    officerStmt.executeUpdate();
                    System.out.println("Seeded default Officer ID: " + officerID);
                }
            }

        } catch (SQLException e) {
            System.err.println("Warning: Could not seed default officer: " + e.getMessage());
        }
    }

}