package Services;

import Services.LibraryDB.Book;
import java.util.ArrayList;

public class bookServ {

    private final LibraryDB db = LibraryDB.get();

    public ArrayList<Book> getAllBooks() {
        return db.books;
    }

    public void addBook(Book book) {
        db.books.add(book);
    }

    public void removeBook(String title) {

        for (int i = 0; i < db.books.size(); i++) {
            if (db.books.get(i).title.equalsIgnoreCase(title)) {
                db.books.remove(i);
                break;
            }
        }
    }

    public Book searchBook(String title) {

        for (Book book : db.books) {
            if (book.title.equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    // ================= BORROW =================
    public void borrowBook(String title) {

        for (Book book : db.books) {

            if (book.title.equalsIgnoreCase(title)) {

                db.borrow(book);
                return;
            }
        }
    }

    // ================= RETURN =================
    public void returnBook(String title) {

        for (Book book : db.borrowed) {

            if (book.title.equalsIgnoreCase(title)) {

                db.returnBook(book);
                return;
            }
        }
    }
}