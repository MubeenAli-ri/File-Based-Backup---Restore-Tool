// src/backend/db/DBConnection.java
package backend.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "backup_tool_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234"; // CHANGE THIS to your password
    
    private static Connection connection = null;
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Connecting to MySQL...");
                
                // Load driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // First connect without database name
                Connection tempConn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                
                // Create database if not exists
                Statement stmt = tempConn.createStatement();
                stmt.execute("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
                System.out.println("✓ Database ready");
                stmt.close();
                tempConn.close();
                
                // Connect to the specific database
                connection = DriverManager.getConnection(URL + DATABASE_NAME, USERNAME, PASSWORD);
                
                // Create table if not exists
                createTableIfNotExists();
                
                System.out.println("Connected to MySQL successfully!");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found!");
            System.err.println("Add mysql-connector-java.jar to lib folder");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("MySQL connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nMake sure:");
            System.err.println("1. MySQL Workbench is open");
            System.err.println("2. MySQL server is running (green circle in Workbench)");
            System.err.println("3. Password in DBConnection.java is correct");
            return null;
        }
    }
    
    private static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS backups (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "file_name VARCHAR(255) NOT NULL," +
                    "original_path VARCHAR(500) NOT NULL," +
                    "backup_path VARCHAR(500) NOT NULL," +
                    "backup_date TIMESTAMP NOT NULL," +
                    "file_size BIGINT NOT NULL" +
                    ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'backups' ready");
        } catch (SQLException e) {
            System.err.println("Failed to create table: " + e.getMessage());
        }
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection");
        }
    }
    
    public static boolean testConnection() {
        Connection conn = getConnection();
        return conn != null;
    }
}