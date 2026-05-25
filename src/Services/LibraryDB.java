package Services;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class LibraryDB {

    private static LibraryDB instance;

    public static LibraryDB get() {
        if (instance == null) instance = new LibraryDB();
        return instance;
    }

    // ================= DATABASE =================
    public ArrayList<Book> books = new ArrayList<>();
    public ArrayList<Book> borrowed = new ArrayList<>();
    public ArrayList<Book> reservations = new ArrayList<>();
    public ArrayList<Book> history = new ArrayList<>();

    public ArrayList<User> patrons = new ArrayList<>();
    public ArrayList<User> librarians = new ArrayList<>();
    public ArrayList<BorrowRecord> borrowRecords = new ArrayList<>();

    // ================= TEST DATE =================
    private String testDate() {
        return "Jan 10, 2025";
    }

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

    // ================= AUTO UI REFRESH =================
    private void triggerUI() {
        try {
            if (PatronPanel.CatalogPanel.instance != null) {
                PatronPanel.CatalogPanel.instance.refresh();
            }
        } catch (Exception ignored) {}
    }

    // ================= BORROW =================
    public void borrow(Book b) {

        if (b == null) return;
        if (b.borrowed) return;

        if (borrowed.size() >= 3) {
            JOptionPane.showMessageDialog(null,
                    "Maximum borrow limit is 3 books only!");
            return;
        }

        books.remove(b);
        reservations.remove(b);

        borrowed.add(b);

        b.borrowed = true;
        b.reserveDate = null;
        b.returnDate = null;

        b.borrowDate = testDate();
        b.dueDate = "7 days from " + b.borrowDate;

        triggerUI();
    }

    // ================= RETURN (FIXED SNAPSHOT + NO NULL HISTORY) =================
    public void returnBook(Book b) {

        if (b == null) return;
        if (!borrowed.contains(b)) return;

        String savedBorrowDate = b.borrowDate;
        String savedDueDate = b.dueDate;
        String savedReturnDate = now();

        borrowed.remove(b);
        borrowRecords.removeIf(r -> r.book == b);

        b.borrowed = false;
        b.borrowDate = null;
        b.dueDate = null;
        b.returnDate = savedReturnDate;

        // 🔥 FIX: clean snapshot (NO NULL FIELDS)
        Book snapshot = new Book(b.title, b.author, b.image);
        snapshot.borrowed = false;
        snapshot.borrowDate = savedBorrowDate;
        snapshot.dueDate = savedDueDate;
        snapshot.returnDate = savedReturnDate;

        history.add(snapshot);

        if (!books.contains(b)) {
            books.add(b);
        }

        reservations.remove(b);

        triggerUI();
    }

    // ================= RESERVE =================
    public void reserve(Book b) {

        if (b == null) return;
        if (b.borrowed) return;

        if (reservations.size() >= 3) {
            JOptionPane.showMessageDialog(null,
                    "Maximum reservation limit is 3 books only!");
            return;
        }

        books.remove(b);
        reservations.add(b);

        b.reserveDate = testDate();

        triggerUI();
    }

    // ================= CANCEL RESERVE =================
    public void cancelReserve(Book b) {

        if (b == null) return;

        reservations.remove(b);
        b.reserveDate = null;

        if (!b.borrowed && !books.contains(b)) {
            books.add(b);
        }

        triggerUI();
    }

    // ================= PAYMENT =================
    public void requestPayment(Book b, String method) {
        b.paymentRequested = true;
        b.paymentMethod = method;
        b.paymentConfirmed = false;
    }

    public void confirmPayment(Book b) {
        if (b.paymentRequested && !b.paymentConfirmed) {
            b.paymentConfirmed = true;
            b.paymentRequested = false;
            b.finePaid = true;
        }
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowed;
    }

    // ================= CLASSES =================
    public static class BorrowRecord {
        public String email;
        public Book book;

        public BorrowRecord(String email, Book book) {
            this.email = email;
            this.book = book;
        }
    }

    public static class Book {

        public String title;
        public String author;
        public String image;

        public boolean borrowed = false;
        public boolean finePaid = false;
        public boolean paymentRequested = false;
        public boolean paymentConfirmed = false;
        public String paymentMethod;

        public String borrowDate;
        public String returnDate;
        public String reserveDate;
        public String dueDate;

        public Book(String t, String a, String i) {
            title = t;
            author = a;
            image = i;
        }

        public int getFine() {
            try {
                if (borrowDate == null) return 0;

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                Date borrow = sdf.parse(borrowDate);
                Date now = new Date();

                long diff = now.getTime() - borrow.getTime();
                long days = diff / (1000 * 60 * 60 * 24);

                int allowed = 7;

                if (days <= allowed) return 0;

                return (int) (days - allowed) * 10;

            } catch (Exception e) {
                return 0;
            }
        }

        public boolean isOverdue() {
            return getFine() > 0;
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