package main;

import LoginUI.LoginFrame;
import javax.swing.SwingUtilities;

public class LibTrackMain {

    public static void main(String[] args) {

        // 🔥 Ensures proper Swing UI thread execution
        SwingUtilities.invokeLater(() -> {

            try {
                new LoginFrame().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to launch LoginFrame.");
            }
        });
    }
}