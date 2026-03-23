package DAO;

import java.util.*;
import java.sql.Connection;
import entity.Item;

/**
 *
 */
public class ReportDataAccess {

    /**
     * Default constructor
     */
    public ReportDataAccess() {
    }

    /**
     *
     */
    private String connectionString;

    // Getters and Setters
    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }


    /**
     * @param start 
     * @param end 
     * @return
     */
    public List<Item> fetchItemsByDate(Date start, Date end) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT ItemID, Title, Description, Category, Location, Photo, DateUploaded, Status FROM FOUND_ITEM WHERE DateUploaded BETWEEN ? AND ? ORDER BY DateUploaded DESC";

        try (Connection conn = dataaccess.DBConnection.getInstance().getConnection();
                java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(start.getTime()));
            stmt.setDate(2, new java.sql.Date(end.getTime()));

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setItemID(rs.getInt("ItemID"));
                    item.setTitle(rs.getString("Title"));
                    item.setDescription(rs.getString("Description"));
                    item.setItemType(factory.ItemTypeFactory.getItemType(rs.getString("Category"), rs.getString("Status")));
                    item.setLocation(rs.getString("Location"));
                    // Use standard DAO methods if possible, but here we'll just map basics
                    item.setDateFound(rs.getDate("DateUploaded"));
                    items.add(item);
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error fetching items by date: " + e.getMessage());
        }
        return items;
    }

}