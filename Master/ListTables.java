import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ListTables {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=LostAndFoundDB;encrypt=false;";
        String user = "sa";
        String pass = "eyad@123";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
                System.out.println("Tables in LostAndFoundDB:");
                while (rs.next()) {
                    System.out.println(rs.getString("TABLE_NAME"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
