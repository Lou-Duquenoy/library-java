package library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class BookDAO {

    /**
     * Adds a new book to the database.
     *
     * @param title  The title of the book.
     * @param author The author of the book.
     */
    public void addBook(String title, String author) {
        String sql = "INSERT INTO Book (title, author) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set title and author parameters in the SQL statement
            stmt.setString(1, title);
            stmt.setString(2, author);

            // Execute the insert statement
            stmt.executeUpdate();
            System.out.println("Book added: " + title);
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions that occur
        }
    }

    /**
     * Retrieves a list of all books in the database.
     *
     * @return A list of strings representing each book's details.
     */
    public List<String> listBooks() {
        List<String> books = new ArrayList<>();
        String sql = "SELECT * FROM Book";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterate through each result in the ResultSet
            while (rs.next()) {
                // Concatenate book details into a single string
                String book = "ID: " + rs.getInt("id") +
                        ", Title: " + rs.getString("title") +
                        ", Author: " + rs.getString("author") +
                        ", Borrowed: " + rs.getBoolean("isBorrowed");

                // Add the book details to the list
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions that occur
        }

        // Return the list of books
        return books;
    }

    /**
     * Retrieves the ID of a book based on its title.
     *
     * @param title The title of the book to search for.
     * @return The ID of the book if found, or -1 if not found.
     */
    public int getBookIdByTitle(String title) {
        String sql = "SELECT id FROM Book WHERE title = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the title parameter in the SQL statement
            stmt.setString(1, title);

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if a result exists and return the book ID
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions that occur
        }

        // Return -1 if the book is not found in the database
        return -1;
    }
}