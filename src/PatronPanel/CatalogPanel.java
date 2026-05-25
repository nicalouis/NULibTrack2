package PatronPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class CatalogPanel extends JPanel {

    private JPanel list;
    private JTextField searchField;

    public static CatalogPanel instance;

    public CatalogPanel() {

        instance = this;

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);

        JLabel icon = new JLabel(loadIcon("src/search.jpg", 40, 40));

        JLabel title = new JLabel("Book Catalog");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        searchField = new JTextField(20);

        JButton searchBtn = new JButton("SEARCH");
        searchBtn.setBackground(AppColor.SECONDARY);
        searchBtn.setFocusPainted(false);

        header.add(icon);
        header.add(title);
        header.add(searchField);
        header.add(searchBtn);

        add(header, BorderLayout.NORTH);

        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(AppColor.BACKGROUND);

        add(new JScrollPane(list), BorderLayout.CENTER);

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                load(searchField.getText());
            }
        });

        searchBtn.addActionListener(e -> load(searchField.getText()));

        load("");
    }

    // ================= LOAD (🔥 FIXED FINAL STATE SYSTEM) =================
    public void load(String keyword) {

    list.removeAll();

    LibraryDB db = LibraryDB.get();

    for (Book b : db.books) {
        addIfVisible(b, keyword, db);
    }

    for (Book b : db.borrowed) {
        addIfVisible(b, keyword, db);
    }

    for (Book b : db.reservations) {
        addIfVisible(b, keyword, db);
    }

    list.revalidate();
    list.repaint();
}

private void addIfVisible(Book b, String keyword, LibraryDB db) {

    // 🔥 KEY FIX: if borrowed or reserved, DON'T show in catalog
    if (db.borrowed.contains(b) || db.reservations.contains(b)) {
        return;
    }

    boolean match =
            keyword == null || keyword.isEmpty()
            || b.title.toLowerCase().contains(keyword.toLowerCase())
            || b.author.toLowerCase().contains(keyword.toLowerCase());

    if (match) {
        list.add(card(b));
        list.add(Box.createVerticalStrut(12));
    }
}

    private void addIfMatch(Book b, String keyword) {

        boolean match =
                keyword == null || keyword.isEmpty()
                || b.title.toLowerCase().contains(keyword.toLowerCase())
                || b.author.toLowerCase().contains(keyword.toLowerCase());

        if (match) {
            list.add(card(b));
            list.add(Box.createVerticalStrut(12));
        }
    }

    // ================= CARD =================
    private JPanel card(Book b) {

        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel img = new JLabel(loadIcon(b.image, 100, 130));

        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(Color.WHITE);
        imgPanel.add(img);

        JLabel title = new JLabel(b.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel author = new JLabel("by " + b.author);
        author.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        author.setForeground(Color.DARK_GRAY);

        JLabel dateInfo = new JLabel();

        LibraryDB db = LibraryDB.get();

        // 🔥 FIXED STATE DISPLAY (NOW RETURNS PROPERLY)
        if (db.borrowed.contains(b) || b.borrowed) {

            dateInfo.setText(
                    b.dueDate != null
                            ? "Borrowed • Due: " + b.dueDate
                            : "Borrowed"
            );

        } else if (db.reservations.contains(b)) {

            dateInfo.setText(
                    b.reserveDate != null
                            ? "Reserved: " + b.reserveDate
                            : "Reserved"
            );

        } else {

            dateInfo.setText("Available");
        }

        dateInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        dateInfo.setForeground(AppColor.INFO);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);

        text.add(title);
        text.add(Box.createVerticalStrut(5));
        text.add(author);
        text.add(Box.createVerticalStrut(10));
        text.add(dateInfo);

        JButton borrow = new JButton("BORROW");
        JButton reserve = new JButton("RESERVE");

        borrow.setBackground(AppColor.SECONDARY);
        reserve.setBackground(AppColor.INFO);
        reserve.setForeground(Color.WHITE);

        borrow.setFocusPainted(false);
        reserve.setFocusPainted(false);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBackground(Color.WHITE);

        buttons.add(borrow);
        buttons.add(Box.createVerticalStrut(10));
        buttons.add(reserve);

        borrow.addActionListener(e -> {
            LibraryDB.get().borrow(b);
            refresh();
        });

        reserve.addActionListener(e -> {
            LibraryDB.get().reserve(b);
            refresh();
        });

        card.add(imgPanel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.EAST);

        return card;
    }

    public void refresh() {
        load(searchField.getText());
    }

    public static void forceRefresh() {
        if (instance != null) instance.refresh();
    }

    private ImageIcon loadIcon(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}