package LibrarianPanel;

import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class FinePanel extends JPanel {

    public FinePanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        JLabel title = new JLabel("Student Fines Overview");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JTextArea area = new JTextArea(
                "Juan Dela Cruz - ₱150 (OVERDUE)\n" +
                "Maria Santos - ₱0 (CLEARED)\n" +
                "Pedro Reyes - ₱50 (DUE SOON)"
        );

        area.setFont(new Font("Arial", Font.PLAIN, 18));

        JScrollPane scroll = new JScrollPane(area);

        add(title, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }
}