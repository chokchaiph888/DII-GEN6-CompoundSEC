// UserPanelFrame.java - UI สำหรับการล็อกอินและสมัครผู้ใช้
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class UserPanelFrame extends JFrame {
    public UserPanelFrame() {
        setTitle("User Panel");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        panel.add(loginButton);
        panel.add(registerButton);
        add(panel);
        setVisible(true);
    }
}