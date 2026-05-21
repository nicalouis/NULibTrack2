package LibrarianPanel;

import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class LibrarianHomePanel extends JPanel {

    public LibrarianHomePanel() {

        setLayout(new GridLayout(1, 3, 20, 20));
        setBackground(AppColor.BACKGROUND);

        add(card("Books", "120", AppColor.INFO));
        add(card("Users", "45", AppColor.SUCCESS));
        add(card("Fines", "₱1,250", AppColor.DANGER));
    }

    private JPanel card(String title, String value, Color color) {

        JPanel c = new JPanel();
        c.setLayout(new BorderLayout());
        c.setBackground(AppColor.CARD);
        c.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setForeground(color);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 30));

        c.add(t, BorderLayout.NORTH);
        c.add(v, BorderLayout.CENTER);

        return c;
    }
}