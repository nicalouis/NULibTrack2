package PatronPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class BorrowedPanel extends JPanel {

    private JPanel list;

    public BorrowedPanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        // ================= HEADER =================
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);

        JLabel icon = new JLabel(loadIcon("src/borrow.jpg", 40, 40));

        JLabel title = new JLabel("Borrowed Books");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        header.add(icon);
        header.add(title);

        add(header, BorderLayout.NORTH);

        // ================= LIST =================
        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(AppColor.BACKGROUND);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppColor.BACKGROUND);

        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    // ================= REFRESH =================
    public void refresh() {

        list.removeAll();

        LibraryDB db = LibraryDB.get();

        boolean found = false;

        for (LibraryDB.BorrowRecord r : db.borrowRecords) {

            if (r == null) continue;
            if (r.email == null) continue;
            if (!r.email.equals(db.currentUserEmail)) continue;
            if (r.book == null) continue;

            Book b = r.book;

            // 🔥 FIX: DO NOT RELY ON b.borrowed (THIS WAS BREAKING YOUR SYSTEM)
            list.add(card(b));
            list.add(Box.createVerticalStrut(12));
            found = true;
        }

        if (!found) {

            JLabel empty = new JLabel("No borrowed books yet.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JPanel wrap = new JPanel();
            wrap.setBackground(AppColor.BACKGROUND);
            wrap.add(empty);

            list.add(Box.createVerticalStrut(20));
            list.add(wrap);
        }

        list.revalidate();
        list.repaint();
    }

    // ================= CARD =================
    private JPanel card(Book b) {

        JPanel p = new JPanel(new BorderLayout(15, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel img = new JLabel(loadIcon(b.image, 80, 105));

        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(Color.WHITE);
        imgPanel.setPreferredSize(new Dimension(100, 110));
        imgPanel.add(img);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);

        JLabel title = new JLabel(b.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel dateInfo = new JLabel();

        // ================= FIXED DATE DISPLAY =================
        String borrow = (b.borrowDate != null) ? b.borrowDate : "N/A";
        String due = (b.dueDate != null) ? b.dueDate : "N/A";

        String info = "Borrowed: " + borrow + " | Due: " + due;

        if (b.isOverdue()) {
            dateInfo.setText(info + " (OVERDUE)");
            dateInfo.setForeground(new Color(220, 38, 38));
        } else {
            dateInfo.setText(info);
            dateInfo.setForeground(Color.GRAY);
        }

        dateInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        text.add(title);
        text.add(Box.createVerticalStrut(5));
        text.add(dateInfo);

        JButton returnBtn = new JButton("RETURN");
        returnBtn.setBackground(AppColor.SECONDARY);
        returnBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        returnBtn.setFocusPainted(false);

        returnBtn.addActionListener(e -> {

            LibraryDB db = LibraryDB.get();

            db.returnBook(b);

            // 🔥 FULL SYNC FIX
            refresh();

            if (PatronPanel.CatalogPanel.instance != null) {
                PatronPanel.CatalogPanel.instance.refresh();
            }
        });

        p.add(imgPanel, BorderLayout.WEST);
        p.add(text, BorderLayout.CENTER);
        p.add(returnBtn, BorderLayout.EAST);

        return p;
    }

    // ================= IMAGE =================
    private ImageIcon loadIcon(String path, int w, int h) {

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}