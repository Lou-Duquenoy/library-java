package library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database URL for the MySQL server and database name
    private static final String URL = "jdbc:mysql://host:port/name_db";
    private static final String USER = "myuser";      // Replace with your MySQL username
    private static final String PASSWORD = "mypassword";      // Replace with your MySQL password

    /**
     * Establishes and returns a connection to the database.
     *
     * @return A Connection object to the MySQL database, or null if the connection fails.
     */
    public static Connection getConnection() {
        try {
            // Attempt to establish a connection to the database using the provided URL, username, and password
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // If the connection fails, print an error message and stack trace for debugging
            System.out.println("Error connecting to the database.");
            e.printStackTrace();
            return null; // Return null to indicate the connection attempt was unsuccessful
        }
    }
}