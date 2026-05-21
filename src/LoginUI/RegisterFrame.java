package LoginUI;

import LibrarianPanel.LibrarianDashboard;
import PatronPanel.PatronDashboard;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public RegisterFrame() {

        setTitle("NU LibTrack Register");
        setSize(520, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(AppColor.PRIMARY);

        // ================= CARD =================
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(390, 540));
        card.setBackground(AppColor.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // ================= HEADER =================
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Join NU LibTrack System");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(Color.GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= INPUTS =================
        fullNameField = field("Full Name");
        emailField = field("Email");
        passwordField = passField("Password");

        roleBox = new JComboBox<>(new String[]{"Patron", "Librarian"});
        roleBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // ================= BUTTONS =================
        JButton registerBtn = new JButton("REGISTER");
        registerBtn.setBackground(AppColor.SECONDARY);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setFocusPainted(false);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backBtn = new JButton("Back to Login");
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerBtn.addActionListener(e -> registerUser());

        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        // ================= BUILD =================
        card.add(title);
        card.add(sub);
        card.add(Box.createVerticalStrut(25));

        card.add(fullNameField);
        card.add(Box.createVerticalStrut(12));

        card.add(emailField);
        card.add(Box.createVerticalStrut(12));

        card.add(passwordField);
        card.add(Box.createVerticalStrut(12));

        card.add(roleBox);
        card.add(Box.createVerticalStrut(25));

        card.add(registerBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(backBtn);

        bg.add(card);
        add(bg);
    }

    // ================= FIELD STYLE =================
    private JTextField field(String text) {

        JTextField f = new JTextField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setBorder(BorderFactory.createTitledBorder(text));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        return f;
    }

    private JPasswordField passField(String text) {

        JPasswordField f = new JPasswordField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setBorder(BorderFactory.createTitledBorder(text));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        return f;
    }

    private void registerUser() {

        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = String.valueOf(passwordField.getPassword());
        String role = roleBox.getSelectedItem().toString();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Registration Successful!");

        dispose();

        if (role.equals("Librarian")) {
            new LibrarianDashboard().setVisible(true);
        } else {
            new PatronDashboard().setVisible(true);
        }
    }
}