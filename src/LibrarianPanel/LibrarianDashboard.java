package LibrarianPanel;

import Dashboard.DashboardLayout;
import LoginUI.LoginFrame;
import UIUtils.SidebarButtons;
import Services.LibraryDB;

import javax.swing.*;
import java.awt.*;

public class LibrarianDashboard extends DashboardLayout {

    private FinePanel finePanel;
    private FineApprovalPanel fineApprovalPanel;

    private BooksPanel booksPanel;
    private BorrowReturnPanel borrowPanel;
    private ReportsPanel reportsPanel;
    private NotificationPanel notificationPanel;

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

        // keep red on hover
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
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Librarian Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 10));

        dashboardPanel.add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 2, 20, 20));
        cards.setBackground(new Color(245, 247, 250));
        cards.setBorder(BorderFactory.createEmptyBorder(20, 25, 25, 25));

        cards.add(createCard("Total Books",
                String.valueOf(LibraryDB.get().books.size()),
                new Color(59, 130, 246)));

        cards.add(createCard("Borrowed Books",
                String.valueOf(LibraryDB.get().borrowed.size()),
                new Color(245, 158, 11)));

        cards.add(createCard("Reservations",
                String.valueOf(LibraryDB.get().reservations.size()),
                new Color(16, 185, 129)));

        int pending = 0;
        for (LibraryDB.Book b : LibraryDB.get().borrowed) {
            if (b.paymentRequested && !b.paymentConfirmed) pending++;
        }

        cards.add(createCard("Pending Payments",
                String.valueOf(pending),
                new Color(239, 68, 68)));

        dashboardPanel.add(cards, BorderLayout.CENTER);

        // ================= PANELS =================
        booksPanel = new BooksPanel();
        borrowPanel = new BorrowReturnPanel();
        reportsPanel = new ReportsPanel();
        notificationPanel = new NotificationPanel();
        finePanel = new FinePanel();
        fineApprovalPanel = new FineApprovalPanel();

        // ================= REGISTER =================
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(booksPanel, "books");
        contentPanel.add(borrowPanel, "borrow");
        contentPanel.add(reportsPanel, "reports");
        contentPanel.add(notificationPanel, "notif");
        contentPanel.add(finePanel, "fines");
        contentPanel.add(fineApprovalPanel, "payments");

        // ================= ACTIONS =================
        dashboardBtn.addActionListener(e -> open("dashboard"));
        booksBtn.addActionListener(e -> open("books"));
        borrowBtn.addActionListener(e -> open("borrow"));
        reportsBtn.addActionListener(e -> open("reports"));
        notifBtn.addActionListener(e -> open("notif"));
        finesBtn.addActionListener(e -> open("fines"));
        paymentBtn.addActionListener(e -> open("payments"));
        accountBtn.addActionListener(e -> open("account"));

        logoutBtn.addActionListener(e -> logout());

        open("dashboard");
    }

    // ================= OPEN =================
    private void open(String name) {

        switchPanel(name);

        SwingUtilities.invokeLater(() -> {

            if ("fines".equals(name) && finePanel != null)
                finePanel.load();

            if ("payments".equals(name) && fineApprovalPanel != null)
                fineApprovalPanel.load();

            contentPanel.revalidate();
            contentPanel.repaint();
        });
    }

    // ================= CARD =================
    private JPanel createCard(String title, String value, Color color) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLbl.setForeground(Color.GRAY);

        JLabel valueLbl = new JLabel(value);
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