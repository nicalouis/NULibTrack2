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

    // ================= DATABASE =================
    public ArrayList<Book> books = new ArrayList<>();
    public ArrayList<Book> borrowed = new ArrayList<>();
    public ArrayList<Book> reservations = new ArrayList<>();
    public ArrayList<Book> history = new ArrayList<>();
    public ArrayList<String> notifications = new ArrayList<>();

    public ArrayList<User> patrons = new ArrayList<>();
    public ArrayList<User> librarians = new ArrayList<>();
    public ArrayList<BorrowRecord> borrowRecords = new ArrayList<>();

    public String currentUserEmail = "guest";

    // ================= DATE HELPERS =================
    private String now() {
        return new SimpleDateFormat("MMM dd, yyyy").format(new Date());
    }

    private String addDays(int days) {
        long ms = System.currentTimeMillis() + (long) days * 24 * 60 * 60 * 1000;
        return new SimpleDateFormat("MMM dd, yyyy").format(new Date(ms));
    }

    // ================= OWNER CHECK =================
    private boolean isOwner(Book b) {
        for (BorrowRecord r : borrowRecords) {
            if (r.book == b &&
                    r.email != null &&
                    r.email.equals(currentUserEmail)) {
                return true;
            }
        }
        return false;
    }

    // ================= SYNC BORROWED =================
    private void syncBorrowed() {

        borrowed.clear();

        for (BorrowRecord r : borrowRecords) {

            if (r == null || r.book == null) continue;

            if (r.email != null &&
                    r.email.equals(currentUserEmail)) {

                borrowed.add(r.book);
            }
        }
    }

    // ================= USER BORROWED LIST =================
    public ArrayList<Book> getBorrowedBooksForCurrentUser() {
        syncBorrowed();
        return borrowed;
    }

    public ArrayList<Book> getBorrowedBooks() {
        syncBorrowed();
        checkOverdueNotifications();
        return borrowed;
    }

    // ================= FIXED FINE =================
    public int getFine(Book b) {

        if (!isOwner(b)) return 0;

        try {
            if (b.borrowDate == null) return 0;

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

            Date borrow = sdf.parse(b.borrowDate);
            Date now = new Date();

            long diff = now.getTime() - borrow.getTime();
            long days = diff / (1000 * 60 * 60 * 24);

            int allowed = 7;

            if (days <= allowed) return 0;

            return (int)(days - allowed) * 10;

        } catch (Exception e) {
            return 0;
        }
    }

    // ================= OVERDUE =================
    private void checkOverdueNotifications() {

        syncBorrowed();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        Date today = new Date();

        for (Book b : borrowed) {

            try {
                if (b.dueDate == null) continue;

                Date due = sdf.parse(b.dueDate);

                if (today.after(due)) {

                    int fine = getFine(b);

                    String msg =
                            "⚠ OVERDUE: \"" + b.title + "\"\n" +
                                    "Due Date: " + b.dueDate + "\n" +
                                    "Current Fine: ₱" + fine;

                    if (!notifications.contains(msg)) {
                        notifications.add(0, msg);
                    }
                }

            } catch (Exception ignored) {}
        }
    }

    // ================= CONSTRUCTOR =================
    private LibraryDB() {

        patrons.add(new User("pat@gmail.com", "pat123", "PATRON"));
        librarians.add(new User("lib@gmail.com", "lib123", "LIBRARIAN"));

        books.add(new Book("Digital Marketing Strategy", "Simon Kingsnorth", "src/DMS.jpg"));
        books.add(new Book("Data Structure and Algorithms", "Rudolph Russell", "src/DSA.jpg"));
        books.add(new Book("Introduction to Java Programming", "Steve Liang", "src/IJP.jpg"));
        books.add(new Book("Basic Economics", "Thomas Sowell", "src/BS.jpg"));
        books.add(new Book("The C++ Programming Language", "Bjarne Stroustrup", "src/TCPL.jpg"));
        books.add(new Book("Java Programming", "Hari Mohan Pandey", "src/JP.jpg"));
        books.add(new Book("Beginning Python Programming", "Matt Harison", "src/BPP.png"));
        books.add(new Book("An Introduction to Creative Problem Solving", "V. Anton Spraul", "src/ICPS.jpg"));

        // ================= FIX: SYSTEM UNAVAILABLE BOOKS =================
        Book unavailable1 = books.get(1);
        unavailable1.available = false;
        unavailable1.borrowed = true;
        unavailable1.borrowDate = "Jan 10, 2025";
        unavailable1.dueDate = "Jan 17, 2025";

        Book unavailable2 = books.get(4);
        unavailable2.available = false;
        unavailable2.borrowed = true;
        unavailable2.borrowDate = "Jan 12, 2025";
        unavailable2.dueDate = "Jan 19, 2025";

        borrowRecords.add(new BorrowRecord("system", unavailable1));
        borrowRecords.add(new BorrowRecord("system", unavailable2));

        // ✅ FIX ADDED: THIS WAS MISSING (WHY YOUR BORROWED TABLE WAS EMPTY)
        borrowed.add(unavailable1);
        borrowed.add(unavailable2);

        notifications.add("\"" + unavailable1.title + "\" is currently unavailable.");
        notifications.add("\"" + unavailable2.title + "\" is currently unavailable.");

        syncBorrowed();
        checkOverdueNotifications();
    }

    // ================= UI REFRESH =================
    private void triggerUI() {
        try {
            if (PatronPanel.CatalogPanel.instance != null) {
                PatronPanel.CatalogPanel.instance.refresh();
            }
        } catch (Exception ignored) {}
    }

    // ================= NOTIFICATIONS =================
    public void addNotification(String message) {
        notifications.add(0, message);
    }

    // ================= MARK AVAILABLE =================
    public void markAvailable(Book b) {

        if (b == null) return;

        borrowRecords.removeIf(r -> r.book == b && "system".equals(r.email));

        reservations.remove(b);
        borrowed.remove(b);

        b.borrowed = false;
        b.available = true;

        b.borrowDate = null;
        b.dueDate = null;

        if (!books.contains(b)) books.add(b);

        syncBorrowed();

        addNotification("\"" + b.title + "\" is now available.");
        triggerUI();
    }

    // ================= MARK UNAVAILABLE =================
    public void markUnavailable(Book b) {

        if (b == null) return;

        b.available = false;
        b.borrowed = false;

        borrowed.remove(b);
        reservations.remove(b);

        if (!books.contains(b)) books.add(b);

        addNotification("🚫 \"" + b.title + "\" is now UNAVAILABLE.");

        triggerUI();
    }

    // ================= BORROW =================
    public String borrow(Book b) {

        if (b == null) return "INVALID";

        syncBorrowed();

        if (borrowed.size() >= 3) return "LIMIT";
        if (!b.available) return "UNAVAILABLE";

        books.remove(b);
        reservations.remove(b);

        b.borrowed = true;
        b.available = false;
        b.borrowDate = now();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            Date borrow = sdf.parse(b.borrowDate);

            long dueMs = borrow.getTime() + (7L * 24 * 60 * 60 * 1000);
            b.dueDate = new SimpleDateFormat("MMM dd, yyyy")
                    .format(new Date(dueMs));

        } catch (Exception e) {
            b.dueDate = addDays(7);
        }

        borrowRecords.add(new BorrowRecord(currentUserEmail, b));

        // ✅ FIX ADDED: MUST UPDATE BORROWED LIST FOR UI
        borrowed.add(b);

        syncBorrowed();

        addNotification("Borrowed: " + b.title);

        triggerUI();
        return "SUCCESS";
    }

    // ================= LIBRARIAN MARK AVAILABLE =================
    public void librarianMarkAvailable(Book b) {

        if (b == null) return;

        borrowRecords.removeIf(r -> r.book == b);
        reservations.remove(b);
        borrowed.remove(b);

        b.borrowed = false;
        b.available = true;

        b.borrowDate = null;
        b.dueDate = null;
        b.reserveDate = null;

        if (!books.contains(b)) books.add(b);

        syncBorrowed();

        addNotification("📢 \"" + b.title + "\" is now AVAILABLE for borrowing!");

        triggerUI();
    }

    // ================= RETURN =================
    public void returnBook(Book b) {

        if (b == null) return;

        boolean isMine = isOwner(b);

        borrowRecords.removeIf(r ->
                r.book == b &&
                r.email != null &&
                r.email.equals(currentUserEmail)
        );

        b.borrowed = false;
        b.available = true;

        b.returnDate = now();
        b.borrowDate = null;
        b.dueDate = null;

        reservations.remove(b);

        if (isMine) {
            boolean exists = false;
            for (Book hb : history) {
                if (hb == b) {
                    exists = true;
                    break;
                }
            }
            if (!exists) history.add(b);
        }

        if (!books.contains(b)) books.add(b);

        syncBorrowed();

        addNotification("Returned: " + b.title);

        triggerUI();
    }

    // ================= RESERVE =================
    public String reserve(Book b) {

        if (b == null) return "INVALID";

        if (reservations.size() >= 3) return "LIMIT";

        books.remove(b);
        reservations.add(b);

        b.reserveDate = (b.reserveDate != null ? b.reserveDate : now());

        addNotification("Reserved: " + b.title);

        triggerUI();

        return "SUCCESS";
    }

    // ================= CANCEL RESERVE =================
    public void cancelReserve(Book b) {

        if (b == null) return;

        reservations.remove(b);
        b.reserveDate = null;

        if (!books.contains(b)) books.add(b);

        addNotification("Reservation cancelled: " + b.title);

        triggerUI();
    }

    // ================= PAYMENT =================
    public void requestPayment(Book b, String method) {
        b.paymentRequested = true;
        b.paymentMethod = method;
    }

    public void confirmPayment(Book b) {
        if (b.paymentRequested) {
            b.finePaid = true;
            b.paymentRequested = false;
        }
    }

    // ================= BOOK =================
    public static class Book {
        public String title;
        public String author;
        public String image;

        public boolean borrowed = false;
        public boolean available = true;

        public String borrowDate;
        public String returnDate;
        public String reserveDate;
        public String dueDate;

        public boolean finePaid = false;
        public boolean paymentRequested = false;
        public boolean paymentConfirmed = false;
        public String paymentMethod;

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

                return (int)(days - allowed) * 10;

            } catch (Exception e) {
                return 0;
            }
        }

        public boolean isOverdue() {
            return getFine() > 0;
        }
    }

    // ================= USER =================
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

    // ================= BORROW RECORD =================
    public static class BorrowRecord {
        public String email;
        public Book book;

        public BorrowRecord(String email, Book book) {
            this.email = email;
            this.book = book;
        }
    }
}