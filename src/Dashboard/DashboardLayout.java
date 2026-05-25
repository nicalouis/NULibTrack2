package Dashboard;

import UIUtils.AppColor;
import javax.swing.*;
import java.awt.*;

public class DashboardLayout extends JFrame {

    protected JPanel sidebar;
    protected JPanel contentPanel;

    protected JPanel sidebarBox;

    private JButton activeButton;

    public DashboardLayout() {

        setTitle("NU LibTrack");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= TOP BAR =================
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(AppColor.PRIMARY);
        topbar.setPreferredSize(new Dimension(1400, 85));

        JLabel title = new JLabel("NU LIBTRACK SYSTEM");
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 26));
        title.setForeground(AppColor.SECONDARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel shadow = new JLabel("NU LIBTRACK SYSTEM");
        shadow.setFont(new Font("Segoe UI Black", Font.BOLD, 26));
        shadow.setForeground(new Color(0, 0, 0, 80));
        shadow.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titleStack = new JPanel();
        titleStack.setLayout(new OverlayLayout(titleStack));
        titleStack.setOpaque(false);

        JPanel shadowWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        shadowWrap.setOpaque(false);
        shadowWrap.add(shadow);

        JPanel titleWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titleWrap.setOpaque(false);
        titleWrap.add(title);

        titleStack.add(shadowWrap);
        titleStack.add(titleWrap);

        JLabel subtitle = new JLabel("Library Tracking & Management System");
        subtitle.setForeground(new Color(200, 200, 200));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        titleStack.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(titleStack);
        header.add(Box.createVerticalStrut(3));
        header.add(subtitle);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(header);

        topbar.add(center, BorderLayout.CENTER);

        // ================= SIDEBAR =================
        sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(AppColor.SIDEBAR_BG);
        sidebar.setBorder(BorderFactory.createEmptyBorder(18, 12, 18, 12));
        sidebar.setPreferredSize(new Dimension(260, 700));

        // ================= SCROLLABLE BOX (FIXED) =================
       // ================= SCROLLABLE BOX (FIXED) =================
sidebarBox = new JPanel();

sidebarBox.setLayout(new BoxLayout(sidebarBox, BoxLayout.Y_AXIS));

sidebarBox.setBackground(new Color(25, 40, 75));

sidebarBox.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(255, 204, 0, 80), 1),
        BorderFactory.createEmptyBorder(15, 12, 15, 12)
));

        JLabel menu = new JLabel("PATRON MENU", SwingConstants.CENTER);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebarBox.add(menu);
        sidebarBox.add(Box.createVerticalStrut(10));

        sidebar.add(sidebarBox, BorderLayout.CENTER);

        // ================= CONTENT =================
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(AppColor.BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(topbar, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    protected void switchPanel(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }

    public JButton createSidebarButton(String text) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        btn.setBackground(new Color(35, 55, 95));
        btn.setForeground(Color.WHITE);

        btn.setOpaque(true);

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 204, 0, 90), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    protected void setActiveButtonExternal(JButton btn) {

        if (activeButton != null) {
            activeButton.setBackground(new Color(35, 55, 95));
            activeButton.setForeground(Color.WHITE);
        }

        activeButton = btn;

        if (activeButton != null) {
            activeButton.setBackground(AppColor.SIDEBAR_ACTIVE);
            activeButton.setForeground(Color.BLACK);
        }
    }
}