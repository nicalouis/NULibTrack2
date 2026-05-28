package LibrarianPanel;

import Services.LibraryDB;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class LibrarianHomePanel extends JPanel {

    private JLabel booksValue;
    private JLabel usersValue;
    private JLabel finesValue;

    public LibrarianHomePanel() {

        setLayout(new GridLayout(1, 3, 20, 20));
        setBackground(AppColor.BACKGROUND);

        add(card("Books", AppColor.INFO, 1));
        add(card("Users", AppColor.SUCCESS, 2));
        add(card("Fines", AppColor.DANGER, 3));

        updateData();
    }

    // ================= CARD =================
    private JPanel card(String title, Color color, int type) {

        JPanel c = new JPanel();
        c.setLayout(new BorderLayout());
        c.setBackground(AppColor.CARD);
        c.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setForeground(color);

        JLabel v = new JLabel("0");
        v.setFont(new Font("Segoe UI", Font.BOLD, 30));

        if (type == 1) booksValue = v;
        if (type == 2) usersValue = v;
        if (type == 3) finesValue = v;

        c.add(t, BorderLayout.NORTH);
        c.add(v, BorderLayout.CENTER);

        return c;
    }

    // ================= UPDATE DATA (FIXED LIVE SYNC) =================
    public void updateData() {

        LibraryDB db = LibraryDB.get();

        // ================= BOOK COUNT FIX =================
        if (booksValue != null) {

            int totalBooks =
                    db.books.size() +
                    db.borrowed.size() +
                    db.reservations.size();

            booksValue.setText(String.valueOf(totalBooks));
        }

        // ================= USER COUNT =================
        if (usersValue != null) {
            usersValue.setText(
                    String.valueOf(db.patrons.size() + db.librarians.size())
            );
        }

        // ================= FINE COUNT FIX =================
        if (finesValue != null) {

            int totalFines = 0;

            for (LibraryDB.Book b : db.borrowed) {

                if (b != null) {
                    totalFines += b.getFine();
                }
            }

            finesValue.setText("₱" + totalFines);
        }

        revalidate();
        repaint();
    }
}