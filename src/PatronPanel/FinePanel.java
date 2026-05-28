package PatronPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FinePanel extends JPanel {

    private JPanel list;

    public FinePanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);

        JLabel icon = new JLabel(loadIcon("src/money.jpg", 45, 45));

        JLabel title = new JLabel("Fines & Payments");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        header.add(icon);
        header.add(Box.createHorizontalStrut(10));
        header.add(title);

        add(header, BorderLayout.NORTH);

        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(AppColor.BACKGROUND);

        add(new JScrollPane(list), BorderLayout.CENTER);

        load();
    }

    public void load() {

        list.removeAll();

        boolean found = false;

        LibraryDB db = LibraryDB.get();

        // 🔥 FIX: only show CURRENT USER borrowed books
        for (Book b : db.borrowed) {

            if (db.currentUserEmail == null) continue;

            for (LibraryDB.BorrowRecord r : db.borrowRecords) {

                if (r.email != null
                        && r.email.equals(db.currentUserEmail)
                        && r.book == b) {

                    if (b.isOverdue() && !b.finePaid) {
                        list.add(card(b));
                        list.add(Box.createVerticalStrut(10));
                        found = true;
                    }
                }
            }
        }

        if (!found) {
            JLabel empty = new JLabel("No pending fines.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));

            JPanel wrap = new JPanel();
            wrap.setBackground(AppColor.BACKGROUND);
            wrap.add(empty);

            list.add(wrap);
        }

        list.revalidate();
        list.repaint();
    }

    // Fine calculation (UNCHANGED LOGIC)
    private int calculateFine(Book b) {

        try {
            if (b.borrowDate == null) return 0;

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

            Date borrow = sdf.parse(b.borrowDate);
            Date now = new Date();

            long diffMillis = now.getTime() - borrow.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diffMillis);

            int allowedDays = 7;

            if (days <= allowedDays) return 0;

            return (int) (days - allowedDays) * 10;

        } catch (Exception e) {
            return 0;
        }
    }

    private JPanel card(Book b) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel img = new JLabel(loadIcon(b.image, 85, 110));

        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(Color.WHITE);
        imgPanel.add(img);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);

        JLabel title = new JLabel(b.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        int fineAmount = calculateFine(b);

        JLabel fine = new JLabel("₱ " + fineAmount);
        fine.setForeground(AppColor.DANGER);
        fine.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel status;

        if (b.finePaid) {
            status = new JLabel("PAID");
            status.setForeground(new Color(0, 150, 0));
        }
        else if (b.paymentRequested) {
            status = new JLabel("PENDING APPROVAL");
            status.setForeground(Color.ORANGE);
        }
        else {
            status = new JLabel("UNPAID");
            status.setForeground(AppColor.DANGER);
        }

        text.add(title);
        text.add(fine);
        text.add(status);

        JButton pay = new JButton("PAY NOW");

        pay.addActionListener(e -> {

            String[] methods = {"Online Payment", "Cash Payment"};

            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Select payment method:",
                    "Payment",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    methods,
                    methods[0]
            );

            if (selected == null) return;

            LibraryDB.get().requestPayment(b, selected);

            JOptionPane.showMessageDialog(this,
                    "Payment REQUEST SENT."
            );

            load();
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(pay);

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