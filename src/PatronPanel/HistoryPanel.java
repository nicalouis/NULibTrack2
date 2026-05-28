package PatronPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class HistoryPanel extends JPanel {

    private JPanel list;

    public static HistoryPanel instance;

    public HistoryPanel() {

        instance = this;

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        // ================= HEADER =================

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        header.setBackground(AppColor.BACKGROUND);
        header.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel icon = new JLabel(loadIcon("src/history.jpg", 45, 45));

        JLabel title = new JLabel("History Records");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 41, 59));

        header.add(icon);
        header.add(title);

        add(header, BorderLayout.NORTH);

        // ================= LIST =================

        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(AppColor.BACKGROUND);
        list.setBorder(BorderFactory.createEmptyBorder(10, 18, 20, 18));

        JScrollPane scroll = new JScrollPane(list);

        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppColor.BACKGROUND);

        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setUnitIncrement(14);

        add(scroll, BorderLayout.CENTER);

        load();
    }

    public void load() {

        list.removeAll();

        LibraryDB db = LibraryDB.get();

        // ================= HISTORY =================

        addSection("📜 Borrow & Return History");

        if (db.history.isEmpty()) {

            list.add(emptyCard("No history records yet."));

        } else {

            for (Book b : db.history) {

                if (b == null) continue;

                list.add(card(b, "RETURNED"));
                list.add(Box.createVerticalStrut(14));
            }
        }

        // ================= CURRENTLY BORROWED =================

        addSection("📚 Currently Borrowed");

        if (db.getBorrowedBooks().isEmpty()) {

            list.add(emptyCard("No borrowed books."));

        } else {

            for (Book b : db.getBorrowedBooks()) {

                if (b == null) continue;

                list.add(card(b, "BORROWED"));
                list.add(Box.createVerticalStrut(14));
            }
        }

        // ================= RESERVATIONS =================

        addSection("📌 Reservations");

        if (db.reservations.isEmpty()) {

            list.add(emptyCard("No reservations found."));

        } else {

            for (Book b : db.reservations) {

                if (b == null) continue;

                list.add(card(b, "RESERVED"));
                list.add(Box.createVerticalStrut(14));
            }
        }

        list.revalidate();
        list.repaint();
    }

    // ================= SECTION TITLE =================

    private void addSection(String text) {

        JPanel section = new JPanel(new FlowLayout(FlowLayout.LEFT));

        section.setBackground(AppColor.BACKGROUND);
        section.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));

        JLabel label = new JLabel(text);

        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(51, 65, 85));

        section.add(label);

        list.add(section);
    }

    // ================= EMPTY CARD =================

    private JPanel emptyCard(String text) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
        ));

        JLabel lbl = new JLabel(text);

        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lbl.setForeground(new Color(120, 120, 120));

        panel.add(lbl, BorderLayout.WEST);

        return panel;
    }

    // ================= CARD =================

    private JPanel card(Book b, String status) {

        JPanel card = new JPanel(new BorderLayout(14, 10));

        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // ================= IMAGE =================

        JLabel img = new JLabel(loadIcon(b.image, 95, 125));

        JPanel imagePanel = new JPanel(new BorderLayout());

        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(105, 130));

        imagePanel.add(img, BorderLayout.CENTER);

        // ================= TEXT AREA =================

        JPanel text = new JPanel();

        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);
        text.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        // ================= TITLE =================

        JLabel bookTitle = new JLabel(
                "<html><body style='width:260px'>" +
                (b.title != null ? b.title : "Unknown Title") +
                "</body></html>"
        );

        bookTitle.setFont(new Font("Segoe UI", Font.BOLD, 19));
        bookTitle.setForeground(new Color(15, 23, 42));
        bookTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ================= AUTHOR =================

        JLabel author = new JLabel(
                "by " +
                (b.author != null ? b.author : "Unknown Author")
        );

        author.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        author.setForeground(new Color(120, 120, 120));
        author.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ================= STATUS BADGE =================

        JLabel stat = new JLabel(status);

        stat.setFont(new Font("Segoe UI", Font.BOLD, 12));
        stat.setOpaque(true);

        stat.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        if ("BORROWED".equals(status)) {

            stat.setBackground(new Color(219, 234, 254));
            stat.setForeground(new Color(29, 78, 216));

        } else if ("RETURNED".equals(status)) {

            stat.setBackground(new Color(220, 252, 231));
            stat.setForeground(new Color(22, 101, 52));

        } else {

            stat.setBackground(new Color(254, 249, 195));
            stat.setForeground(new Color(161, 98, 7));
        }

        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        badgePanel.setBackground(Color.WHITE);
        badgePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        badgePanel.add(stat);

        // ================= DATE INFO =================

        JLabel dateInfo = new JLabel();

        if ("RETURNED".equals(status)) {

            dateInfo.setText(
                    "Returned Date: " + safe(b.returnDate)
            );

        } else if ("BORROWED".equals(status)) {

            dateInfo.setText(
                    "<html>" +
                    "<b>Borrowed:</b> " + safe(b.borrowDate) +
                    "<br>" +
                    "<b>Due:</b> " + safe(b.dueDate) +
                    "</html>"
            );

        } else if ("RESERVED".equals(status)) {

            dateInfo.setText(
                    "Reserved Date: " + safe(b.reserveDate)
            );
        }

        dateInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateInfo.setForeground(new Color(90, 90, 90));
        dateInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ================= ADD COMPONENTS =================

        text.add(bookTitle);
        text.add(Box.createVerticalStrut(5));
        text.add(author);
        text.add(Box.createVerticalStrut(10));
        text.add(badgePanel);
        text.add(Box.createVerticalStrut(10));
        text.add(dateInfo);

        card.add(imagePanel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    // ================= SAFE TEXT =================

    private String safe(String s) {

        return (s == null || s.isEmpty())
                ? "N/A"
                : s;
    }

    // ================= IMAGE =================

    private ImageIcon loadIcon(String path, int w, int h) {

        ImageIcon icon = new ImageIcon(path);

        Image img = icon.getImage();

        Image scaled = img.getScaledInstance(
                w,
                h,
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(scaled);
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