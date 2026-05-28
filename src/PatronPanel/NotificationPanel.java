package PatronPanel;

import Services.LibraryDB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationPanel extends JPanel {

    private JPanel list;

    public static NotificationPanel instance;

    public NotificationPanel() {

        instance = this;

        setLayout(new BorderLayout());
        setBackground(new Color(245,247,250));

        // ================= HEADER =================

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(24,32,16,32));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,14,0));
        left.setOpaque(false);

        JLabel icon = new JLabel("📚");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));

        JPanel titleWrap = new JPanel();
        titleWrap.setLayout(new BoxLayout(titleWrap, BoxLayout.Y_AXIS));
        titleWrap.setOpaque(false);

        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(25,35,55));

        JLabel sub = new JLabel("Stay updated with your library activities");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(120,120,120));

        titleWrap.add(title);
        titleWrap.add(sub);

        left.add(icon);
        left.add(titleWrap);

        header.add(left, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ================= LIST =================

        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setBorder(new EmptyBorder(0,32,25,32));

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(245,247,250));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        load();
    }

    // ================= DATE =================

    private String currentDate() {

        return new SimpleDateFormat(
                "MMM dd, yyyy • hh:mm a"
        ).format(new Date());
    }

    // ================= LOAD =================

    public void load() {

        list.removeAll();

        LibraryDB db = LibraryDB.get();

        // ================= AUTO OVERDUE =================

        for (LibraryDB.Book b : db.borrowed) {

            if (b == null) continue;

            if (b.dueDate != null && b.isOverdue()) {

                String overdueMsg =
                        "Book: \"" + b.title + "\"\n" +
                        "Borrow Date: " + b.borrowDate + "\n" +
                        "Due Date: " + b.dueDate + "\n" +
                        "Penalty: ₱" + b.getFine();

                if (!db.notifications.contains(overdueMsg)) {
                    db.notifications.add(0, overdueMsg);
                }
            }
        }

        // ================= EMPTY =================

        if (db.notifications.isEmpty()) {

            JPanel empty = new JPanel();
            empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
            empty.setBackground(Color.WHITE);

            empty.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                            new Color(228,228,228),
                            1,
                            true
                    ),
                    new EmptyBorder(55,40,55,40)
            ));

            JLabel e1 = new JLabel("📚");
            e1.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 54));
            e1.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel e2 = new JLabel("No notifications yet");
            e2.setFont(new Font("Segoe UI", Font.BOLD, 21));
            e2.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel e3 = new JLabel(
                    "Library updates and alerts will appear here."
            );

            e3.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            e3.setForeground(new Color(145,145,145));
            e3.setAlignmentX(Component.CENTER_ALIGNMENT);

            empty.add(e1);
            empty.add(Box.createVerticalStrut(16));
            empty.add(e2);
            empty.add(Box.createVerticalStrut(6));
            empty.add(e3);

            list.add(Box.createVerticalStrut(30));
            list.add(empty);

        } else {

            for (String msg : db.notifications) {

                if (msg == null) continue;

                String lower = msg.toLowerCase();

                String title;
                String tag;
                Color bg;
                Color accent;

                if (lower.contains("penalty")
                        || lower.contains("overdue")
                        || lower.contains("fine")) {

                    title = "OVERDUE / PENALTY";
                    tag = "URGENT";
                    bg = new Color(255,248,248);
                    accent = new Color(220,38,38);

                } else if (lower.contains("borrow")) {

                    title = "BOOK BORROWED";
                    tag = "BORROWED";
                    bg = new Color(239,246,255);
                    accent = new Color(37,99,235);

                } else if (lower.contains("reserve")) {

                    title = "BOOK RESERVED";
                    tag = "RESERVED";
                    bg = new Color(236,253,245);
                    accent = new Color(22,163,74);

                } else if (lower.contains("return")) {

                    title = "BOOK RETURNED";
                    tag = "RETURNED";
                    bg = new Color(248,250,252);
                    accent = new Color(71,85,105);

                } else {

                    title = "LIBRARY NOTICE";
                    tag = "NOTICE";
                    bg = Color.WHITE;
                    accent = new Color(45,55,72);
                }

                LibraryDB.Book matchedBook = null;

                for (LibraryDB.Book b : db.books) {
                    if (b != null && msg.contains(b.title)) {
                        matchedBook = b;
                        break;
                    }
                }

                if (matchedBook == null) {
                    for (LibraryDB.Book b : db.borrowed) {
                        if (b != null && msg.contains(b.title)) {
                            matchedBook = b;
                            break;
                        }
                    }
                }

                if (matchedBook == null) {
                    for (LibraryDB.Book b : db.reservations) {
                        if (b != null && msg.contains(b.title)) {
                            matchedBook = b;
                            break;
                        }
                    }
                }

                list.add(card(
                        title,
                        msg,
                        bg,
                        accent,
                        tag,
                        matchedBook
                ));

                list.add(Box.createVerticalStrut(15));
            }
        }

        list.revalidate();
        list.repaint();
    }

    // ================= CARD =================

    private JPanel card(
            String title,
            String message,
            Color bg,
            Color accent,
            String tag,
            LibraryDB.Book book
    ) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);

        card.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                250
        ));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(228,228,228),
                        1,
                        true
                ),
                new EmptyBorder(0,0,0,0)
        ));

        // ================= LEFT =================

        JPanel left = new JPanel();
        left.setBackground(accent);
        left.setPreferredSize(new Dimension(10,0));

        // ================= CONTENT =================

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(bg);
        content.setBorder(new EmptyBorder(18,22,18,22));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 17));
        t.setForeground(accent);

        JLabel badge = new JLabel(" " + tag + " ");
        badge.setOpaque(true);
        badge.setBackground(accent);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setBorder(new EmptyBorder(4,10,4,10));

        top.add(t, BorderLayout.WEST);
        top.add(badge, BorderLayout.EAST);

        JLabel date = new JLabel(currentDate());
        date.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        date.setForeground(new Color(135,135,135));

        // ================= BOOK INFO =================

        JPanel bookPanel = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                12,
                0
        ));

        bookPanel.setOpaque(false);

        if (book != null) {

            JLabel img = new JLabel(loadBook(book.image, 55, 75));

            JPanel details = new JPanel();
            details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
            details.setOpaque(false);

            JLabel bt = new JLabel(book.title);
            bt.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JLabel ba = new JLabel("by " + book.author);
            ba.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            ba.setForeground(Color.GRAY);

            details.add(bt);
            details.add(Box.createVerticalStrut(4));
            details.add(ba);

            bookPanel.add(img);
            bookPanel.add(details);
        }

        JTextArea msg = new JTextArea(message);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        msg.setForeground(new Color(58,58,58));
        msg.setBackground(bg);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);
        msg.setEditable(false);
        msg.setFocusable(false);

        JPanel footer = new JPanel(
                new FlowLayout(FlowLayout.LEFT,0,0)
        );

        footer.setOpaque(false);

        JLabel foot = new JLabel("● NULibTrack Library System");
        foot.setForeground(new Color(125,125,125));
        foot.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        footer.add(foot);

        content.add(top);
        content.add(Box.createVerticalStrut(6));
        content.add(date);

        if (book != null) {
            content.add(Box.createVerticalStrut(12));
            content.add(bookPanel);
        }

        content.add(Box.createVerticalStrut(14));
        content.add(msg);
        content.add(Box.createVerticalStrut(16));
        content.add(footer);

        card.add(left, BorderLayout.WEST);
        card.add(content, BorderLayout.CENTER);

        return card;
    }

    // ================= IMAGE =================

    private ImageIcon loadBook(String path, int w, int h) {

        if (path == null || path.isEmpty()) {
            path = "src/defaultbook.png";
        }

        ImageIcon icon = new ImageIcon(path);

        Image img = icon.getImage().getScaledInstance(
                w,
                h,
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(img);
    }

    // ================= REFRESH =================

    public void refresh() {
        load();
    }

    public static void forceRefresh() {

        if (instance != null) {
            instance.refresh();
        }
    }
}