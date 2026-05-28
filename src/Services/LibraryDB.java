package Services;

import java.util.*;
import java.text.SimpleDateFormat;

public class LibraryDB {

    private static LibraryDB instance;

    public static LibraryDB get() {
        return instance == null ? instance = new LibraryDB() : instance;
    }

    // ================= DATABASE =================
    public ArrayList<Book> books = new ArrayList<>(),
            borrowed = new ArrayList<>(),
            reservations = new ArrayList<>(),
            history = new ArrayList<>();

    public ArrayList<String> notifications = new ArrayList<>();

    public ArrayList<User> patrons = new ArrayList<>(),
            librarians = new ArrayList<>();

    public ArrayList<BorrowRecord> borrowRecords = new ArrayList<>();

    public String currentUserEmail = "guest";

    private final SimpleDateFormat sdf =
            new SimpleDateFormat("MMM dd, yyyy");

    // ================= DATE HELPERS =================
    private String now() {
        return sdf.format(new Date());
    }

    private String addDays(int d) {
        return sdf.format(
                new Date(System.currentTimeMillis() + d * 86400000L)
        );
    }

    // ================= OWNER CHECK =================
    private boolean isOwner(Book b) {

        for (BorrowRecord r : borrowRecords)

            if (r.book == b &&
                    Objects.equals(r.email, currentUserEmail))

                return true;

        return false;
    }

    // ================= SYNC BORROWED =================
    private void syncBorrowed() {

        borrowed.clear();

        for (BorrowRecord r : borrowRecords)

            if (r != null &&
                    r.book != null &&
                    Objects.equals(r.email, currentUserEmail))

                borrowed.add(r.book);
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

        if (!isOwner(b) || b.borrowDate == null)
            return 0;

        try {

            long days =
                    (new Date().getTime() -
                    sdf.parse(b.borrowDate).getTime()) / 86400000;

            return days <= 7 ? 0 :
                    (int)(days - 7) * 10;

        } catch (Exception e) {
            return 0;
        }
    }

    // ================= OVERDUE =================
    private void checkOverdueNotifications() {

        syncBorrowed();

        for (Book b : borrowed)

            try {

                if (b.dueDate == null) continue;

                if (new Date().after(sdf.parse(b.dueDate))) {

                    String msg =
                            "⚠ OVERDUE: \"" + b.title + "\"\n" +
                            "Due Date: " + b.dueDate + "\n" +
                            "Current Fine: ₱" + getFine(b);

                    if (!notifications.contains(msg))
                        notifications.add(0, msg);
                }

            } catch (Exception ignored) {}
    }

    // ================= CONSTRUCTOR =================
    private LibraryDB() {

        patrons.add(
                new User("pat@gmail.com", "pat123", "PATRON")
        );

        librarians.add(
                new User("lib@gmail.com", "lib123", "LIBRARIAN")
        );

        books.add(new Book("Digital Marketing Strategy","Simon Kingsnorth","src/DMS.jpg"));
        books.add(new Book("Data Structure and Algorithms","Rudolph Russell","src/DSA.jpg"));
        books.add(new Book("Introduction to Java Programming","Steve Liang","src/IJP.jpg"));
        books.add(new Book("Basic Economics","Thomas Sowell","src/BS.jpg"));
        books.add(new Book("The C++ Programming Language","Bjarne Stroustrup","src/TCPL.jpg"));
        books.add(new Book("Java Programming","Hari Mohan Pandey","src/JP.jpg"));
        books.add(new Book("Beginning Python Programming","Matt Harison","src/BPP.png"));
        books.add(new Book("An Introduction to Creative Problem Solving","V. Anton Spraul","src/ICPS.jpg"));

        // ================= FIX: SYSTEM UNAVAILABLE BOOKS =================
        Book b1 = books.get(1),
             b2 = books.get(4);

        for (Book b : new Book[]{b1, b2}) {
            b.available = false;
            b.borrowed = true;
        }

        b1.borrowDate = "Jan 10, 2025";
        b1.dueDate = "Jan 17, 2025";

        b2.borrowDate = "Jan 12, 2025";
        b2.dueDate = "Jan 19, 2025";

        borrowRecords.add(new BorrowRecord("system", b1));
        borrowRecords.add(new BorrowRecord("system", b2));

        borrowed.add(b1);
        borrowed.add(b2);

        notifications.add("\"" + b1.title + "\" is currently unavailable.");
        notifications.add("\"" + b2.title + "\" is currently unavailable.");

        syncBorrowed();
        checkOverdueNotifications();
    }

    // ================= UI REFRESH =================
    private void triggerUI() {

        try {

            if (PatronPanel.CatalogPanel.instance != null)

                PatronPanel.CatalogPanel.instance.refresh();

        } catch (Exception ignored) {}
    }

