import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

public class CheckRoles {
    public static void main(String[] args) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("Master/db.properties")) {
            props.load(fis);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.pass");

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                    Statement stmt = conn.createStatement()) {

                String[] tables = { "ADMIN", "OFFICER", "STUDENT" };
                for (String table : tables) {
                    System.out.println("Table: " + table);
                    try (ResultSet rs = stmt.executeQuery("SELECT * FROM [" + table + "]")) {
                        ResultSetMetaData meta = rs.getMetaData();
                        int cols = meta.getColumnCount();
                        while (rs.next()) {
                            for (int i = 1; i <= cols; i++) {
                                System.out.print(rs.getObject(i) + " | ");
                            }
                            System.out.println();
                        }
                    } catch (Exception e) {
                        System.out.println("Error reading " + table + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
