package Boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        JPanel panel = new JPanel(new GridLayout(2, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton manageRolesButton = new JButton("Manage Users & Roles");
        manageRolesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open UserAdminPanel (assuming it's a window or panel we need to show)
                // For now, let's assume UserAdminPanel is a JFrame or we wrap it in one
                // Since UserAdminPanel is currently just empty, we should probably update it to
                // extend JFrame or show it here.
                // The user request implied redirecting.
                new UserAdminPanel().setVisible(true);
            }
        });

        JButton reportsButton = new JButton("View Reports");
        reportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open ReportWindow
                new ReportWindow().setVisible(true);
            }
        });

        panel.add(manageRolesButton);
        panel.add(reportsButton);

        add(panel);
    }
}
