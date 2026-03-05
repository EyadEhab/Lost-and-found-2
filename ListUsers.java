import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

public class ListUsers {
    public static void main(String[] args) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("Master/db.properties")) {
            props.load(fis);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.pass");

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT UserID, Name, PasswordHash, Role FROM [User]")) {
                System.out.println("ID | Name | Password | Role");
                while (rs.next()) {
                    System.out.println(rs.getInt("UserID") + " | " + rs.getString("Name") + " | "
                            + rs.getString("PasswordHash") + " | " + rs.getString("Role"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
