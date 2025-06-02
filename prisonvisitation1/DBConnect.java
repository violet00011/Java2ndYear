package prisonvisitation1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    // Database URL, username, and password
    private static final String URL = "jdbc:mysql://localhost:3306/PRISON_VISITATION?serverTimezone=Asia/Manila&noAccessToProcedureBodies=true";
    private static final String USER = "armaine24";  // Replace with your DB username
    private static final String PASSWORD = "armaine";  // Replace with your DB password

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver (optional if using newer versions of JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found: " + e.getMessage());
            throw new SQLException("Database driver error", e);
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
            throw e;
        }
    }

    // Main method for testing the connection (optional)
    public static void main(String[] args) {
        try {
            Connection conn = DBConnect.getConnection();
            System.out.println("✅ Connected to MySQL!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("❌ Failed to connect: " + e.getMessage());
        }
    }
}
