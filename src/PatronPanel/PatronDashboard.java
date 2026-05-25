package PatronPanel;

import Dashboard.DashboardLayout;
import LoginUI.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class PatronDashboard extends DashboardLayout {

    private CatalogPanel catalogPanel;
    private BorrowedPanel borrowedPanel;
    private ReservationPanel reservationPanel;
    private HistoryPanel historyPanel;
    private FinePanel finePanel;
    private MyAccountPanel accountPanel;

    private JButton activeButton;

    public PatronDashboard() {

        super();

        initPanels();
        initSidebar();
        openDefault();
    }

    // ================= PANELS =================
    private void initPanels() {

        catalogPanel = new CatalogPanel();
        borrowedPanel = new BorrowedPanel();
        reservationPanel = new ReservationPanel();
        historyPanel = new HistoryPanel();
        finePanel = new FinePanel();
        accountPanel = new MyAccountPanel();

        contentPanel.add(catalogPanel, "catalog");
        contentPanel.add(borrowedPanel, "borrowed");
        contentPanel.add(reservationPanel, "reservation");
        contentPanel.add(historyPanel, "history");
        contentPanel.add(finePanel, "fines");
        contentPanel.add(accountPanel, "account");
    }

    // ================= SIDEBAR =================
    private void initSidebar() {

        sidebar.removeAll();
        sidebar.setLayout(new GridLayout(0, 1, 8, 8));

        sidebar.setBackground(new Color(18, 30, 60));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel menu = new JLabel("PATRON MENU", SwingConstants.CENTER);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 16));

        sidebar.add(menu);

        JButton catalogBtn = createBtn("Catalog");
        JButton borrowedBtn = createBtn("Borrowed");
        JButton reserveBtn = createBtn("Reservations");
        JButton historyBtn = createBtn("History");
        JButton finesBtn = createBtn("Fines");
        JButton accountBtn = createBtn("My Account");

        // 🔥 NEW LOGOUT BUTTON
        JButton logoutBtn = createBtn("Logout");
        logoutBtn.setBackground(new Color(200, 50, 50));

        // ================= ACTIONS =================
        catalogBtn.addActionListener(e -> {
            switchPanel("catalog");
            setActive(catalogBtn);
        });

        borrowedBtn.addActionListener(e -> {
            switchPanel("borrowed");
            borrowedPanel.refresh();
            setActive(borrowedBtn);
        });

        reserveBtn.addActionListener(e -> {
            switchPanel("reservation");
            reservationPanel.refresh();
            setActive(reserveBtn);
        });

        historyBtn.addActionListener(e -> {
            switchPanel("history");
            historyPanel.load();
            setActive(historyBtn);
        });

        finesBtn.addActionListener(e -> {
            switchPanel("fines");
            finePanel.load();
            setActive(finesBtn);
        });

        accountBtn.addActionListener(e -> {
            switchPanel("account");

            SwingUtilities.invokeLater(() -> {
                accountPanel.refresh();
            });

            setActive(accountBtn);
        });

        // ================= LOGOUT ACTION =================
        logoutBtn.addActionListener(e -> logout());

        // ================= ADD TO SIDEBAR =================
        sidebar.add(catalogBtn);
        sidebar.add(borrowedBtn);
        sidebar.add(reserveBtn);
        sidebar.add(historyBtn);
        sidebar.add(finesBtn);
        sidebar.add(accountBtn);

        sidebar.add(Box.createVerticalStrut(20)); // spacing
        sidebar.add(logoutBtn);
    }

    // ================= LOGOUT =================
    private void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }

    // ================= BUTTON STYLE =================
    private JButton createBtn(String text) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setOpaque(true);

        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btn.setBackground(new Color(25, 40, 75));
        btn.setForeground(Color.WHITE);

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 204, 0), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        return btn;
    }

    // ================= ACTIVE BUTTON =================
    private void setActive(JButton btn) {

        if (activeButton != null) {
            activeButton.setBackground(new Color(25, 40, 75));
            activeButton.setForeground(Color.WHITE);
        }

        activeButton = btn;

        if (activeButton != null) {
            activeButton.setBackground(new Color(255, 204, 0));
            activeButton.setForeground(Color.BLACK);
        }
    }

    // ================= DEFAULT =================
    private void openDefault() {
        switchPanel("catalog");
        setActive(null);
    }
}