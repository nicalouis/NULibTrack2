package LibrarianPanel;

import Services.LibraryDB;

import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {

    public ReportsPanel(){

        setLayout(new GridLayout(2,2,20,20));

        LibraryDB db = LibraryDB.get();

        // 🔥 SAFETY CHECK (ADDED ONLY)
        if (db == null) return;

        add(createCard("Total Books", String.valueOf(db.books.size())));
        add(createCard("Active Members", String.valueOf(db.patrons.size())));
        add(createCard("Overdue Books", String.valueOf(getOverdueCount(db))));
        add(createCard("Reservations", String.valueOf(db.reservations.size())));
    }

    // ================= OVERDUE COUNT =================
    private int getOverdueCount(LibraryDB db){

        int count = 0;

        for (LibraryDB.Book b : db.borrowed) {

            if (b == null) continue; // 🔥 SAFETY FIX

            // ⚠️ FIXED LOGIC (your old version counted ALL books with dueDate)
            // Now it only counts ACTUALLY overdue books if fine exists or overdue flag exists
            if (b.borrowed && b.dueDate != null) {

                // improved check (still same structure, just safer meaning)
                if (b.getFine() > 0 || b.paymentRequested) {
                    count++;
                }
            }
        }

        return count;
    }

    // ================= CARD =================
    private JPanel createCard(String title, String value){

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setLayout(new GridLayout(2,1));

        JLabel t = new JLabel(title, SwingConstants.CENTER);
        JLabel v = new JLabel(value, SwingConstants.CENTER);

        t.setFont(new Font("Arial", Font.BOLD, 20));
        v.setFont(new Font("Arial", Font.BOLD, 35));

        panel.add(t);
        panel.add(v);

        return panel;
    }
}