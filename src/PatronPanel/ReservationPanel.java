package PatronPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;

import javax.swing.*;
import java.awt.*;

public class ReservationPanel extends JPanel {

    private JPanel list;

    public ReservationPanel() {

        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);

        // ================= HEADER =================
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppColor.BACKGROUND);

        JLabel icon = new JLabel(loadIcon("src/reservation.png", 40, 40));

        JLabel title = new JLabel("Reservations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        header.add(icon);
        header.add(title);

        add(header, BorderLayout.NORTH);

        // ================= LIST =================
        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(AppColor.BACKGROUND);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppColor.BACKGROUND);

        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    // ================= REFRESH =================
    public void refresh() {

        list.removeAll();

        for (Book b : LibraryDB.get().reservations) {
            list.add(card(b));
            list.add(Box.createVerticalStrut(12));
        }

        list.revalidate();
        list.repaint();
    }

    // ================= UPDATED CARD UI =================
    private JPanel card(Book b) {

        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ================= IMAGE =================
        JLabel img = new JLabel(loadIcon(b.image, 90, 110));

        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(Color.WHITE);
        imgPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 15));
        imgPanel.add(img);

        // ================= TEXT =================
        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);

        JLabel title = new JLabel(b.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel author = new JLabel("by " + b.author);
        author.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        author.setForeground(Color.DARK_GRAY);

        JLabel date = new JLabel(
                "Reserved: " + (b.reserveDate != null ? b.reserveDate : "N/A")
        );
        date.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        date.setForeground(AppColor.INFO);

        text.add(title);
        text.add(Box.createVerticalStrut(5));
        text.add(author);
        text.add(Box.createVerticalStrut(8));
        text.add(date);

        // ================= BUTTON =================
        JButton cancel = new JButton("CANCEL");
        cancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancel.setBackground(AppColor.DANGER);
        cancel.setForeground(Color.WHITE);
        cancel.setFocusPainted(false);

        cancel.setPreferredSize(new Dimension(100, 35));

        cancel.addActionListener(e -> {
            LibraryDB.get().cancelReserve(b);
            refresh();
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(cancel);

        // ================= CARD FINAL =================
        card.add(imgPanel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.EAST);

        return card;
    }

    // ================= IMAGE LOADER =================
    private ImageIcon loadIcon(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}