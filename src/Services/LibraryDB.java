package Services;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LibraryDB {

    private static LibraryDB instance;

    public static LibraryDB get() {
        if (instance == null) instance = new LibraryDB();
        return instance;
    }

    public ArrayList<Book> books = new ArrayList<>();
    public ArrayList<Book> borrowed = new ArrayList<>();
    public ArrayList<Book> reservations = new ArrayList<>();
    public ArrayList<Book> history = new ArrayList<>();

    public ArrayList<User> patrons = new ArrayList<>();
    public ArrayList<User> librarians = new ArrayList<>();

    private String now() {
        return new SimpleDateFormat("MMM dd, yyyy").format(new Date());
    }

    private LibraryDB() {

        patrons.add(new User("pat@gmail.com", "pat123", "PATRON"));
        librarians.add(new User("lib@gmail.com", "lib123", "LIBRARIAN"));

        books.add(new Book("Digital Marketing Strategy", "Simon Kingsnorth", "src/DMS.jpg"));
        books.add(new Book("Data Structure and Algorithms", "Rudolph Russell", "src/DSA.jpg"));
        books.add(new Book("Introduction to Java Programming", "Steve Liang", "src/IJP.jpg"));
        books.add(new Book("Basic Economics", "Thomas Sowell", "src/BS.jpg"));
    }

    // ================= FIXED BORROW =================
    public void borrow(Book b) {

        if (b == null) return;

        books.remove(b);
        reservations.remove(b);

        if (!borrowed.contains(b)) {
            borrowed.add(b);
        }

        b.borrowed = true;
        b.borrowDate = now();
        b.dueDate = "7 days from " + b.borrowDate;
        b.finePaid = false;
    }

    // ================= FIXED RETURN =================
    public void returnBook(Book b) {

        if (b == null) return;

        borrowed.remove(b);
        books.add(b);

        b.borrowed = false;
        b.returnDate = now();

        if (!history.contains(b)) {
            history.add(b);
        }
    }

    public void reserve(Book b) {

        if (b == null) return;

        books.remove(b);

        if (!reservations.contains(b)) {
            reservations.add(b);
        }

        b.reserveDate = now();
    }

    public void cancelReserve(Book b) {

        if (b == null) return;

        reservations.remove(b);
        books.add(b);
    }

    public static class Book {
        public String title;
        public String author;
        public String image;

        public boolean borrowed = false;
        public boolean finePaid = false;

        public String borrowDate;
        public String returnDate;
        public String reserveDate;
        public String dueDate;

        public Book(String t, String a, String i) {
            title = t;
            author = a;
            image = i;
        }

        public boolean isOverdue() {
            return borrowed;
        }

        public int getFine() {
            return borrowed ? (finePaid ? 0 : 50) : 0;
        }

        public void clearFine() {
            finePaid = true;
        }
    }

    public static class User {
        public String email;
        public String password;
        public String role;

        public User(String e, String p, String r) {
            email = e;
            password = p;
            role = r;
        }
    }
}