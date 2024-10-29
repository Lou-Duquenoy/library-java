package library;

import java.util.ArrayList;

public class Library {

    // List to store all books in the library
    private ArrayList<Book> books;

    /**
     * Constructor to initialize the Library with an empty list of books.
     */
    public Library() {
        this.books = new ArrayList<>();
    }

    /**
     * Adds a new book to the library's collection.
     *
     * @param book The Book object to add to the library.
     */
    public void addBook(Book book) {
        books.add(book); // Add the book to the list
        System.out.println("Book added: " + book.getTitle()); // Confirm addition of the book
    }

    /**
     * Displays all books currently in the library's collection.
     * If there are no books, a message indicating an empty library is displayed.
     */
    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library."); // Message if the library has no books
        } else {
            for (Book book : books) {
                System.out.println(book); // Display each book's details
            }
        }
    }

    /**
     * Searches for a book in the library by its title.
     *
     * @param title The title of the book to search for.
     * @return The Book object if found; otherwise, returns null if no match is found.
     */
    public Book searchBookByTitle(String title) {
        // Iterate through each book in the list
        for (Book book : books) {
            // Check if the book's title matches the search title (case insensitive)
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book; // Return the book if a match is found
            }
        }
        return null; // Return null if no matching book is found
    }
}