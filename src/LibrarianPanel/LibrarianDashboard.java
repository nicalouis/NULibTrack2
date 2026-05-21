package LibrarianPanel;

import Dashboard.DashboardLayout;
import UIUtils.*;

import javax.swing.*;

public class LibrarianDashboard extends DashboardLayout {

    public LibrarianDashboard() {

        super();

        // ===== SIDEBAR (LIBRARIAN SIDE) =====
        SidebarButtons dashboardBtn = new SidebarButtons("Dashboard");
        SidebarButtons booksBtn = new SidebarButtons("Books");
        SidebarButtons borrowBtn = new SidebarButtons("Borrow / Return");
        SidebarButtons reportsBtn = new SidebarButtons("Reports");
        SidebarButtons notifBtn = new SidebarButtons("Notifications");
        SidebarButtons finesBtn = new SidebarButtons("Fines Overview");

        sidebar.add(dashboardBtn);
        sidebar.add(booksBtn);
        sidebar.add(borrowBtn);
        sidebar.add(reportsBtn);
        sidebar.add(notifBtn);
        sidebar.add(finesBtn);

        // ===== PANELS =====
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.add(new JLabel("Librarian Dashboard Home"));

        BooksPanel booksPanel = new BooksPanel();
        BorrowReturnPanel borrowPanel = new BorrowReturnPanel();
        ReportsPanel reportsPanel = new ReportsPanel();
        NotificationPanel notificationPanel = new NotificationPanel();
        FinePanel finePanel = new FinePanel();

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(booksPanel, "books");
        contentPanel.add(borrowPanel, "borrow");
        contentPanel.add(reportsPanel, "reports");
        contentPanel.add(notificationPanel, "notif");
        contentPanel.add(finePanel, "fines");

        // ===== BUTTON ACTIONS =====
        dashboardBtn.addActionListener(e -> switchPanel("dashboard"));
        booksBtn.addActionListener(e -> switchPanel("books"));
        borrowBtn.addActionListener(e -> switchPanel("borrow"));
        reportsBtn.addActionListener(e -> switchPanel("reports"));
        notifBtn.addActionListener(e -> switchPanel("notif"));
        finesBtn.addActionListener(e -> switchPanel("fines"));

        switchPanel("dashboard");
    }
}