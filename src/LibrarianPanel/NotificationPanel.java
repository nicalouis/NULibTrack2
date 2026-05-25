package LibrarianPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NotificationPanel extends JPanel {

    public NotificationPanel() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // ================= TITLE =================
        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(10, 15, 10, 10));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(245, 247, 250));
        top.add(title, BorderLayout.WEST);

        // ================= CONTAINER =================
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(10, 10, 10, 10));

        LibraryDB db = LibraryDB.get();

        boolean hasNotif = false;

        // ================= OVERDUE + BORROWED =================
        for (Book b : db.getBorrowedBooks()) {

            // ✔ FIXED LOGIC: borrowed = active book
            if (b.borrowed) {

                // ⚠️ simplified overdue rule (until real date system)
                if (!b.finePaid) {

                    container.add(createNotification(
                            "Overdue / Fine: " + b.title + " (₱" + b.getFine() + ")",
                            UIManager.getIcon("OptionPane.errorIcon"),
                            new Color(220, 38, 38)
                    ));

                    hasNotif = true;
                }
            }
        }

        // ================= PAYMENT REQUESTS =================
        for (Book b : db.getBorrowedBooks()) {

            if (b.paymentRequested && !b.paymentConfirmed) {

                container.add(createNotification(
                        "Payment Pending: " + b.title + " via " + b.paymentMethod,
                        UIManager.getIcon("OptionPane.warningIcon"),
                        new Color(255, 193, 7)
                ));

                hasNotif = true;
            }
        }

        // ================= EMPTY STATE =================
        if (!hasNotif) {

            container.add(createNotification(
                    "No new notifications",
                    UIManager.getIcon("OptionPane.informationIcon"),
                    new Color(59, 130, 246)
            ));
        }

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    // ================= CARD =================
    private JPanel createNotification(String message, Icon icon, Color color) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        card.setBackground(Color.WHITE);

        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(color);
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel(icon);
        iconPanel.add(iconLabel);

        JLabel msg = new JLabel(message);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msg.setBorder(new EmptyBorder(0, 10, 0, 0));

        card.add(iconPanel, BorderLayout.WEST);
        card.add(msg, BorderLayout.CENTER);

        return card;
    }
}