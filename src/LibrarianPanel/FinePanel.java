package LibrarianPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class FinePanel extends JPanel {

    private JPanel container;

    public FinePanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        // ================= HEADER (IMPROVED LAYOUT WRAP) =================
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 20));

        JLabel title = new JLabel("Student Fines Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(30, 30, 30));

        header.add(title);
        add(header, BorderLayout.NORTH);

        // ================= CONTAINER =================
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(AppColor.BACKGROUND);
        container.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppColor.BACKGROUND);

        add(scroll, BorderLayout.CENTER);

        load();
    }

    // ================= LOAD =================
    public void load() {

        container.removeAll();

        boolean found = false;

        LibraryDB db = LibraryDB.get();

        for (Book b : db.getBorrowedBooks()) {

            if (b == null) continue;

            boolean hasFine = b.getFine() > 0;
            boolean hasStatus = b.paymentRequested || b.paymentConfirmed;

            if (hasFine || hasStatus) {

                container.add(createFineCard(b));
                container.add(Box.createVerticalStrut(10));
                found = true;
            }
        }

        if (!found) {

            JLabel empty = new JLabel("No fines or payment activity.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            empty.setForeground(Color.GRAY);

            JPanel wrap = new JPanel();
            wrap.setBackground(AppColor.BACKGROUND);
            wrap.add(empty);

            container.add(Box.createVerticalStrut(20));
            container.add(wrap);
        }

        container.revalidate();
        container.repaint();
    }

    // ================= CARD (IMPROVED LAYOUT BALANCE) =================
    private JPanel createFineCard(Book b) {

        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // ================= LEFT =================
        JLabel name = new JLabel(b.title);
        name.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setBackground(Color.WHITE);
        left.add(name);

        // ================= CENTER =================
        JLabel amount = new JLabel("₱ " + b.getFine());
        amount.setFont(new Font("Segoe UI", Font.BOLD, 16));
        amount.setForeground(Color.RED);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        center.setBackground(Color.WHITE);
        center.add(amount);

        // ================= RIGHT =================
        String statusText;
        Color statusColor;

        if (b.paymentConfirmed) {
            statusText = "PAID";
            statusColor = new Color(0, 170, 0);

        } else if (b.paymentRequested) {
            statusText = "PENDING";
            statusColor = Color.ORANGE;

        } else if (b.getFine() > 0) {
            statusText = "OVERDUE";
            statusColor = Color.RED;

        } else {
            statusText = "CLEAR";
            statusColor = Color.GRAY;
        }

        JLabel status = new JLabel(statusText);
        status.setFont(new Font("Segoe UI", Font.BOLD, 13));
        status.setForeground(statusColor);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);
        right.add(status);

        // ================= ADD =================
        card.add(left, BorderLayout.WEST);
        card.add(center, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }
}