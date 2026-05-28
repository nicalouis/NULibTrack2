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
        setBackground(new Color(245, 247, 250));

        // ================= HEADER =================

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(24, 32, 16, 32));

        JPanel left = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                14,
                0
        ));

        left.setOpaque(false);

        JLabel icon = new JLabel("📚");
        icon.setFont(new Font(
                "Segoe UI Emoji",
                Font.PLAIN,
                34
        ));

        JPanel titleWrap = new JPanel();
        titleWrap.setLayout(new BoxLayout(
                titleWrap,
                BoxLayout.Y_AXIS
        ));
        titleWrap.setOpaque(false);

        JLabel title = new JLabel("Notifications");

        title.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                30
        ));

        title.setForeground(new Color(25, 35, 55));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(
                "Stay updated with your library activities"
        );

        sub.setFont(new Font(
                "Segoe UI",
                Font.PLAIN,
                13
        ));

        sub.setForeground(new Color(120, 120, 120));

        sub.setBorder(new EmptyBorder(
                2,
                2,
                0,
                0
        ));

        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleWrap.add(title);
        titleWrap.add(sub);

        left.add(icon);
        left.add(titleWrap);

        header.add(left, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ================= LIST =================

        list = new JPanel();

        list.setLayout(new BoxLayout(
                list,
                BoxLayout.Y_AXIS
        ));

        list.setOpaque(false);

        list.setBorder(new EmptyBorder(
                0,
                32,
                25,
                32
        ));

        JScrollPane scroll = new JScrollPane(list);

        scroll.setBorder(null);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.getViewport().setBackground(
                new Color(245, 247, 250)
        );

        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        load();
    }

    // ================= CURRENT DATE =================

    private String currentDate() {

        return new SimpleDateFormat(
                "MMM dd, yyyy • hh:mm a"
        ).format(new Date());
    }

    // ================= LOAD =================

    public void load() {

        list.removeAll();

        LibraryDB db = LibraryDB.get();

        // ================= AUTO OVERDUE CHECK =================

        for (LibraryDB.Book b : db.borrowed) {

            if (b == null) continue;

            if (b.dueDate != null && b.isOverdue()) {

                String overdueMsg =

                        "Book: \"" + b.title + "\"\n" +
                        "Borrow Date: " + b.borrowDate + "\n" +
                        "Due Date: " + b.dueDate + "\n" +
                        "Penalty: ₱" + b.getFine() + "\n" +
                        "Please return immediately.";

                if (!db.notifications.contains(overdueMsg)) {

                    db.notifications.add(0, overdueMsg);
                }
            }
        }

        // ================= EMPTY =================

        if (db.notifications.isEmpty()) {

            JPanel emptyCard = new JPanel();

            emptyCard.setLayout(new BoxLayout(
                    emptyCard,
                    BoxLayout.Y_AXIS
            ));

            emptyCard.setBackground(Color.WHITE);

            emptyCard.setBorder(BorderFactory.createCompoundBorder(

                    BorderFactory.createLineBorder(
                            new Color(228, 228, 228),
                            1,
                            true
                    ),

                    new EmptyBorder(
                            55,
                            40,
                            55,
                            40
                    )
            ));

            emptyCard.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel emptyIcon = new JLabel("📚");
            emptyIcon.setFont(new Font(
                    "Segoe UI Emoji",
                    Font.PLAIN,
                    54
            ));

            emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel emptyText = new JLabel(
                    "No notifications yet"
            );

            emptyText.setFont(new Font(
                    "Segoe UI",
                    Font.BOLD,
                    21
            ));

            emptyText.setForeground(
                    new Color(65, 65, 65)
            );

            emptyText.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            JLabel sub = new JLabel(
                    "Library updates and alerts will appear here."
            );

            sub.setFont(new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    13
            ));

            sub.setForeground(
                    new Color(145, 145, 145)
            );

            sub.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            emptyCard.add(emptyIcon);
            emptyCard.add(Box.createVerticalStrut(16));
            emptyCard.add(emptyText);
            emptyCard.add(Box.createVerticalStrut(7));
            emptyCard.add(sub);

            list.add(Box.createVerticalStrut(30));
            list.add(emptyCard);

        } else {

            for (String msg : db.notifications) {

                if (msg == null) continue;

                String lower = msg.toLowerCase();

                // ================= OVERDUE =================

                if (lower.contains("penalty")
                        || lower.contains("overdue")
                        || lower.contains("fine")) {

                    list.add(card(
                            "⚠ OVERDUE / PENALTY WARNING",
                            msg,
                            new Color(255, 248, 248),
                            new Color(220, 38, 38),
                            "URGENT",
                            "📕"
                    ));
                }

                // ================= BORROWED =================

                else if (lower.contains("borrow")) {

                    String enhanced = msg;

                    for (LibraryDB.Book b : db.borrowed) {

                        if (b != null && msg.contains(b.title)) {

                            enhanced =

                                    "Book: " + b.title + "\n" +
                                    "Borrow Date: " + b.borrowDate + "\n" +
                                    "Due Date: " + b.dueDate;
                        }
                    }

                    list.add(card(
                            "📘 BOOK BORROWED",
                            enhanced,
                            new Color(239, 246, 255),
                            new Color(37, 99, 235),
                            "BORROWED",
                            "📘"
                    ));
                }

                // ================= RESERVED =================

                else if (lower.contains("reserve")) {

                    String enhanced = msg;

                    for (LibraryDB.Book b : db.reservations) {

                        if (b != null && msg.contains(b.title)) {

                            enhanced =

                                    "Book: " + b.title + "\n" +
                                    "Reserved Date: " + b.reserveDate;
                        }
                    }

                    list.add(card(
                            "📌 RESERVATION SUCCESSFUL",
                            enhanced,
                            new Color(236, 253, 245),
                            new Color(22, 163, 74),
                            "RESERVED",
                            "📗"
                    ));
                }

                // ================= RETURN =================

                else if (lower.contains("return")) {

                    list.add(card(
                            "📚 BOOK RETURNED",
                            msg,
                            new Color(248, 250, 252),
                            new Color(71, 85, 105),
                            "RETURNED",
                            "📚"
                    ));
                }

                // ================= DEFAULT =================

                else {

                    list.add(card(
                            "🔔 LIBRARY NOTICE",
                            msg,
                            Color.WHITE,
                            new Color(45, 55, 72),
                            "NOTICE",
                            "📖"
                    ));
                }

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
            String bookIcon
    ) {

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(bg);

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        240
                )
        );

        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.setBorder(BorderFactory.createCompoundBorder(

                BorderFactory.createLineBorder(
                        new Color(228, 228, 228),
                        1,
                        true
                ),

                new EmptyBorder(
                        0,
                        0,
                        0,
                        0
                )
        ));

        // ================= LEFT SIDE =================

        JPanel leftSide = new JPanel(new BorderLayout());

        leftSide.setBackground(accent);
        leftSide.setPreferredSize(new Dimension(78, 0));

        JLabel image = new JLabel(bookIcon);

        image.setFont(new Font(
                "Segoe UI Emoji",
                Font.PLAIN,
                34
        ));

        image.setForeground(Color.WHITE);

        image.setHorizontalAlignment(SwingConstants.CENTER);

        leftSide.add(image, BorderLayout.CENTER);

        card.add(leftSide, BorderLayout.WEST);

        // ================= CONTENT =================

        JPanel content = new JPanel();

        content.setLayout(new BoxLayout(
                content,
                BoxLayout.Y_AXIS
        ));

        content.setBackground(bg);

        content.setBorder(new EmptyBorder(
                18,
                22,
                18,
                22
        ));

        // ================= TOP =================

        JPanel top = new JPanel(new BorderLayout());

        top.setOpaque(false);

        top.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                34
        ));

        JLabel t = new JLabel(title);

        t.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                17
        ));

        t.setForeground(accent);

        JLabel badge = new JLabel(" " + tag + " ");

        badge.setOpaque(true);

        badge.setBackground(accent);

        badge.setForeground(Color.WHITE);

        badge.setBorder(new EmptyBorder(
                4,
                10,
                4,
                10
        ));

        badge.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                10
        ));

        top.add(t, BorderLayout.WEST);
        top.add(badge, BorderLayout.EAST);

        // ================= DATE =================

        JLabel date = new JLabel(currentDate());

        date.setFont(new Font(
                "Segoe UI",
                Font.PLAIN,
                11
        ));

        date.setForeground(
                new Color(135, 135, 135)
        );

        date.setBorder(new EmptyBorder(
                3,
                1,
                0,
                0
        ));

        // ================= MESSAGE =================

        JTextArea msg = new JTextArea(message);

        msg.setFont(new Font(
                "Segoe UI",
                Font.PLAIN,
                13
        ));

        msg.setForeground(
                new Color(58, 58, 58)
        );

        msg.setBackground(bg);

        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);

        msg.setEditable(false);

        msg.setFocusable(false);

        msg.setBorder(new EmptyBorder(
                0,
                0,
                0,
                0
        ));

        msg.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ================= FOOTER =================

        JPanel footer = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        0,
                        0
                )
        );

        footer.setOpaque(false);

        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dot = new JLabel("● ");

        dot.setForeground(accent);

        dot.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                9
        ));

        JLabel footerText = new JLabel(
                "NULibTrack Library System"
        );

        footerText.setFont(new Font(
                "Segoe UI",
                Font.PLAIN,
                11
        ));

        footerText.setForeground(
                new Color(125, 125, 125)
        );

        footer.add(dot);
        footer.add(footerText);

        // ================= ADD =================

        content.add(top);
        content.add(Box.createVerticalStrut(7));
        content.add(date);
        content.add(Box.createVerticalStrut(14));
        content.add(msg);
        content.add(Box.createVerticalStrut(16));
        content.add(footer);

        card.add(content, BorderLayout.CENTER);

        return card;
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