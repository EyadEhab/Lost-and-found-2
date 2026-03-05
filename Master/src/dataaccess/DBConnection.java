package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Database connection utility for MS SQL Server
 * Handles JDBC connections to the Lost and Found database
 */
public class DBConnection {

    // Database connection parameters loaded from db.properties
    private static String URL;
    private static String USER;
    private static String PASS;

    static {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            props.load(fis);
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASS = props.getProperty("db.pass");
        } catch (IOException e) {
            System.err.println("Fatal: Could not load db.properties. Database connection will fail.");
            e.printStackTrace();
        }
    }

    // Connection instance (Singleton pattern)
    private static Connection connection = null;

    /**
     * Get database connection (creates new connection if none exists)
     * 
     * @return Database connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load SQL Server JDBC driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Create connection
                System.out.println("Connecting to: " + URL);
                connection = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Database connection established successfully");

            } catch (ClassNotFoundException e) {
                throw new SQLException("SQL Server JDBC Driver not found", e);
            } catch (SQLException e) {
                System.err.println("Failed to connect to database: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    /**
     * Close the database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Authenticates a user and returns their role
     * 
     * @param username
     * @param password
     * @return Role of the user if authentication is successful, null otherwise
     */
    public static String authenticateUser(String username, String password) {
        String role = null;
        // String hashedPassword = hashPassword(password); // Disabled: Using plain text
        // password for now

        // First check credentials in USER table
        // SQL Server USER is a reserved keyword, so use [User] or alias
        String authQuery = "SELECT UserID FROM [User] WHERE Name = ? AND PasswordHash = ?";

        try (Connection conn = getConnection();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(authQuery)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // Comparing plain text password directly

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("UserID");
                    // Credentials valid, now determine role
                    role = determineUserRole(conn, userId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        return role;
    }

    private static String determineUserRole(Connection conn, int userId) throws SQLException {
        // Check Admin
        if (checkTableForId(conn, "ADMIN", userId))
            return "Admin";
        // Check Officer
        if (checkTableForId(conn, "OFFICER", userId))
            return "Officer";
        // Check Student
        if (checkTableForId(conn, "STUDENT", userId))
            return "Student";

        return "User"; // Default if no specific role found
    }

    private static boolean checkTableForId(Connection conn, String tableName, int userId) throws SQLException {
        String query = "SELECT 1 FROM [" + tableName + "] WHERE UserID = ?";
        try (java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static String hashPassword(String password) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // --- Admin Features ---

    /**
     * Fetch report statistics
     * 
     * @return Map containing counts for Found, Claims, Collected, Not Collected
     */
    public static java.util.Map<String, Integer> getReportStats() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        stats.put("Found", 0);
        stats.put("Claims", 0);
        stats.put("Collected", 0);
        stats.put("Not Collected", 0);

        try (Connection conn = getConnection()) {
            // Count Found Items
            try (java.sql.Statement stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM FOUND_ITEM")) {
                if (rs.next())
                    stats.put("Found", rs.getInt(1));
            }

            // Count Claims
            try (java.sql.Statement stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM CLAIM")) {
                if (rs.next())
                    stats.put("Claims", rs.getInt(1));
            }

            // Count Collected Items
            try (java.sql.Statement stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt
                            .executeQuery("SELECT COUNT(*) FROM FOUND_ITEM WHERE Status = 'Collected'")) {
                if (rs.next())
                    stats.put("Collected", rs.getInt(1));
            }

            // Count Not Collected Items (Assuming anything not collected is 'Not Collected'
            // or similar)
            try (java.sql.Statement stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt.executeQuery(
                            "SELECT COUNT(*) FROM FOUND_ITEM WHERE Status != 'Collected' OR Status IS NULL")) {
                if (rs.next())
                    stats.put("Not Collected", rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching report stats: " + e.getMessage());
        }
        return stats;
    }

    /**
     * Get all users with their roles
     * 
     * @return List of arrays [ID, Name, Email, Role]
     */
    public static java.util.List<Object[]> getAllUsersWithRoles() {
        java.util.List<Object[]> users = new java.util.ArrayList<>();
        String query = "SELECT UserID, Name, Email FROM [User]";

        try (Connection conn = getConnection();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("UserID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String role = determineUserRole(conn, id); // Reuse helper
                users.add(new Object[] { id, name, email, role });
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Search for a user by ID
     * 
     * @param userId
     * @return Array [ID, Name, Email, Role] or null if not found
     */
    public static Object[] searchUserById(int searchId) {
        String query = "SELECT UserID, Name, Email FROM [User] WHERE UserID = ?";
        try (Connection conn = getConnection();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, searchId);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("UserID");
                    String name = rs.getString("Name");
                    String email = rs.getString("Email");
                    String role = determineUserRole(conn, id);
                    return new Object[] { id, name, email, role };
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching user: " + e.getMessage());
        }
        return null;
    }

    /**
     * Update a user's role
     * 
     * @return true if successful
     */
    public static boolean updateUserRole(int userId, String newRole) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Transaction
            try {
                // remove from all role tables first
                removeFromRoleTable(conn, "ADMIN", userId);
                removeFromRoleTable(conn, "OFFICER", userId);
                removeFromRoleTable(conn, "STUDENT", userId);

                // insert into new role table
                if ("Admin".equalsIgnoreCase(newRole)) {
                    insertToRoleTable(conn, "ADMIN", userId);
                } else if ("Officer".equalsIgnoreCase(newRole)) {
                    insertToRoleTable(conn, "OFFICER", userId);
                } else if ("Student".equalsIgnoreCase(newRole)) {
                    insertToRoleTable(conn, "STUDENT", userId);
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error updating role: " + e.getMessage());
            return false;
        }
    }

    private static void removeFromRoleTable(Connection conn, String table, int id) throws SQLException {
        try (java.sql.PreparedStatement pstmt = conn.prepareStatement("DELETE FROM [" + table + "] WHERE UserID = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private static void insertToRoleTable(Connection conn, String table, int id) throws SQLException {
        // Prepare statement based on table specific columns if any required not nulls.
        // Assuming simple tables for now or defaults.
        String sql = "";
        if ("Student".equalsIgnoreCase(table)) {
            // ERD shows: AcademicYear. Access check showed Major is invalid.
            sql = "INSERT INTO STUDENT (UserID, AcademicYear) VALUES (?, '1')";
        } else if ("Officer".equalsIgnoreCase(table)) {
            // ERD shows: SecurityBadgeNumber
            sql = "INSERT INTO OFFICER (UserID, SecurityBadgeNumber) VALUES (?, 'TEMP')";
        } else if ("Admin".equalsIgnoreCase(table)) {
            sql = "INSERT INTO ADMIN (UserID, Permissions) VALUES (?, 'Basic')";
        }

        if (!sql.isEmpty()) {
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * Add new user
     */
    public static boolean addUser(int id, String name, String email, String password, String role) {
        // UserID is an IDENTITY column. We omit it from the insert.
        String insertUser = "INSERT INTO [User] (Name, Email, PasswordHash, Role) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(insertUser,
                    java.sql.Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, password); // store plain text
                pstmt.setString(4, role);
                pstmt.executeUpdate();

                int generatedId = 0;
                try (java.sql.ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }

                // Assign Role (if needed, though we already stored it in the Role column)
                // Note: determineUserRole logic expects roles to be in sub-tables.
                conn.commit();

                if (generatedId > 0) {
                    updateUserRole(generatedId, role);
                    return true;
                }
                return false;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a user by ID
     */
    public static boolean deleteUser(int userId) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Remove from all potential role tables to be safe
                removeFromRoleTable(conn, "ADMIN", userId);
                removeFromRoleTable(conn, "OFFICER", userId);
                removeFromRoleTable(conn, "STUDENT", userId);

                // Remove from User table
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement("DELETE FROM [User] WHERE UserID = ?")) {
                    pstmt.setInt(1, userId);
                    int rows = pstmt.executeUpdate();
                    if (rows == 0) {
                        conn.rollback();
                        return false; // User not found
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

}
