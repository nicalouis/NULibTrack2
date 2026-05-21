package LibrarianPanel;

import javax.swing.*;
import java.awt.*;

public class NotificationPanel extends JPanel {

    public NotificationPanel(){

        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();

        area.append("2 Books Due Tomorrow\n");
        area.append("Reservation Pending\n");
        area.append("Overdue Fine Alert\n");

        add(new JScrollPane(area), BorderLayout.CENTER);
    }
}