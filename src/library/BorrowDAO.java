package library;

import java.sql.*;
import java.time.LocalDate;

public class BorrowDAO {

    // Instances of BookDAO and UserDAO to interact with book and user data
    private BookDAO bookDAO = new BookDAO();
    private UserDAO userDAO = new UserDAO();

    /**
     * Allows a user to borrow a book if it is available.
     *
     * @param bookTitle The title of the book to borrow.
     * @param userName  The name of the user borrowing the book.
     */
    public void borrowBook(String bookTitle, String userName) {
        // Retrieve the book ID and user ID based on title and username
        int bookId = bookDAO.getBookIdByTitle(bookTitle);
        int userId = userDAO.getUserIdByName(userName);

        // Check if the book exists in the database
        if (bookId == -1) {
            System.out.println("Book not found: " + bookTitle);
            return;
        }
        // Check if the user exists in the database
        if (userId == -1) {
            System.out.println("User not found: " + userName);
            return;
        }

        // SQL query to check if the book is already borrowed
        String checkBorrowedSql = "SELECT isBorrowed FROM Book WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkBorrowedSql)) {

            // Set the book ID parameter and execute the query
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            // If the book is already borrowed, display a message and return
            if (rs.next() && rs.getBoolean("isBorrowed")) {
                System.out.println("The book \"" + bookTitle + "\" is already borrowed.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions that occur
            return;
        }

        // SQL queries for inserting the borrow record and updating the book status
        String sqlBorrow = "INSERT INTO Borrow (book_id, user_id, borrow_date) VALUES (?, ?, ?)";
        String sqlUpdateBook = "UPDATE Book SET isBorrowed = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtBorrow = conn.prepareStatement(sqlBorrow);
             PreparedStatement stmtUpdateBook = conn.prepareStatement(sqlUpdateBook)) {

            // Create the borrow record in the Borrow table
            stmtBorrow.setInt(1, bookId);
            stmtBorrow.setInt(2, userId);
            stmtBorrow.setDate(3, Date.valueOf(LocalDate.now())); // Set current date as borrow date
            stmtBorrow.executeUpdate();

            // Update the book's status to indicate it is borrowed
            stmtUpdateBook.setBoolean(1, true);
            stmtUpdateBook.setInt(2, bookId);
            stmtUpdateBook.executeUpdate();

            System.out.println("The book \"" + bookTitle + "\" has been borrowed by " + userName);
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions that occur
        }
    }

    /**
     * Allows a user to return a borrowed book.
     * Only the user who borrowed the book can return it.
     *
     * @param bookTitle The title of the book to return.
     * @param userName  The name of the user returning the book.
     */
    public void returnBook(String bookTitle, String userName) {
        // Retrieve the book ID and user ID based on title and username
        int bookId = bookDAO.getBookIdByTitle(bookTitle);
        int userId = userDAO.getUserIdByName(userName);

        // Check if the book exists in the database
        if (bookId == -1) {
            System.out.println("Book not found: " + bookTitle);
            return;
        }
        // Check if the user exists in the database
        if (userId == -1) {
            System.out.println("User not found: " + userName);
            return;
        }

        // SQL query to check if the specified user has borrowed this book and has not yet returned it
        String checkBorrowerSql = "SELECT * FROM Borrow WHERE book_id = ? AND user_id = ? AND return_date IS NULL";
        String sqlReturn = "UPDATE Borrow SET return_date = ? WHERE book_id = ? AND user_id = ?";
        String sqlUpdateBook = "UPDATE Book SET isBorrowed = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkBorrowerSql);
             PreparedStatement stmtReturn = conn.prepareStatement(sqlReturn);
             PreparedStatement stmtUpdateBook = conn.prepareStatement(sqlUpdateBook)) {

            // Set parameters to check if the user has an active borrow record for this book
            checkStmt.setInt(1, bookId);
            checkStmt.setInt(2, userId);
            ResultSet rs = checkStmt.executeQuery();

            // If no active borrow record exists, display a message and return
            if (!rs.next()) {
                System.out.println("The book \"" + bookTitle + "\" was not borrowed by " + userName + " or has already been returned.");
                return;
            }

            // If the check passes, proceed with returning the book

            // 1. Update the return date in the Borrow table to mark it as returned
            stmtReturn.setDate(1, Date.valueOf(LocalDate.now())); // Set current date as return date
            stmtReturn.setInt(2, bookId);
            stmtReturn.setInt(3, userId);
            stmtReturn.executeUpdate();

            // 2. Update the isBorrowed status in the Book table to make it available again
            stmtUpdateBook.setBoolean(1, false);
            stmtUpdateBook.setInt(2, bookId);
            stmtUpdateBook.executeUpdate();

            System.out.println("The book \"" + bookTitle + "\" has been returned by " + userName);
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions that occur
        }
    }
}