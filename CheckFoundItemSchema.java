import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

public class CheckFoundItemSchema {
    public static void main(String[] args) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("Master/db.properties")) {
            props.load(fis);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.pass");

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(
                            "SELECT c.name, t.name as tablename FROM sys.columns c JOIN sys.tables t ON c.object_id = t.object_id WHERE UPPER(t.name) = 'FOUND_ITEM'")) {
                System.out.println("Columns in FOUND_ITEM:");
                while (rs.next()) {
                    System.out.println(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
