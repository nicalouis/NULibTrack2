package PatronPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CatalogPanel extends JPanel {

    private JPanel list;
    private JTextField searchField;

    public CatalogPanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        // ================= HEADER =================
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);

        JLabel icon = new JLabel(loadIcon("src/search.jpg", 40, 40));

        JLabel title = new JLabel("Book Catalog");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        searchField = new JTextField(20);

        JButton searchBtn = new JButton("SEARCH");
        searchBtn.setBackground(AppColor.SECONDARY);

        header.add(icon);
        header.add(title);
        header.add(searchField);
        header.add(searchBtn);

        add(header, BorderLayout.NORTH);

        // ================= LIST =================
        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(AppColor.BACKGROUND);

        add(new JScrollPane(list), BorderLayout.CENTER);

        // ================= LIVE SEARCH =================
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                load(searchField.getText());
            }
        });

        searchBtn.addActionListener(e -> load(searchField.getText()));

        load("");
    }

    // ================= LOAD =================
    public void load(String keyword) {

        list.removeAll();

        ArrayList<Book> books = LibraryDB.get().books;

        for (Book b : books) {

            if (keyword == null || keyword.isEmpty()
                    || b.title.toLowerCase().startsWith(keyword.toLowerCase())
                    || b.author.toLowerCase().startsWith(keyword.toLowerCase())) {

                list.add(card(b));
                list.add(Box.createVerticalStrut(12));
            }
        }

        list.revalidate();
        list.repaint();
    }

    // ================= CARD (UPDATED UI ONLY) =================
    private JPanel card(Book b) {

        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ================= IMAGE (BIGGER + SPACED) =================
        JLabel img = new JLabel(loadIcon(b.image, 100, 130));

        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(Color.WHITE);
        imgPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 20));
        imgPanel.add(img);

        // ================= TEXT =================
        JLabel title = new JLabel(b.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel author = new JLabel("by " + b.author);
        author.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        author.setForeground(Color.DARK_GRAY);

        JLabel dateInfo = new JLabel();

        if (b.borrowDate != null) {
            dateInfo.setText("Borrowed: " + b.borrowDate + " | Due: " + b.dueDate);
        } else if (b.reserveDate != null) {
            dateInfo.setText("Reserved: " + b.reserveDate);
        } else {
            dateInfo.setText("Available");
        }

        dateInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        dateInfo.setForeground(AppColor.INFO);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        author.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        text.add(title);
        text.add(Box.createVerticalStrut(5));
        text.add(author);
        text.add(Box.createVerticalStrut(10));
        text.add(dateInfo);

        // ================= BUTTONS =================
        JButton borrow = new JButton("BORROW");
        JButton reserve = new JButton("RESERVE");

        borrow.setBackground(AppColor.SECONDARY);
        reserve.setBackground(AppColor.INFO);
        reserve.setForeground(Color.WHITE);

        borrow.setMaximumSize(new Dimension(120, 35));
        reserve.setMaximumSize(new Dimension(120, 35));

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBackground(Color.WHITE);

        buttons.add(borrow);
        buttons.add(Box.createVerticalStrut(10));
        buttons.add(reserve);

        // ================= ACTIONS =================
        borrow.addActionListener(e -> {
            LibraryDB.get().borrow(b);
            load(searchField.getText());
        });

        reserve.addActionListener(e -> {
            LibraryDB.get().reserve(b);
            load(searchField.getText());
        });

        // ================= FINAL =================
        card.add(imgPanel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.EAST);

        return card;
    }

    // ================= IMAGE =================
    private ImageIcon loadIcon(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}