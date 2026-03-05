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
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton uploadButton = new JButton("Upload Item");
        uploadButton.setFont(new Font("Arial", Font.BOLD, 16));
        uploadButton.addActionListener(e -> {
            new UploadForm().setVisible(true);
        });

        JButton editButton = new JButton("Edit Item");
        editButton.setFont(new Font("Arial", Font.BOLD, 16));
        editButton.addActionListener(e -> {
            new EditItemWindow().setVisible(true);
        });

        JButton deleteButton = new JButton("Delete Item");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.addActionListener(e -> {
            new DeleteItemWindow().setVisible(true);
        });

        panel.add(uploadButton);
        panel.add(editButton);
        panel.add(deleteButton);

        String username = SessionManager.getInstance().getUsername();
        if (username == null || username.isEmpty()) {
            username = "Officer";
        }
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(welcomeLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }
}
