package LibrarianPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;

import javax.swing.*;
import java.awt.*;

public class BorrowReturnPanel extends JPanel {

    public BorrowReturnPanel() {

        setLayout(new GridLayout(1, 2, 15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ================= ISSUE BOOK =================
        JPanel borrowPanel = createCard("Issue Book");

        JTextField emailField = styledField("pat@gmail.com");
        JTextField bookTitleField = styledField("");

        JButton issueBtn = styledButton("ISSUE", new Color(34, 197, 94));

        issueBtn.addActionListener(e -> {

            String email = emailField.getText().trim();
            String bookTitle = bookTitleField.getText().trim();

            if (email.isEmpty() || bookTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields");
                return;
            }

            LibraryDB db = LibraryDB.get();

            LibraryDB.User foundUser = null;

            for (LibraryDB.User u : db.patrons) {
                if (u.email.equalsIgnoreCase(email)) {
                    foundUser = u;
                    break;
                }
            }

            if (foundUser == null) {
                JOptionPane.showMessageDialog(this, "Patron email not found!");
                return;
            }

            // 🔥 FIX: global borrow size check removed (it was wrong)
            // Borrow limit should be per user, NOT system-wide

            Book foundBook = null;

            for (Book b : db.books) {
                if (b.title.equalsIgnoreCase(bookTitle)) {
                    foundBook = b;
                    break;
                }
            }

            if (foundBook == null) {
                JOptionPane.showMessageDialog(this, "Book not found!");
                return;
            }

            if (foundBook.borrowed) {
                JOptionPane.showMessageDialog(this, "Book is already borrowed!");
                return;
            }

            db.borrow(foundBook);

            JOptionPane.showMessageDialog(this,
                    "Book issued successfully!\n\n"
                            + "Patron: " + foundUser.email
                            + "\nBook: " + foundBook.title
            );

            emailField.setText("pat@gmail.com");
            bookTitleField.setText("");
        });

        borrowPanel.add(label("Patron Gmail"));
        borrowPanel.add(emailField);

        borrowPanel.add(label("Book Title"));
        borrowPanel.add(bookTitleField);

        borrowPanel.add(issueBtn);

        // ================= RETURN BOOK =================
        JPanel returnPanel = createCard("Return Book");

        JTextField returnEmailField = styledField("pat@gmail.com");
        JTextField returnBookField = styledField("");

        JButton returnBtn = styledButton("RETURN", new Color(239, 68, 68));

        returnBtn.addActionListener(e -> {

            String email = returnEmailField.getText().trim();
            String title = returnBookField.getText().trim();

            if (email.isEmpty() || title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields");
                return;
            }

            LibraryDB db = LibraryDB.get();

            LibraryDB.User foundUser = null;

            for (LibraryDB.User u : db.patrons) {
                if (u.email.equalsIgnoreCase(email)) {
                    foundUser = u;
                    break;
                }
            }

            if (foundUser == null) {
                JOptionPane.showMessageDialog(this, "Patron email not found!");
                return;
            }

            Book foundBook = null;

            for (Book b : db.borrowed) {
                if (b.title.equalsIgnoreCase(title)) {
                    foundBook = b;
                    break;
                }
            }

            if (foundBook == null) {
                JOptionPane.showMessageDialog(this, "Book not currently borrowed!");
                return;
            }

            db.returnBook(foundBook);

            JOptionPane.showMessageDialog(this,
                    "Book returned successfully!\n\n"
                            + "Patron: " + foundUser.email
                            + "\nBook: " + foundBook.title
            );

            returnEmailField.setText("pat@gmail.com");
            returnBookField.setText("");
        });

        returnPanel.add(label("Patron Gmail"));
        returnPanel.add(returnEmailField);

        returnPanel.add(label("Book Title"));
        returnPanel.add(returnBookField);

        returnPanel.add(returnBtn);

        add(borrowPanel);
        add(returnPanel);
    }

    // ================= CLEAN CARD (FIXED SIZE LOOK) =================
    private JPanel createCard(String titleText) {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 8, 8));
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(30, 30, 30));

        panel.add(title);

        return panel;
    }

    private JTextField styledField(String placeholder) {

        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        field.setPreferredSize(new Dimension(180, 28));
        field.setMaximumSize(new Dimension(180, 28));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        return field;
    }

    private JLabel label(String text) {

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(90, 90, 90));

        return lbl;
    }

    private JButton styledButton(String text, Color color) {

        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 32));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }
}