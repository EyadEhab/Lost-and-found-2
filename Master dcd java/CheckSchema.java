import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckSchema {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=LostAndFoundDB;encrypt=false;";
        String user = "sa";
        String pass = "eyad@123";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT c.name, c.is_nullable FROM sys.columns c JOIN sys.tables t ON c.object_id = t.object_id WHERE UPPER(t.name) = 'CLAIM'")) {
                while (rs.next()) {
                    System.out.println(rs.getString("name") + " - Nullable: " + rs.getBoolean("is_nullable"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
