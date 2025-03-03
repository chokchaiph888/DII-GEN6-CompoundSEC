// MainMenuFrame.java - UI สำหรับเมนูหลัก
import javax.swing.*;
import java.awt.*;

// 1 ระบบเมนูหลักของโรงแรม
class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        setTitle("Hotel System");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton adminButton = new JButton("Admin Panel");
        JButton userButton = new JButton("User Panel");

        adminButton.addActionListener(e -> {
            dispose();
            new AdminFrame();
        });

        userButton.addActionListener(e -> {
            dispose();
            new UserFrame();
        });

        panel.add(adminButton);
        panel.add(userButton);
        add(panel);
        setVisible(true);
    }
}