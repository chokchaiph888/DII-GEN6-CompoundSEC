// LoginFrame.java - UI สำหรับล็อกอิน
import javax.swing.*;
import java.awt.*;

class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton, backButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> loginUser());

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose();
            new UserPanelFrame();
        });

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(loginButton);
        panel.add(backButton);

        add(panel);
        setVisible(true);
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (UserManager.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();
            new UserFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}