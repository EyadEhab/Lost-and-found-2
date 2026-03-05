import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

public class ListItems {
    public static void main(String[] args) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("Master/db.properties")) {
            props.load(fis);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.pass");

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT ItemID, Title, Category, Status FROM FOUND_ITEM")) {
                System.out.println("ID | Title | Category | Status");
                while (rs.next()) {
                    System.out.println(rs.getInt("ItemID") + " | " + rs.getString("Title") + " | "
                            + rs.getString("Category") + " | " + rs.getString("Status"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
