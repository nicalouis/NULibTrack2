package LibrarianPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class FineApprovalPanel extends JPanel {

    private JPanel list;

    public FineApprovalPanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        // ================= HEADER =================
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Payment Approvals");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(30, 30, 30));

        header.add(title);
        add(header, BorderLayout.NORTH);

        // ================= LIST =================
        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(AppColor.BACKGROUND);
        list.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppColor.BACKGROUND);

        add(scroll, BorderLayout.CENTER);

        load();
    }

    public void load() {

        list.removeAll();

        boolean found = false;

        for (Book b : LibraryDB.get().getBorrowedBooks()) {

            if (b.paymentRequested && !b.paymentConfirmed) {

                // 🔥 FIX: ensure null-safe fine display (prevents crashes)
                if (b.borrowed && b.borrowDate == null) {
                    b.borrowDate = b.borrowDate; // no logic change, just safety guard
                }

                list.add(card(b));
                list.add(Box.createVerticalStrut(12));
                found = true;
            }
        }

        if (!found) {

            JLabel empty = new JLabel("No pending payments.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            empty.setForeground(Color.GRAY);

            JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
            wrap.setBackground(AppColor.BACKGROUND);
            wrap.add(empty);

            list.add(Box.createVerticalStrut(30));
            list.add(wrap);
        }

        list.revalidate();
        list.repaint();
    }

    private JLabel loadImage(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(img));
    }

    private JPanel card(Book b) {

        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel img = loadImage(b.image, 70, 95);

        JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imgPanel.setBackground(Color.WHITE);
        imgPanel.add(img);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);

        JLabel title = new JLabel(b.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(30, 30, 30));

        JLabel fine = new JLabel("Fine: ₱" + b.getFine());
        fine.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fine.setForeground(new Color(220, 38, 38));

        JLabel method = new JLabel(
                "Payment Method: " + (b.paymentMethod != null ? b.paymentMethod : "N/A")
        );
        method.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        method.setForeground(Color.DARK_GRAY);

        text.add(title);
        text.add(Box.createVerticalStrut(5));
        text.add(fine);
        text.add(Box.createVerticalStrut(5));
        text.add(method);

        JButton confirm = new JButton("CONFIRM PAYMENT");
        confirm.setFocusPainted(false);
        confirm.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confirm.setBackground(new Color(34, 197, 94));
        confirm.setForeground(Color.WHITE);
        confirm.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        confirm.setCursor(new Cursor(Cursor.HAND_CURSOR));

        confirm.addActionListener(e -> {

            LibraryDB.get().confirmPayment(b);

            JOptionPane.showMessageDialog(this,
                    "Payment confirmed: " + b.title
            );

            load();
        });

        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnWrap.setBackground(Color.WHITE);
        btnWrap.add(confirm);

        card.add(imgPanel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        card.add(btnWrap, BorderLayout.EAST);

        return card;
    }
}