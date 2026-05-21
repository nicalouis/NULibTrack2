package PatronPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class FinePanel extends JPanel {

    private JPanel list;

    public FinePanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        // ================= HEADER =================
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);

        JLabel icon = new JLabel(loadIcon("src/money.jpg", 45, 45));

        JLabel title = new JLabel("Fines & Payments");
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

        for (Book b : LibraryDB.get().borrowed) {

            if (b.isOverdue()) {
                list.add(card(b));
                list.add(Box.createVerticalStrut(12));
            }
        }

        list.revalidate();
        list.repaint();
    }

    // ================= FIXED CARD =================
    private JPanel card(Book b) {

        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ================= IMAGE (LARGER & CLEAN) =================
        JLabel img = new JLabel(loadIcon(b.image, 85, 110));

        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(Color.WHITE);
        imgPanel.add(img);

        // ================= TEXT =================
        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);

        JLabel title = new JLabel(b.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel due = new JLabel("Due Date: " + (b.dueDate != null ? b.dueDate : "N/A"));
        due.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel fine = new JLabel("₱ " + b.getFine());
        fine.setForeground(AppColor.DANGER);
        fine.setFont(new Font("Segoe UI", Font.BOLD, 16));

        text.add(title);
        text.add(Box.createVerticalStrut(4));
        text.add(due);
        text.add(Box.createVerticalStrut(8));
        text.add(fine);

        // ================= BUTTON =================
        JButton pay = new JButton("PAY");
        pay.setBackground(AppColor.SUCCESS);
        pay.setForeground(Color.WHITE);
        pay.setFocusPainted(false);

        pay.addActionListener(e -> {
            b.clearFine();
            load();
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(pay);

        // ================= FINAL =================
        card.add(imgPanel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.EAST);

        return card;
    }

    private ImageIcon loadIcon(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}