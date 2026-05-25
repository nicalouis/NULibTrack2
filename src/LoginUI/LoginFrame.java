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

        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(AppColor.PRIMARY);

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

        JLabel title = new JLabel("NU LIBTRACK");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Library Management System");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(Color.GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= FIELDS =================
        email = createField("Email");
        pass = createPassField("Password");

        role = new JComboBox<>(new String[]{"Patron", "Librarian"});
        role.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        role.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= LOGIN BUTTON (FIXED CENTER) =================
        JButton login = new JButton("LOGIN");
        login.setBackground(AppColor.SECONDARY);
        login.setForeground(Color.BLACK);
        login.setFocusPainted(false);
        login.setPreferredSize(new Dimension(120, 35));
        login.setMaximumSize(new Dimension(120, 35));
        login.setAlignmentX(Component.CENTER_ALIGNMENT); // ✔ KEY FIX

        login.addActionListener(e -> loginUser());

        // ================= ADD COMPONENTS =================
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
        card.add(Box.createVerticalStrut(25));

        // ✔ CENTER WRAPPER (extra fix for perfect centering)
        JPanel btnWrapper = new JPanel();
        btnWrapper.setOpaque(false);
        btnWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnWrapper.add(login);

        card.add(btnWrapper);

        bg.add(card);
        add(bg);
    }

    private JTextField createField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        return field;
    }

    private JPasswordField createPassField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        return field;
    }

    // ================= LOGIN =================
    private void loginUser() {

        authServ auth = new authServ();

        String em = email.getText().trim();
        String pw = String.valueOf(pass.getPassword()).trim();

        String selectedRole = (role.getSelectedItem() != null)
                ? role.getSelectedItem().toString()
                : "";

        if (selectedRole.equals("Patron")) {

            if (auth.loginPatron(em, pw)) {

                new PatronDashboard().setVisible(true);
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Patron credentials!");
            }
        }

        else if (selectedRole.equals("Librarian")) {

            if (auth.loginLibrarian(em, pw)) {

                new LibrarianDashboard().setVisible(true);
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Librarian credentials!");
            }
        }
    }
}