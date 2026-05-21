package LibrarianPanel;

import javax.swing.*;
import java.awt.*;

public class BorrowReturnPanel extends JPanel {

    public BorrowReturnPanel(){

        setLayout(new GridLayout(1,2,20,20));

        JPanel borrowPanel = new JPanel();
        borrowPanel.setBorder(BorderFactory.createTitledBorder("Issue Book"));

        JPanel returnPanel = new JPanel();
        returnPanel.setBorder(BorderFactory.createTitledBorder("Return Book"));

        borrowPanel.add(new JLabel("Student ID"));
        borrowPanel.add(new JTextField(15));
        borrowPanel.add(new JButton("Issue"));

        returnPanel.add(new JLabel("Book ID"));
        returnPanel.add(new JTextField(15));
        returnPanel.add(new JButton("Return"));

        add(borrowPanel);
        add(returnPanel);
    }
}