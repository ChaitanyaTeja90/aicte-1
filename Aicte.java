import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isBorrowed;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isBorrowed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", ISBN: " + isbn + ", Borrowed: " + isBorrowed;
    }
}

class Library {
    private ArrayList<Book> books;
    private Scanner scanner;
    private String filename;

    public Library(String filename) {
        this.books = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.filename = filename;
        loadBooks();
    }

    public void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book ISBN: ");
        String isbn = scanner.nextLine();

        Book book = new Book(title, author, isbn);
        books.add(book);
        System.out.println("Book added successfully.");
        saveBooks();
    }

    public void updateBook() {
        System.out.print("Enter ISBN of the book to update: ");
        String isbn = scanner.nextLine();

        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                System.out.print("Enter new title: ");
                String title = scanner.nextLine();
                System.out.print("Enter new author: ");
                String author = scanner.nextLine();

                book.title = title;
                book.author = author;
                System.out.println("Book updated successfully.");
                saveBooks();
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void deleteBook() {
        System.out.print("Enter ISBN of the book to delete: ");
        String isbn = scanner.nextLine();

        books.removeIf(book -> book.getIsbn().equals(isbn));
        System.out.println("Book deleted successfully.");
        saveBooks();
    }

    public void searchBook() {
        System.out.print("Search by (title/author/isbn): ");
        String searchBy = scanner.nextLine().toLowerCase();
        System.out.print("Enter search query: ");
        String query = scanner.nextLine().toLowerCase();

        for (Book book : books) {
            switch (searchBy) {
                case "title":
                    if (book.getTitle().toLowerCase().contains(query)) {
                        System.out.println(book);
                    }
                    break;
                case "author":
                    if (book.getAuthor().toLowerCase().contains(query)) {
                        System.out.println(book);
                    }
                    break;
                case "isbn":
                    if (book.getIsbn().toLowerCase().equals(query)) {
                        System.out.println(book);
                    }
                    break;
                default:
                    System.out.println("Invalid search criteria.");
                    return;
            }
        }
    }

    public void borrowBook() {
        System.out.print("Enter ISBN of the book to borrow: ");
        String isbn = scanner.nextLine();

        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                if (!book.isBorrowed()) {
                    book.setBorrowed(true);
                    System.out.println("Book borrowed successfully.");
                    saveBooks();
                    return;
                } else {
                    System.out.println("Book is already borrowed.");
                    return;
                }
            }
        }
        System.out.println("Book not found.");
    }

    public void returnBook() {
        System.out.print("Enter ISBN of the book to return: ");
        String isbn = scanner.nextLine();

        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                if (book.isBorrowed()) {
                    book.setBorrowed(false);
                    System.out.println("Book returned successfully.");
                    saveBooks();
                    return;
                } else {
                    System.out.println("Book is not borrowed.");
                    return;
                }
            }
        }
        System.out.println("Book not found.");
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private void saveBooks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Book book : books) {
                writer.println(book.getTitle() + "," + book.getAuthor() + "," + book.getIsbn() + "," + book.isBorrowed());
            }
        } catch (IOException e) {
            System.err.println("Error saving books to file: " + e.getMessage());
        }
    }

    private void loadBooks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String title = parts[0];
                    String author = parts[1];
                    String isbn = parts[2];
                    boolean isBorrowed = Boolean.parseBoolean(parts[3]);
                    Book book = new Book(title, author, isbn);
                    book.setBorrowed(isBorrowed);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading books from file: " + e.getMessage());
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library("books.txt");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Search Book");
            System.out.println("5. Borrow Book");
            System.out.println("6. Return Book");
            System.out.println("7. Display Books");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    library.addBook();
                    break;
                case 2:
                    library.updateBook();
                    break;
                case 3:
                    library.deleteBook();
                    break;
                case 4:
                    library.searchBook();
                    break;
                case 5:
                    library.borrowBook();
                    break;
                case 6:
                    library.returnBook();
                    break;
                case 7:
                    library.displayBooks();
                    break;
                case 8:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}