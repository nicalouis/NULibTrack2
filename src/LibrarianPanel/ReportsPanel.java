package LibrarianPanel;

import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {

    public ReportsPanel(){

        setLayout(new GridLayout(2,2,20,20));

        add(createCard("Total Books", "1250"));
        add(createCard("Active Members", "540"));
        add(createCard("Overdue Books", "22"));
        add(createCard("Reservations", "5"));
    }

    private JPanel createCard(String title, String value){

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setLayout(new GridLayout(2,1));

        JLabel t = new JLabel(title,SwingConstants.CENTER);
        JLabel v = new JLabel(value,SwingConstants.CENTER);

        t.setFont(new Font("Arial", Font.BOLD, 20));
        v.setFont(new Font("Arial", Font.BOLD, 35));

        panel.add(t);
        panel.add(v);

        return panel;
    }
}