package Boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dataaccess.DBConnection;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginWindow() {
        setTitle("Lost and Found Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Add empty label for spacing
        panel.add(new JLabel(""));
        panel.add(loginButton);

        add(panel);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String role = DBConnection.authenticateUser(username, password);

        if (role != null) {
            JOptionPane.showMessageDialog(this, "Login Successful! Role: " + role);
            dispose(); // Close login window
            openWindowForRole(role);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openWindowForRole(String role) {
        // Normalize role string (trim and lowercase) for comparison
        role = role.trim();

        if (role.equalsIgnoreCase("Student")) {
            new SearchWindow().setVisible(true);
        } else if (role.equalsIgnoreCase("Officer")) {
            new OfficerDashboard().setVisible(true);
        } else if (role.equalsIgnoreCase("Admin")) {
            new AdminDashboard().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Unknown role: " + role, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
