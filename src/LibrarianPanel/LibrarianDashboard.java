package LibrarianPanel;


import Dashboard.DashboardLayout;
import LoginUI.LoginFrame;
import Services.LibraryDB;
import UIUtils.SidebarButtons;
import java.awt.*;
import javax.swing.*;


public class LibrarianDashboard extends DashboardLayout {


    private FinePanel finePanel;
    private FineApprovalPanel fineApprovalPanel;


    private BooksPanel booksPanel;
    private BorrowReturnPanel borrowPanel;
    private ReportsPanel reportsPanel;
    private NotificationPanel notificationPanel;
    private MyAccountPanel accountPanel;   //  account panel


    // ================= ADDED FOR LIVE UPDATES =================
    private JPanel dashboardPanel;
    private JLabel booksValue;
    private JLabel borrowedValue;
    private JLabel reservationsValue;
    private JLabel pendingPaymentsValue;


    public LibrarianDashboard() {
        super();


        // ================= SAFE RESET SIDEBAR =================
        sidebarBox.removeAll();
        sidebarBox.setLayout(new BoxLayout(sidebarBox, BoxLayout.Y_AXIS));
        sidebarBox.setBackground(new Color(18, 30, 60));
        sidebarBox.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));


        // ================= TITLE =================
        JLabel menu = new JLabel("LIBRARIAN PANEL");
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);


        sidebarBox.add(menu);
        sidebarBox.add(Box.createVerticalStrut(15));


        // ================= BUTTONS =================
        SidebarButtons dashboardBtn = new SidebarButtons("Dashboard");
        SidebarButtons booksBtn = new SidebarButtons("Books");
        SidebarButtons borrowBtn = new SidebarButtons("Borrow / Return");
        SidebarButtons reportsBtn = new SidebarButtons("Reports");
        SidebarButtons notifBtn = new SidebarButtons("Notifications");
        SidebarButtons finesBtn = new SidebarButtons("Fines Overview");
        SidebarButtons paymentBtn = new SidebarButtons("Payments");
        SidebarButtons accountBtn = new SidebarButtons("My Account");
        SidebarButtons logoutBtn = new SidebarButtons("Logout");


        SidebarButtons[] buttons = {
                dashboardBtn,
                booksBtn,
                borrowBtn,
                reportsBtn,
                notifBtn,
                finesBtn,
                paymentBtn,
                accountBtn
        };


        for (SidebarButtons b : buttons) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(210, 40));
            b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            sidebarBox.add(b);
            sidebarBox.add(Box.createVerticalStrut(8));
        }


        // ================= PUSH LOGOUT DOWN =================
        sidebarBox.add(Box.createVerticalGlue());


        // ================= LOGOUT =================
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(210, 40));
        logoutBtn.setBackground(new Color(220, 38, 38));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setOpaque(true);
        logoutBtn.setContentAreaFilled(true);


        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                logoutBtn.setBackground(new Color(185, 28, 28));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                logoutBtn.setBackground(new Color(220, 38, 38));
            }
        });


        sidebarBox.add(logoutBtn);


        // ================= DASHBOARD =================
        dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(new Color(245, 247, 250));


        JLabel title = new JLabel("Librarian Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 10));
        dashboardPanel.add(title, BorderLayout.NORTH);


        JPanel cards = new JPanel(new GridLayout(2, 2, 20, 20));
        cards.setBackground(new Color(245, 247, 250));
        cards.setBorder(BorderFactory.createEmptyBorder(20, 25, 25, 25));


        booksValue = new JLabel();
        borrowedValue = new JLabel();
        reservationsValue = new JLabel();
        pendingPaymentsValue = new JLabel();


        cards.add(createCard("Total Books",
                booksValue,
                new Color(59, 130, 246)));


        cards.add(createCard("Borrowed Books",
                borrowedValue,
                new Color(245, 158, 11)));


        cards.add(createCard("Reservations",
                reservationsValue,
                new Color(16, 185, 129)));


        cards.add(createCard("Pending Payments",
                pendingPaymentsValue,
                new Color(239, 68, 68)));


        dashboardPanel.add(cards, BorderLayout.CENTER);


        // ================= PANELS =================
        booksPanel = new BooksPanel();
        borrowPanel = new BorrowReturnPanel();
        reportsPanel = new ReportsPanel();
        notificationPanel = new NotificationPanel();
        finePanel = new FinePanel();
        fineApprovalPanel = new FineApprovalPanel();
        accountPanel = new MyAccountPanel();   //  instantiate account panel


        // ================= REGISTER =================
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(booksPanel, "books");
        contentPanel.add(borrowPanel, "borrow");
        contentPanel.add(reportsPanel, "reports");
        contentPanel.add(notificationPanel, "notif");
        contentPanel.add(finePanel, "fines");
        contentPanel.add(fineApprovalPanel, "payments");
        contentPanel.add(accountPanel, "account");   //  register account panel


        // ================= ACTIONS =================
        dashboardBtn.addActionListener(e -> {
            open("dashboard");
            refreshDashboard(); // LIVE UPDATE
        });

        booksBtn.addActionListener(e -> open("books"));
        borrowBtn.addActionListener(e -> open("borrow"));
        reportsBtn.addActionListener(e -> open("reports"));
        notifBtn.addActionListener(e -> open("notif"));
        finesBtn.addActionListener(e -> open("fines"));
        paymentBtn.addActionListener(e -> open("payments"));

        accountBtn.addActionListener(e -> {
            open("account");
            accountPanel.refresh();
        });


        logoutBtn.addActionListener(e -> logout());


        open("dashboard");
        refreshDashboard(); // INITIAL LOAD
    }


    // ================= LIVE UPDATE METHOD (ADDED ONLY) =================
    public void refreshDashboard() {

        LibraryDB db = LibraryDB.get();

        if (booksValue != null)
            booksValue.setText(String.valueOf(db.books.size()));

        if (borrowedValue != null)
            borrowedValue.setText(String.valueOf(db.borrowed.size()));

        if (reservationsValue != null)
            reservationsValue.setText(String.valueOf(db.reservations.size()));

        if (pendingPaymentsValue != null) {

            int pending = 0;

            for (LibraryDB.Book b : db.borrowed) {
                if (b != null && b.paymentRequested && !b.paymentConfirmed) {
                    pending++;
                }
            }

            pendingPaymentsValue.setText(String.valueOf(pending));
        }

        revalidate();
        repaint();
    }


    // ================= OPEN =================
    private void open(String name) {
        switchPanel(name);


        SwingUtilities.invokeLater(() -> {

            if ("dashboard".equals(name)) {
                refreshDashboard(); // LIVE REFRESH WHEN OPENING
            }

            if ("fines".equals(name) && finePanel != null)
                finePanel.load();


            if ("payments".equals(name) && fineApprovalPanel != null)
                fineApprovalPanel.load();


            contentPanel.revalidate();
            contentPanel.repaint();
        });
    }


    // ================= CARD =================
    private JPanel createCard(String title, JLabel valueLbl, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);


        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));


        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLbl.setForeground(Color.GRAY);


        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLbl.setForeground(color);


        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLbl, BorderLayout.CENTER);


        return card;
    }


    // ================= LOGOUT =================
    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}