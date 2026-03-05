package Boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import core.SessionManager;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        factory.ui.UIFactory uiFactory = core.ThemeManager.getInstance().getFactory();
        getContentPane().setBackground(uiFactory.getBackgroundColor());

        JPanel panel = new JPanel(new GridLayout(2, 1, 20, 20));
        panel.setBackground(uiFactory.getBackgroundColor());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton manageRolesButton = uiFactory.createButton("Manage Users & Roles");
        manageRolesButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        manageRolesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserAdminPanel().setVisible(true);
            }
        });

        JButton reportsButton = uiFactory.createButton("View Reports");
        reportsButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        reportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReportWindow().setVisible(true);
            }
        });

        panel.add(manageRolesButton);
        panel.add(reportsButton);

        // Top Panel: Theme & Sign Out
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(uiFactory.getBackgroundColor());

        JButton toggleThemeBtn = uiFactory.createButton("Toggle Theme");
        toggleThemeBtn.addActionListener(e -> {
            core.ThemeManager.getInstance().toggleTheme();
            dispose();
            new AdminDashboard().setVisible(true);
        });

        JButton signOutBtn = uiFactory.createButton("Sign Out");
        signOutBtn.addActionListener(e -> {
            SessionManager.getInstance().clearSession();
            dispose();
            new LoginWindow().setVisible(true);
        });

        topPanel.add(toggleThemeBtn);
        topPanel.add(signOutBtn);

        String username = SessionManager.getInstance().getUsername();
        if (username == null || username.isEmpty()) {
            username = "Admin";
        }
        JLabel welcomeLabel = uiFactory.createLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(topPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(uiFactory.getBackgroundColor());
        centerPanel.add(welcomeLabel, BorderLayout.NORTH);
        centerPanel.add(panel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }
}
