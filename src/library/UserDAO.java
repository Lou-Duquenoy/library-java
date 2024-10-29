package library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Adds a new user to the database.
     *
     * @param name  The name of the user.
     * @param email The email of the user.
     */
    public void addUser(String name, String email) {
        String sql = "INSERT INTO User (name, email) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set name and email parameters in the SQL statement
            stmt.setString(1, name);
            stmt.setString(2, email);

            // Execute the insert statement
            stmt.executeUpdate();
            System.out.println("User added: " + name); // Confirm addition of the user
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions that occur
        }
    }

    /**
     * Retrieves the ID of a user based on their name.
     *
     * @param name The name of the user to search for.
     * @return The ID of the user if found, or -1 if not found.
     */
    public int getUserIdByName(String name) {
        String sql = "SELECT id FROM User WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the name parameter in the SQL statement
            stmt.setString(1, name);

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if a result exists and return the user ID
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions that occur
        }

        // Return -1 if the user is not found in the database
        return -1;
    }

    /**
     * Retrieves a list of all users in the database.
     *
     * @return A list of strings representing each user's details.
     */
    public List<String> listUsers() {
        List<String> users = new ArrayList<>();
        String sql = "SELECT * FROM User";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterate through each result in the ResultSet
            while (rs.next()) {
                // Concatenate user details into a single string
                String user = "ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Email: " + rs.getString("email");

                // Add the user details to the list
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions that occur
        }

        // Return the list of users
        return users;
    }
}