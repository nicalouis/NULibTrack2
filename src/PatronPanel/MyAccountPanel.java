package PatronPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class MyAccountPanel extends JPanel {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField courseField;
    private JTextField yearField;

    private JLabel borrowedCount;
    private JLabel reservedCount;
    private JLabel lastBorrowLabel;

    private JLabel profilePic;

    public MyAccountPanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        // ================= HEADER =================
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);

        JLabel title = new JLabel("My Account Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        header.add(title);
        add(header, BorderLayout.NORTH);

        // ================= MAIN WRAPPER =================
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppColor.BACKGROUND);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // ================= LEFT (PROFILE IMAGE) =================
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(AppColor.BACKGROUND);

        profilePic = new JLabel(loadIcon("src/profile.png", 140, 140));
        leftPanel.add(profilePic);

        // ================= MAIN CARD =================
        JPanel mainCard = new JPanel();
        mainCard.setLayout(new BoxLayout(mainCard, BoxLayout.Y_AXIS));
        mainCard.setBackground(AppColor.BACKGROUND);

        // ================= ACCOUNT CARD =================
        JPanel accountCard = createSectionCard("Account Information");

        nameField = createInputRow(accountCard, "Full Name", "Juan Dela Cruz");
        emailField = createInputRow(accountCard, "Email", "juan@gmail.com");
        courseField = createInputRow(accountCard, "Course", "BSIT");
        yearField = createInputRow(accountCard, "Year Level", "First Year");

        // ================= LIBRARY CARD =================
        JPanel libraryCard = createSectionCard("Library Summary");

        borrowedCount = createStatRow(libraryCard, "Borrowed Books");
        reservedCount = createStatRow(libraryCard, "Reservations");
        lastBorrowLabel = createStatRow(libraryCard, "Last Borrowed");

        // ================= SAVE BUTTON =================
        JButton saveBtn = new JButton("SAVE CHANGES");
        saveBtn.setBackground(AppColor.SECONDARY);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setFocusPainted(false);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(AppColor.BACKGROUND);
        btnPanel.add(saveBtn);

        saveBtn.addActionListener(e -> saveProfile());

        // ================= BUILD CENTER =================
        mainCard.add(accountCard);
        mainCard.add(Box.createVerticalStrut(15));
        mainCard.add(libraryCard);
        mainCard.add(Box.createVerticalStrut(20));
        mainCard.add(btnPanel);

        wrapper.add(leftPanel, BorderLayout.WEST);
        wrapper.add(mainCard, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);

        updateInfo();
    }

    // ================= SECTION CARD =================
    private JPanel createSectionCard(String titleText) {

        JPanel card = new JPanel();
        card.setLayout(new GridLayout(0, 1, 8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(AppColor.PRIMARY);

        card.add(title);

        return card;
    }

    // ================= INPUT ROW =================
    private JTextField createInputRow(JPanel panel, String label, String value) {

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTextField field = new JTextField(value);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        panel.add(lbl);
        panel.add(field);

        return field;
    }

    // ================= STAT ROW =================
    private JLabel createStatRow(JPanel panel, String label) {

        JLabel lbl = new JLabel(label + ": ");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel value = new JLabel();
        value.setFont(new Font("Segoe UI", Font.BOLD, 13));
        value.setForeground(AppColor.INFO);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(Color.WHITE);

        row.add(lbl);
        row.add(value);

        panel.add(row);

        return value;
    }

    // ================= UPDATE INFO =================
    public void refresh() {
        updateInfo();
        revalidate();
        repaint();
    }

    private void updateInfo() {

        LibraryDB db = LibraryDB.get();

        borrowedCount.setText(String.valueOf(db.borrowed.size()));
        reservedCount.setText(String.valueOf(db.reservations.size()));

        String last = "None";

        if (!db.borrowed.isEmpty()) {
            Book b = db.borrowed.get(db.borrowed.size() - 1);
            last = b.title + " (" + b.borrowDate + ")";
        }

        lastBorrowLabel.setText(last);
    }

    // ================= SAVE =================
    private void saveProfile() {

        JOptionPane.showMessageDialog(this,
                "Profile Updated!\n\n" +
                        "Name: " + nameField.getText() + "\n" +
                        "Email: " + emailField.getText() + "\n" +
                        "Course: " + courseField.getText() + "\n" +
                        "Year: " + yearField.getText()
        );
    }

    // ================= IMAGE LOADER =================
    private ImageIcon loadIcon(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}