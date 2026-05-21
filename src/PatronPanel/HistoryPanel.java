package PatronPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class HistoryPanel extends JPanel {

    private JPanel list;

    public HistoryPanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        // ================= HEADER =================
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);

        JLabel icon = new JLabel(loadIcon("src/history.jpg", 45, 45));

        JLabel title = new JLabel("History Records");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        header.add(icon);
        header.add(Box.createHorizontalStrut(10));
        header.add(title);

        add(header, BorderLayout.NORTH);

        // ================= LIST =================
        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(AppColor.BACKGROUND);

        add(new JScrollPane(list), BorderLayout.CENTER);

        load();
    }

    public void load() {

        list.removeAll();

        LibraryDB db = LibraryDB.get();

        addSection("Borrow / Return History");
        for (Book b : db.history) {
            list.add(card(b, "RETURNED"));
            list.add(Box.createVerticalStrut(12));
        }

        addSection("Currently Borrowed");
        for (Book b : db.borrowed) {
            list.add(card(b, "BORROWED"));
            list.add(Box.createVerticalStrut(12));
        }

        addSection("Reservations");
        for (Book b : db.reservations) {
            list.add(card(b, "RESERVED"));
            list.add(Box.createVerticalStrut(12));
        }

        list.revalidate();
        list.repaint();
    }

    // ================= SECTION =================
    private void addSection(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(AppColor.PRIMARY);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(AppColor.BACKGROUND);
        p.add(label);

        list.add(p);
    }

    // ================= FIXED CARD =================
    private JPanel card(Book b, String status) {

        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ================= IMAGE (FIXED SIZE) =================
        JLabel img = new JLabel(loadIcon(b.image, 85, 110));

        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(Color.WHITE);
        imgPanel.add(img);

        // ================= TEXT (FIXED ALIGNMENT) =================
        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);

        JLabel title = new JLabel(b.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel author = new JLabel(b.author);
        author.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        author.setForeground(Color.DARK_GRAY);

        JLabel stat = new JLabel(status);
        stat.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel dateInfo = new JLabel();

        if (status.equals("BORROWED")) {
            dateInfo.setText("Borrowed: " + b.borrowDate + " | Due: " + b.dueDate);
        } else if (status.equals("RETURNED")) {
            dateInfo.setText("Returned: " + b.returnDate);
        } else {
            dateInfo.setText("Reserved: " + b.reserveDate);
        }

        dateInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateInfo.setForeground(AppColor.INFO);

        text.add(title);
        text.add(Box.createVerticalStrut(3));
        text.add(author);
        text.add(Box.createVerticalStrut(5));
        text.add(stat);
        text.add(Box.createVerticalStrut(5));
        text.add(dateInfo);

        // ================= FINAL =================
        card.add(imgPanel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    private ImageIcon loadIcon(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}