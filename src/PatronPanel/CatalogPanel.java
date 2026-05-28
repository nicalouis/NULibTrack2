package PatronPanel;


import Services.LibraryDB;
import Services.LibraryDB.Book;
import UIUtils.AppColor;
import UIUtils.DatePicker;


import javax.swing.*;
import java.awt.*;


public class CatalogPanel extends JPanel {


    private JPanel list;
    private JTextField searchField;


    public static CatalogPanel instance;


    public CatalogPanel() {


        instance = this;


        setLayout(new BorderLayout());
        setBackground(AppColor.BACKGROUND);


        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        header.setBackground(AppColor.BACKGROUND);
        header.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));


        JLabel icon = new JLabel(loadIcon("src/search.jpg", 40, 40));


        JLabel title = new JLabel("Book Catalog");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(30, 41, 59));


        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(220, 36));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));


        JButton searchBtn = new JButton("SEARCH");
        searchBtn.setBackground(AppColor.SECONDARY);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));


        header.add(icon);
        header.add(title);
        header.add(Box.createHorizontalStrut(10));
        header.add(searchField);
        header.add(searchBtn);


        add(header, BorderLayout.NORTH);


        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(AppColor.BACKGROUND);
        list.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));


        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppColor.BACKGROUND);


        add(scroll, BorderLayout.CENTER);


        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                load(searchField.getText());
            }
        });


        searchBtn.addActionListener(e -> load(searchField.getText()));


        load("");
    }


    public void load(String keyword) {


        list.removeAll();


        LibraryDB db = LibraryDB.get();


        for (Book b : db.books) addIfVisible(b, keyword);
        for (Book b : db.getBorrowedBooks()) addIfVisible(b, keyword);


        list.revalidate();
        list.repaint();
    }


    private void addIfVisible(Book b, String keyword) {


        if (b == null) return;


        String k = (keyword == null) ? "" : keyword.toLowerCase();


        boolean match =
                k.isEmpty()
                        || (b.title != null && b.title.toLowerCase().contains(k))
                        || (b.author != null && b.author.toLowerCase().contains(k));


        if (match) {
            list.add(card(b));
            list.add(Box.createVerticalStrut(14));
        }
    }


    private JPanel card(Book b) {


        JPanel card = new JPanel(new BorderLayout(18, 10));


        card.setBackground(Color.WHITE);


        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(230, 230, 230),
                        1,
                        true
                ),
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        ));


        JLabel img = new JLabel(loadIcon(b.image, 100, 130));


        JPanel imgPanel = new JPanel(new BorderLayout());


        imgPanel.setBackground(Color.WHITE);
        imgPanel.setPreferredSize(new Dimension(110, 140));


        imgPanel.add(img, BorderLayout.CENTER);


        JLabel title = new JLabel(b.title);


        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(new Color(15, 23, 42));


        JLabel author = new JLabel("by " + b.author);


        author.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        author.setForeground(new Color(100, 100, 100));


        JLabel dateInfo = new JLabel();


        dateInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));


        if (b.borrowed) {


            dateInfo.setText("Not Available • Was Borrwoed on: " + b.dueDate);
            dateInfo.setForeground(new Color(220, 38, 38));


        } else if (b.reserveDate != null) {


            dateInfo.setText("Reserved • " + b.reserveDate);
            dateInfo.setForeground(new Color(37, 99, 235));


        } else if (b.reserveDate != null) {


        int position = 1;


        for (Book r : LibraryDB.get().reservations) {
                if (r.title.equals(b.title)) {
                if (r == b) break;
                position++;
                }
        }


        if (b.available) {
                dateInfo.setText("Reserved • " + b.reserveDate);
        } else {
                dateInfo.setText("Queued (#" + position + ")");
        }


        dateInfo.setForeground(new Color(37, 99, 235));
        }else {


            dateInfo.setText("Available");
            dateInfo.setForeground(new Color(22, 163, 74));
        }


        JPanel text = new JPanel();


        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);


        text.add(Box.createVerticalStrut(8));
        text.add(title);
        text.add(Box.createVerticalStrut(5));
        text.add(author);
        text.add(Box.createVerticalStrut(14));
        text.add(dateInfo);


        JButton borrow = new JButton("BORROW");
        JButton reserve = new JButton("RESERVE");


        styleButton(borrow, AppColor.SECONDARY);
        styleButton(reserve, AppColor.INFO);


        if (!b.available || b.borrowed) {


        borrow.setEnabled(false);
        borrow.setBackground(Color.LIGHT_GRAY);


        } else {


        borrow.setEnabled(true);
        styleButton(borrow, AppColor.SECONDARY);
        }


        // ✅ RESERVE ALWAYS ENABLED
        reserve.setEnabled(true);
        styleButton(reserve, AppColor.INFO);


        // ================= BORROW =================


        borrow.addActionListener(e -> {


            String result = LibraryDB.get().borrow(b);


            if (result.equals("LIMIT")) {


                JOptionPane.showMessageDialog(
                        this,
                        createWarningPanel(
                                "Borrow Limit Reached",
                                "You can only borrow 3 books at a time."
                        ),
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );


                return;
            }


            if (result.equals("UNAVAILABLE")) {


                JOptionPane.showMessageDialog(
                        this,
                        createWarningPanel(
                                "Book Unavailable",
                                "This book is currently not available."
                        ),
                        "Borrow Failed",
                        JOptionPane.WARNING_MESSAGE
                );


                return;
            }


            JPanel popup = createSuccessPopup(
                    "BOOK BORROWED",
                    "Transaction Successful",
                    b.title,
                    b.author,
                    "DUE DATE",
                    b.dueDate,
                    new Color(239, 246, 255),
                    new Color(29, 78, 216),
                    new Color(220, 38, 38),
                    b.image
            );


            JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this),
                    popup,
                    "Borrow Success",
                    JOptionPane.PLAIN_MESSAGE
            );


            refresh();
        });


        // ================= RESERVE =================


        reserve.addActionListener(e -> {


        String result = LibraryDB.get().reserve(b);
        boolean isNextInLine = result.equals("NEXT");


        if (result.equals("LIMIT")) {


        JOptionPane.showMessageDialog(
                this,
                createWarningPanel(
                        "Reserve Limit Reached",
                        "You can only reserve 3 books."
                ),
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }


    if (result.equals("ALREADY")) {


        JOptionPane.showMessageDialog(
                this,
                createWarningPanel(
                        "Already Reserved",
                        "You are already in the queue."
                ),
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }


    JPanel popup;


    if (result.equals("NO_QUEUE")) {


        String selectedDate = DatePicker.pickDate(this);
        if (selectedDate == null) return;


        b.reserveDate = selectedDate;


        popup = createSuccessPopup(
                "BOOK RESERVED",
                "Reservation Confirmed",
                b.title,
                b.author,
                "RESERVED DATE",
                b.reserveDate,
                new Color(236, 253, 245),
                new Color(21, 128, 61),
                new Color(22, 163, 74),
                b.image
        );


         } else {
                popup = createSuccessPopup(
                "BOOK RESERVED",
                isNextInLine ? "You are NEXT in line" : "Added to Queue",
                b.title,
                b.author,
                isNextInLine ? "STATUS" : "QUEUE NUMBER",
                isNextInLine ? "Next in Line" : "#" + result,
                new Color(236, 253, 245),
                new Color(21, 128, 61),
                new Color(22, 163, 74),
                b.image
        );
        }


        JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                popup,
                "Reserve Success",
                JOptionPane.PLAIN_MESSAGE
        );


        refresh();
        });
        JPanel buttons = new JPanel();


        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBackground(Color.WHITE);


        buttons.add(Box.createVerticalStrut(12));
        buttons.add(borrow);
        buttons.add(Box.createVerticalStrut(10));
        buttons.add(reserve);


        card.add(imgPanel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.EAST);


        return card;
    }


    // ================= SUCCESS POPUP =================


    private JPanel createSuccessPopup(
            String title,
            String subtitle,
            String bookTitle,
            String author,
            String label,
            String value,
            Color boxBg,
            Color titleColor,
            Color valueColor,
            String imagePath
    ) {


        JPanel main = new JPanel(new BorderLayout(18, 0));


        main.setBackground(Color.WHITE);


        main.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 220, 220),
                        1,
                        true
                ),
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        ));


        // LEFT IMAGE


        JLabel bookImage = new JLabel(loadIcon(imagePath, 120, 160));


        JPanel imagePanel = new JPanel(new BorderLayout());


        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(130, 170));


        imagePanel.add(bookImage, BorderLayout.CENTER);


        // RIGHT CONTENT


        JPanel content = new JPanel();


        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);


        JLabel icon = new JLabel("✓");


        icon.setFont(new Font("Segoe UI", Font.BOLD, 30));
        icon.setForeground(titleColor);
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel popupTitle = new JLabel(title);


        popupTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        popupTitle.setForeground(titleColor);
        popupTitle.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel sub = new JLabel(subtitle);


        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(110, 110, 110));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);


        JPanel infoCard = new JPanel();


        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setBackground(new Color(249, 250, 251));


        infoCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(230, 230, 230),
                        1,
                        true
                ),
                BorderFactory.createEmptyBorder(
                        14,
                        16,
                        14,
                        16
                )
        ));


        JLabel bookLbl = new JLabel("BOOK TITLE");


        bookLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        bookLbl.setForeground(new Color(120, 120, 120));


        JLabel bookTxt = new JLabel(bookTitle);


        bookTxt.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bookTxt.setForeground(new Color(17, 24, 39));


        JLabel authorLbl = new JLabel("AUTHOR");


        authorLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        authorLbl.setForeground(new Color(120, 120, 120));


        JLabel authorTxt = new JLabel(author);


        authorTxt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        authorTxt.setForeground(new Color(55, 65, 81));


        infoCard.add(bookLbl);
        infoCard.add(Box.createVerticalStrut(5));
        infoCard.add(bookTxt);
        infoCard.add(Box.createVerticalStrut(14));
        infoCard.add(authorLbl);
        infoCard.add(Box.createVerticalStrut(5));
        infoCard.add(authorTxt);


        JPanel highlight = new JPanel();


        highlight.setLayout(new BoxLayout(highlight, BoxLayout.Y_AXIS));
        highlight.setBackground(boxBg);


        highlight.setBorder(BorderFactory.createEmptyBorder(
                12,
                16,
                12,
                16
        ));


        JLabel hlTitle = new JLabel(label);


        hlTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        hlTitle.setForeground(new Color(90, 90, 90));
        hlTitle.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel hlValue = new JLabel(value);


        hlValue.setFont(new Font("Segoe UI", Font.BOLD, 19));
        hlValue.setForeground(valueColor);
        hlValue.setAlignmentX(Component.LEFT_ALIGNMENT);


        highlight.add(hlTitle);
        highlight.add(Box.createVerticalStrut(5));
        highlight.add(hlValue);


        JLabel footer = new JLabel(
                "Please return books on time to avoid penalties."
        );


        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(120, 120, 120));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);


        content.add(icon);
        content.add(Box.createVerticalStrut(5));
        content.add(popupTitle);
        content.add(Box.createVerticalStrut(4));
        content.add(sub);
        content.add(Box.createVerticalStrut(16));
        content.add(infoCard);
        content.add(Box.createVerticalStrut(14));
        content.add(highlight);
        content.add(Box.createVerticalStrut(14));
        content.add(footer);


        main.add(imagePanel, BorderLayout.WEST);
        main.add(content, BorderLayout.CENTER);


        return main;
    }


    // ================= WARNING PANEL =================


    private JPanel createWarningPanel(String title, String msg) {


        JPanel panel = new JPanel();


        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);


        panel.setBorder(BorderFactory.createEmptyBorder(
                18,
                20,
                18,
                20
        ));


        JLabel icon = new JLabel("⚠");


        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel t = new JLabel(title);


        t.setFont(new Font("Segoe UI", Font.BOLD, 18));
        t.setForeground(new Color(180, 83, 9));
        t.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel m = new JLabel(msg);


        m.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        m.setForeground(new Color(90, 90, 90));
        m.setAlignmentX(Component.CENTER_ALIGNMENT);


        panel.add(icon);
        panel.add(Box.createVerticalStrut(10));
        panel.add(t);
        panel.add(Box.createVerticalStrut(8));
        panel.add(m);


        return panel;
    }


    // ================= BUTTON STYLE =================


    private void styleButton(JButton btn, Color color) {


        btn.setBackground(color);
        btn.setForeground(Color.WHITE);


        btn.setFocusPainted(false);


        btn.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                12
        ));


        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));


        btn.setPreferredSize(new Dimension(110, 36));
    }


    public void refresh() {
        load(searchField.getText());
    }


    public static void forceRefresh() {


        if (instance != null) {
            instance.refresh();
        }
    }


    private ImageIcon loadIcon(String path, int w, int h) {


        ImageIcon icon = new ImageIcon(path);


        Image img = icon.getImage();


        Image scaled = img.getScaledInstance(
                w,
                h,
                Image.SCALE_SMOOTH
        );


        return new ImageIcon(scaled);
    }
}
