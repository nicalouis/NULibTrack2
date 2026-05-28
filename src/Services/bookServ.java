package Services;

import Services.LibraryDB.Book;
import java.util.ArrayList;

public class bookServ {

    private final LibraryDB db = LibraryDB.get();

    // ================= GET ALL BOOKS =================
    public ArrayList<Book> getAllBooks() {
        return db.books;
    }

    // ================= ADD BOOK =================
    public void addBook(Book book) {
        if (book == null) return;
        db.books.add(book);
    }

    // ================= REMOVE BOOK =================
    public void removeBook(String title) {

        for (int i = 0; i < db.books.size(); i++) {

            if (db.books.get(i).title.equalsIgnoreCase(title)) {
                db.books.remove(i);
                break;
            }
        }
    }

    // ================= SEARCH BOOK =================
    public Book searchBook(String title) {

        for (Book book : db.books) {

            if (book.title.equalsIgnoreCase(title)) {
                return book;
            }
        }

        return null;
    }

    // ================= BORROW BOOK (GLOBAL SYSTEM FIX) =================
    public void borrowBook(String title) {

        for (Book book : db.books) {

            if (book.title.equalsIgnoreCase(title)) {

                // 🔥 GLOBAL RULE: book is controlled by LibraryDB (not "current patron ownership")
                if (!book.available) {
                    System.out.println("Book is currently unavailable (already borrowed in system).");
                    return;
                }

                db.borrow(book);
                return;
            }
        }
    }

    // ================= RETURN BOOK =================
    public void returnBook(String title) {

        for (Book book : db.borrowed) {

            if (book.title.equalsIgnoreCase(title)) {

                db.returnBook(book);
                return;
            }
        }
    }

    // ================= MARK BOOK AVAILABLE =================
    public void markAvailable(String title) {

        // 🔥 FIX: must search BOTH borrowed + system state consistency
        for (Book book : db.borrowed) {

            if (book.title.equalsIgnoreCase(title)) {

                db.markAvailable(book);
                return;
            }
        }
    }

    // ================= MARK BOOK UNAVAILABLE =================
    public void markUnavailable(String title) {

        for (Book book : db.books) {

            if (book.title.equalsIgnoreCase(title)) {

                book.available = false;
                return;
            }
        }
    }
}