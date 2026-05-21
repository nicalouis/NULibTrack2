package UIUtils;

import javax.swing.*;
import java.awt.*;

public class SidebarButtons extends JButton {

    private boolean isActive = false;

    public SidebarButtons(String text) {

        setText(text);
        setFocusPainted(false);

        setBackground(AppColor.PRIMARY);
        setForeground(AppColor.SECONDARY);

        setFont(new Font("Segoe UI", Font.BOLD, 14));

        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        setHorizontalAlignment(SwingConstants.LEFT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ================= HOVER EFFECT =================
        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    setBackground(new Color(35, 90, 170));
                    setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    setBackground(AppColor.PRIMARY);
                    setForeground(AppColor.SECONDARY);
                }
            }
        });
    }

    // ================= ACTIVE STATE =================
    public void setActive(boolean active) {

        this.isActive = active;

        if (active) {
            setBackground(AppColor.SECONDARY); // GOLD ACTIVE
            setForeground(Color.BLACK);
        } else {
            setBackground(AppColor.PRIMARY);
            setForeground(AppColor.SECONDARY);
        }
    }
}