    // ================= NOTIFICATIONS =================
    public void addNotification(String msg) {

        notifications.add(
                0,
                "[" +
                new SimpleDateFormat("MMM dd, yyyy hh:mm a")
                .format(new Date()) +
                "] " + msg
        );
    }

    // ================= MARK AVAILABLE =================
    public void markAvailable(Book b) {

        if (b == null) return;

        borrowRecords.removeIf(
                r -> r.book == b &&
                "system".equals(r.email)
        );

        reservations.remove(b);
        borrowed.remove(b);

        b.borrowed = false;
        b.available = true;

        b.borrowDate = b.dueDate = null;

        if (!books.contains(b))
            books.add(b);

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

        if (!books.contains(b))
            books.add(b);

        addNotification(
                "🚫 \"" + b.title + "\" is now UNAVAILABLE."
        );

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

            b.dueDate = sdf.format(
                    new Date(
                            sdf.parse(b.borrowDate).getTime() +
                            7L * 86400000
                    )
            );

        } catch (Exception e) {
            b.dueDate = addDays(7);
        }

        borrowRecords.add(
                new BorrowRecord(currentUserEmail, b)
        );

        syncBorrowed();

        boolean exists = false;

        for (Book hb : history)

            if (hb == b) {
                exists = true;
                break;
            }

        if (!exists &&
                !currentUserEmail.equals("system"))

            history.add(b);

        addNotification(
                "📚 Patron (" + currentUserEmail +
                ") borrowed \"" + b.title +
                "\" | Due: " + b.dueDate
        );

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

        b.borrowDate =
        b.dueDate =
        b.reserveDate = null;

        if (!books.contains(b))
            books.add(b);

        syncBorrowed();

        addNotification(
                "📢 \"" + b.title +
                "\" is now AVAILABLE for borrowing!"
        );

        triggerUI();
    }

    // ================= RETURN =================
    public void returnBook(Book b) {

        if (b == null) return;

        boolean mine = isOwner(b);

        borrowRecords.removeIf(
                r -> r.book == b &&
                Objects.equals(r.email, currentUserEmail)
        );

        b.borrowed = false;
        b.available = true;

        b.returnDate = now();
        b.borrowDate =
        b.dueDate = null;

        reservations.remove(b);

        if (mine) {

            boolean exists = false;

            for (Book hb : history)

                if (hb == b) {
                    exists = true;
                    break;
                }

            if (!exists)
                history.add(b);
        }

        if (!books.contains(b))
            books.add(b);

        syncBorrowed();

        addNotification(
                "✅ Patron (" + currentUserEmail +
                ") returned \"" + b.title + "\""
        );

        triggerUI();
    }

    // ================= RESERVE =================
    public String reserve(Book b) {

        if (b == null) return "INVALID";
        if (reservations.size() >= 3) return "LIMIT";

        books.remove(b);

        if (!reservations.contains(b))
            reservations.add(b);

        b.available = false;
        b.borrowed = false;

        b.reserveDate =
                b.reserveDate != null ?
                b.reserveDate : now();

        addNotification(
                "📘 Patron (" + currentUserEmail +
                ") reserved \"" + b.title +
                "\" on " + b.reserveDate
        );

        triggerUI();

        return "SUCCESS";
    }

    // ================= CANCEL RESERVE =================
    public void cancelReserve(Book b) {

        if (b == null) return;

        reservations.remove(b);
        b.reserveDate = null;

        if (!books.contains(b))
            books.add(b);

        addNotification(
                "Reservation cancelled: " + b.title
        );

        triggerUI();
    }

    // ================= PAYMENT =================
    public void requestPayment(Book b, String m) {
        b.paymentRequested = true;
        b.paymentMethod = m;
    }

    public void confirmPayment(Book b) {

        if (b.paymentRequested) {
            b.finePaid = true;
            b.paymentRequested = false;
        }
    }

    // ================= BOOK =================
    public static class Book {

        public String title, author, image,
                borrowDate, returnDate,
                reserveDate, dueDate,
                paymentMethod;

        public boolean borrowed = false,
                available = true,
                finePaid = false,
                paymentRequested = false,
                paymentConfirmed = false;

        public Book(String t, String a, String i) {
            title = t;
            author = a;
            image = i;
        }

        public int getFine() {

            try {

                if (borrowDate == null)
                    return 0;

                long days =
                        (new Date().getTime() -
                        new SimpleDateFormat("MMM dd, yyyy")
                        .parse(borrowDate).getTime()) / 86400000;

                return days <= 7 ? 0 :
                        (int)(days - 7) * 10;

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

        public String email, password, role;

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

        public BorrowRecord(String e, Book b) {
            email = e;
            book = b;
        }
    }
}