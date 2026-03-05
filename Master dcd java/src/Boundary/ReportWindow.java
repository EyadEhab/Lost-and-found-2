package Boundary;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import dataaccess.DBConnection;
import factory.ui.UIFactory;
import factory.ui.LightUIFactory;

public class ReportWindow extends javax.swing.JFrame {

    private JLabel foundLabel;
    private JLabel claimsLabel;
    private JLabel collectedLabel;
    private JLabel notCollectedLabel;

    private final UIFactory uiFactory = new LightUIFactory();

    public ReportWindow() {
        setTitle("Generate Reports");
        setSize(400, 300);
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshStats();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        foundLabel = uiFactory.createLabel("Found Items: -");
        claimsLabel = uiFactory.createLabel("Total Claims: -");
        collectedLabel = uiFactory.createLabel("Items Collected: -");
        notCollectedLabel = uiFactory.createLabel("Items Not Collected: -");

        foundLabel.setFont(new Font("Arial", Font.BOLD, 14));
        claimsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        collectedLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        notCollectedLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton refreshButton = uiFactory.createButton("Refresh Statistics");
        refreshButton.addActionListener(e -> refreshStats());

        mainPanel.add(foundLabel);
        mainPanel.add(claimsLabel);
        mainPanel.add(collectedLabel);
        mainPanel.add(notCollectedLabel);
        mainPanel.add(refreshButton);

        add(mainPanel);
    }

    private void refreshStats() {
        Map<String, Integer> stats = DBConnection.getReportStats();

        foundLabel.setText("Found Items: " + stats.getOrDefault("Found", 0));
        claimsLabel.setText("Total Claims: " + stats.getOrDefault("Claims", 0));
        collectedLabel.setText("Items Collected: " + stats.getOrDefault("Collected", 0));
        notCollectedLabel.setText("Items Not Collected: " + stats.getOrDefault("Not Collected", 0));
    }
}