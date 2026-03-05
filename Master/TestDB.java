import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=LostAndFoundDB;encrypt=false;";
        String user = "sa";
        String pass = "eyad@123";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                
                String query = "SET IDENTITY_INSERT [User] ON; INSERT INTO [User] (UserID, Name, Email, PasswordHash) VALUES (?, ?, ?, ?); SET IDENTITY_INSERT [User] OFF;";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, 999);
                    pstmt.setString(2, "Test User");
                    pstmt.setString(3, "test@example.com");
                    pstmt.setString(4, "pass123");
                    int rows = pstmt.executeUpdate();
                    System.out.println("Inserted " + rows + " rows.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
