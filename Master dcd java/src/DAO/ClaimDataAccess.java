package DAO;

import java.sql.*;
import entity.Claim;
import dataaccess.DBConnection;

/**
 * Data Access Object for Claim entities
 * Handles database operations for item claims
 */
public class ClaimDataAccess {

    /**
     * Default constructor
     */
    public ClaimDataAccess() {
    }

    /**
     * Insert a new claim into the database
     * 
     * @param claim the claim to insert
     */
    public void insertClaim(Claim claim) {
        // Mapping Entity fields to DB Columns:
        // status -> Status
        // requestDate -> DateInitiated
        // Note: Claim entity doesn't have studentID/itemID fields in the user's version
        // (reverted),
        // so we might need to rely on what's available or default them if the entity
        // doesn't support them.
        // Wait, looking at Step 130, Claim.java DOES NOT have studentID/itemID.
        // But the DAO needs to insert them into the DB table CLAIM which requires them
        // (or allows null?).
        // If the Entity doesn't have them, we can't insert them easily unless passed as
        // args?
        // But the signature is `insertClaim(Claim claim)`.

        // Strategy: Use placeholders or skip if nullable. The ERD shows (1,1)
        // relationships so they are likely NOT NULL.
        // But since I cannot change the Entity, I will comment this constraint and
        // insert what I can,
        // or hardcode defaults for testing if allowed.
        // However, the previous code had: `stmt.setInt(3, claim.getStudentID());` which
        // caused error if method missing.
        // I must allow compilation.

        String sql = "INSERT INTO CLAIM (Status, DateInitiated, ClaimedByUserID, ItemID) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, claim.getStatus());
            stmt.setDate(2, new java.sql.Date(claim.getRequestDate().getTime()));
            stmt.setInt(3, claim.getStudentID());
            stmt.setInt(4, claim.getItemID());


            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Claim inserted successfully for student " + claim.getStudentID());
            } else {
                throw new SQLException("No rows affected - claim insertion failed");
            }

        } catch (SQLException e) {
            System.err.println("Error inserting claim to database: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    /**
     * Get all claims from the database
     * 
     * @return List of all claims
     */
    public java.util.List<Claim> getAllClaims() {
        java.util.List<Claim> claims = new java.util.ArrayList<>();
        String sql = "SELECT ClaimID, Status, DateInitiated, ClaimedByUserID, ItemID FROM CLAIM ORDER BY DateInitiated DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Claim claim = new Claim();
                claim.setClaimID(rs.getInt("ClaimID"));
                claim.setStatus(rs.getString("Status"));
                claim.setRequestDate(rs.getDate("DateInitiated"));
                claim.setStudentID(rs.getInt("ClaimedByUserID"));
                claim.setItemID(rs.getInt("ItemID"));
                claims.add(claim);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all claims: " + e.getMessage());
        }
        return claims;
    }

    /**
     * Updates the status of an existing claim in the database.
     * Called by ClaimController after a strategy processes a claim.
     *
     * @param id     the ClaimID to update
     * @param status the new status string (e.g., "Approved - Strict", "Pending - Manual Review")
     */
    public void updateClaimStatus(int id, String status) {
        System.out.println("Updating claim " + id + " to " + status);
        String sql = "UPDATE CLAIM SET Status = ? WHERE ClaimID = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Claim ID " + id + " status updated to: " + status);
            } else {
                System.err.println("Claim ID " + id + " not found for status update.");
            }

        } catch (SQLException e) {
            System.err.println("Error updating claim status: " + e.getMessage());
        }
    }

    /**
     * Retrieves only pending claims from the database.
     * Used by the officer claims management screen to show unprocessed work.
     *
     * @return list of claims with status "Pending"
     */
    public java.util.List<Claim> getPendingClaims() {
        java.util.List<Claim> claims = new java.util.ArrayList<>();
        String sql = "SELECT ClaimID, Status, DateInitiated, ClaimedByUserID, ItemID "
                   + "FROM CLAIM WHERE Status = 'Pending' ORDER BY DateInitiated ASC";

        try (Connection conn = DBConnection.getInstance().getConnection();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Claim claim = new Claim();
                claim.setClaimID(rs.getInt("ClaimID"));
                claim.setStatus(rs.getString("Status"));
                claim.setRequestDate(rs.getDate("DateInitiated"));
                claim.setStudentID(rs.getInt("ClaimedByUserID"));
                claim.setItemID(rs.getInt("ItemID"));
                claims.add(claim);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching pending claims: " + e.getMessage());
        }
        return claims;
    }

}