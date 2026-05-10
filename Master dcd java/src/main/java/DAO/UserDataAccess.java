package DAO;

/**
 *
 */
public class UserDataAccess {

    /**
     * Default constructor
     */
    public UserDataAccess() {
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
     * @param uID
     * @param rID
     * @return
     */
    /**
     * @param uID
     * @param rID
     * @return
     */
    public void updateRole(int uID, int rID) {
        // Updated to use USER table from ERD
        String sql = "UPDATE [USER] SET Role = ? WHERE UserID = ?";
        // Note: [USER] is escaped because USER is a reserved keyword in SQL Server

        try (java.sql.Connection conn = dataaccess.DBConnection.getInstance().getConnection();
                java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Mapping ID to Role string as ERD shows 'Role' is a string/varchar
            // 1=Student, 2=Officer, 3=Admin (Assumed mapping)
            String roleName = "Student";
            if (rID == 2)
                roleName = "Officer";
            if (rID == 3)
                roleName = "Admin";

            stmt.setString(1, roleName);
            stmt.setInt(2, uID);

            stmt.executeUpdate();
            System.out.println("User role updated successfully");

        } catch (java.sql.SQLException e) {
            System.err.println("Error updating user role: " + e.getMessage());
        }
    }

}