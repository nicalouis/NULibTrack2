package LoginUI;

import Services.authServ;
import PatronPanel.PatronDashboard;
import LibrarianPanel.LibrarianDashboard;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    JTextField email;
    JPasswordField pass;
    JComboBox<String> role;

    public LoginFrame() {

        setTitle("NU LibTrack Login");
        setSize(520, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ================= BACKGROUND =================
        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(AppColor.PRIMARY);

        // ================= LOGIN CARD =================
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 460));
        card.setBackground(AppColor.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // ================= LOGO =================
        ImageIcon icon = new ImageIcon("src/logo.png");
        Image img = icon.getImage().getScaledInstance(85, 85, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= TITLE =================
        JLabel title = new JLabel("NU LIBTRACK");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.BLACK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Library Management System");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(Color.GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= INPUTS =================
        email = createField("Email");
        pass = createPassField("Password");

        role = new JComboBox<>(new String[]{"Patron", "Librarian"});
        role.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        role.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // ================= LOGIN BUTTON =================
        JButton login = new JButton("LOGIN");
        login.setBackground(AppColor.SECONDARY);
        login.setForeground(Color.BLACK);
        login.setFocusPainted(false);
        login.setFont(new Font("Segoe UI", Font.BOLD, 14));
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        login.addActionListener(e -> loginUser());

        // ================= SPACING =================
        card.add(logo);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(sub);
        card.add(Box.createVerticalStrut(20));

        card.add(email);
        card.add(Box.createVerticalStrut(10));

        card.add(pass);
        card.add(Box.createVerticalStrut(10));

        card.add(role);
        card.add(Box.createVerticalStrut(20));

        card.add(login);

        bg.add(card);
        add(bg);
    }

    // ================= TEXT FIELD STYLE =================
    private JTextField createField(String placeholder) {

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        return field;
    }

    private JPasswordField createPassField(String placeholder) {

        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        return field;
    }

    // ================= LOGIN =================
    private void loginUser() {

        authServ auth = new authServ();

        String em = email.getText();
        String pw = String.valueOf(pass.getPassword());
        String r = role.getSelectedItem().toString();

        if (r.equals("Patron") && auth.loginPatron(em, pw)) {
            new PatronDashboard().setVisible(true);
            dispose();
        } else if (r.equals("Librarian") && auth.loginLibrarian(em, pw)) {
            new LibrarianDashboard().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
        }
    }
}