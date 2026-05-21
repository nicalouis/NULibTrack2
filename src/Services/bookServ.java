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

    // ================= BORROW BOOK =================
    public void borrowBook(String title) {

        for (Book book : db.books) {

            if (book.title.equalsIgnoreCase(title)) {

                db.borrow(book);
                break;
            }
        }
    }

    // ================= RETURN BOOK =================
    public void returnBook(String title) {

        for (Book book : db.borrowed) {

            if (book.title.equalsIgnoreCase(title)) {

                db.returnBook(book);
                break;
            }
        }
    }
}