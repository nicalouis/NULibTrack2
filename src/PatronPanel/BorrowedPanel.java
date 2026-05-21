package PatronPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class BorrowedPanel extends JPanel {

    private JPanel list;

    public BorrowedPanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        // ================= HEADER =================
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);

        JLabel icon = new JLabel(loadIcon("src/borrow.jpg", 40, 40));

        JLabel title = new JLabel("Borrowed Books");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        header.add(icon);
        header.add(title);

        add(header, BorderLayout.NORTH);

        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(AppColor.BACKGROUND);

        add(new JScrollPane(list), BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {

        list.removeAll();

        for (Book b : LibraryDB.get().borrowed) {
            list.add(card(b));
            list.add(Box.createVerticalStrut(12));
        }

        list.revalidate();
        list.repaint();
    }

    // ================= CARD (ENHANCED UI) =================
    private JPanel card(Book b) {

        JPanel p = new JPanel(new BorderLayout(15, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // IMAGE
        JLabel img = new JLabel(loadIcon(b.image, 80, 100));

        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(Color.WHITE);
        imgPanel.add(img);

        // TEXT
        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);

        JLabel title = new JLabel(b.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel dateInfo = new JLabel(
                (b.borrowDate != null)
                        ? "Borrowed: " + b.borrowDate + " | Due: " + b.dueDate
                        : "No date info"
        );

        dateInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateInfo.setForeground(Color.GRAY);

        text.add(title);
        text.add(Box.createVerticalStrut(5));
        text.add(dateInfo);

        // BUTTON
        JButton returnBtn = new JButton("RETURN");
        returnBtn.setBackground(AppColor.SECONDARY);
        returnBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        returnBtn.addActionListener(e -> {
            LibraryDB.get().returnBook(b);
            refresh();
        });

        p.add(imgPanel, BorderLayout.WEST);
        p.add(text, BorderLayout.CENTER);
        p.add(returnBtn, BorderLayout.EAST);

        return p;
    }

    private ImageIcon loadIcon(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}