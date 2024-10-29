package library;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAO();
        UserDAO userDAO = new UserDAO();
        BorrowDAO borrowDAO = new BorrowDAO();
        Scanner scanner = new Scanner(System.in);
        boolean continueProgram = true;

        while (continueProgram) {
            System.out.println("\n=== Library ===");
            System.out.println("1. Add a user");
            System.out.println("2. Add a book");
            System.out.println("3. Display books");
            System.out.println("4. Borrow a book");
            System.out.println("5. Return a book");
            System.out.println("6. Quit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            switch (choice) {
                case 1:
                    System.out.print("User name: ");
                    String name = scanner.nextLine();
                    System.out.print("User email: ");
                    String email = scanner.nextLine();
                    userDAO.addUser(name, email);
                    break;
                case 2:
                    System.out.print("Book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Book author: ");
                    String author = scanner.nextLine();
                    bookDAO.addBook(title, author);
                    break;
                case 3:
                    System.out.println("List of books:");
                    for (String book : bookDAO.listBooks()) {
                        System.out.println(book);
                    }
                    break;
                case 4:
                    System.out.print("Title of the book to borrow: ");
                    String borrowTitle = scanner.nextLine();
                    System.out.print("Name of the user borrowing: ");
                    String userName = scanner.nextLine();
                    borrowDAO.borrowBook(borrowTitle, userName);
                    break;
                case 5:
                    System.out.print("Title of the book to return: ");
                    String returnTitle = scanner.nextLine();
                    System.out.print("Name of the user returning: ");
                    String returningUserName = scanner.nextLine();
                    borrowDAO.returnBook(returnTitle, returningUserName);
                    break;
                case 6:
                    continueProgram = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
        System.out.println("Goodbye!");
    }
}