package Boundary;

import javax.swing.*;
import java.awt.*;
import core.SessionManager;

public class OfficerDashboard extends JFrame {

    public OfficerDashboard() {
        setTitle("Officer Dashboard - Lost and Found");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        factory.ui.UIFactory uiFactory = core.ThemeManager.getInstance().getFactory();
        getContentPane().setBackground(uiFactory.getBackgroundColor());

        JPanel panel = new JPanel(new GridLayout(3, 1, 15, 15));
        panel.setBackground(uiFactory.getBackgroundColor());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton uploadButton = uiFactory.createButton("Upload Item");
        uploadButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        uploadButton.addActionListener(e -> {
            JFrame form = new UploadForm();
            form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setVisible(true);
        });

        JButton editButton = uiFactory.createButton("Edit Item");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editButton.addActionListener(e -> {
            JFrame window = new EditItemWindow();
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.setVisible(true);
        });

        JButton deleteButton = uiFactory.createButton("Delete Item");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        deleteButton.addActionListener(e -> {
            JFrame window = new DeleteItemWindow();
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.setVisible(true);
        });

        panel.add(uploadButton);
        panel.add(editButton);
        panel.add(deleteButton);

        // Top Panel: Theme & Sign Out
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(uiFactory.getBackgroundColor());

        JButton toggleThemeBtn = uiFactory.createButton("Toggle Theme");
        toggleThemeBtn.addActionListener(e -> {
            core.ThemeManager.getInstance().toggleTheme();
            dispose();
            new OfficerDashboard().setVisible(true);
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
            username = "Officer";
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
