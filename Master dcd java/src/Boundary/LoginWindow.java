package Boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dataaccess.DBConnection;
import core.SessionManager;
import factory.DashboardCreator;
import factory.AdminDashboardCreator;
import factory.OfficerDashboardCreator;
import factory.StudentDashboardCreator;
import factory.ui.UIFactory;
import core.ThemeManager;

public class LoginWindow extends JFrame {

    private UIFactory getUIFactory() {
        return ThemeManager.getInstance().getFactory();
    }

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
        UIFactory factory = getUIFactory();
        getContentPane().setBackground(factory.getBackgroundColor());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(factory.getBackgroundColor());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(factory.createLabel("Username:"));
        usernameField = factory.createTextField(15);
        panel.add(usernameField);

        panel.add(factory.createLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.setBackground(factory.getSurfaceColor());
        passwordField.setForeground(factory.getTextColor());
        passwordField.setCaretColor(factory.getTextColor());
        panel.add(passwordField);

        loginButton = factory.createButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        JButton toggleButton = factory.createButton("Toggle Theme");
        toggleButton.addActionListener(e -> {
            ThemeManager.getInstance().toggleTheme();
            dispose();
            new LoginWindow().setVisible(true);
        });

        panel.add(toggleButton);
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
            int userId = fetchUserIdByUsername(username);
            SessionManager.getInstance().setSession(userId, username, role);

            JOptionPane.showMessageDialog(this, "Login Successful! Role: " + role);
            dispose(); // Close login window
            openWindowForRole(role);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Best-effort lookup of the authenticated user's ID based on username.
     * Falls back to 0 if no matching user is found.
     */
    private int fetchUserIdByUsername(String username) {
        java.util.List<Object[]> users = DBConnection.getAllUsersWithRoles();
        if (users != null) {
            for (Object[] row : users) {
                if (row != null && row.length >= 2) {
                    int id = (Integer) row[0];
                    String name = (String) row[1];
                    if (name != null && name.equals(username)) {
                        return id;
                    }
                }
            }
        }
        return 0;
    }

    private void openWindowForRole(String role) {
        // Normalize role string (trim and lowercase) for comparison
        role = role.trim();

        DashboardCreator creator;

        if (role.equalsIgnoreCase("Admin")) {
            creator = new AdminDashboardCreator();
        } else if (role.equalsIgnoreCase("Officer")) {
            creator = new OfficerDashboardCreator();
        } else if (role.equalsIgnoreCase("Student")) {
            creator = new StudentDashboardCreator();
        } else {
            JOptionPane.showMessageDialog(null, "Unknown role: " + role, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame dashboard = creator.createDashboard();
        dashboard.setVisible(true);
    }
}
