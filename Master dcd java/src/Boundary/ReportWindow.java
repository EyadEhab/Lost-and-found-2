package Boundary;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import notification.NotificationBridgeDemo;
import controller.ReportController;
import dataaccess.DBConnection;
import report.CsvReportFormatter;
import report.PdfReportFormatter;
import report.ScreenReportFormatter;
import factory.ui.UIFactory;
import core.ThemeManager;
import adapter.report.ItemReportAdapter;

import entity.Item;

public class ReportWindow extends javax.swing.JFrame {

    private JLabel foundLabel;
    private JLabel claimsLabel;
    private JLabel collectedLabel;
    private JLabel notCollectedLabel;

    // Use current theme so this window participates in application theming
    private final UIFactory uiFactory = ThemeManager.getInstance().getFactory();

    /** Optional Bridge demo; does not replace existing stats logic. */
    private final ReportController reportController = new ReportController();

    public ReportWindow() {
        setTitle("Generate Reports");
        setSize(400, 300);
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshStats();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(uiFactory.getBackgroundColor());

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

        JButton reportButton = uiFactory.createButton("Report Bridge");
        reportButton.addActionListener(e -> showReportBridgeDemo());

        JButton exportCsvButton = uiFactory.createButton("Export All Items (CSV)");
        exportCsvButton.addActionListener(e -> exportAllItemsToCsv());

        mainPanel.add(foundLabel);
        mainPanel.add(claimsLabel);
        mainPanel.add(collectedLabel);
        mainPanel.add(notCollectedLabel);
        mainPanel.add(refreshButton);
        mainPanel.add(reportButton);
        mainPanel.add(exportCsvButton);

        getContentPane().setBackground(uiFactory.getBackgroundColor());
        add(mainPanel);
    }

    private void refreshStats() {
        Map<String, Integer> stats = DBConnection.getReportStats();

        foundLabel.setText("Found Items: " + stats.getOrDefault("Found", 0));
        claimsLabel.setText("Total Claims: " + stats.getOrDefault("Claims", 0));
        collectedLabel.setText("Items Collected: " + stats.getOrDefault("Collected", 0));
        notCollectedLabel.setText("Items Not Collected: " + stats.getOrDefault("Not Collected", 0));
    }

    /**
     * Shows Bridge-based report text (screen + simulated PDF) without changing
     * existing DAO behavior.
     */
    private void showReportBridgeDemo() {
        String screenText = reportController.buildWeeklyReportBridge(new ScreenReportFormatter(), null);
        String pdfLikeText = reportController.buildWeeklyReportBridge(new PdfReportFormatter(), null);
        String csvText = reportController.buildWeeklyReportBridge(new CsvReportFormatter(), null);
        JTextArea area = new JTextArea(
                screenText + "\n\n---\n\n" + pdfLikeText + "\n\n---\n\n" + csvText);
        area.setEditable(false);
        area.setRows(16);
        area.setColumns(48);
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(this, scroll, "Report Bridge demo", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows Decorator-based report text (screen + simulated PDF) without changing
     * existing DAO behavior.
     */
    private void showReportDecoratorDemo() {
        String screenText = reportController.buildWeeklyReportDecorated(new ScreenReportFormatter(), null);
        String pdfLikeText = reportController.buildWeeklyReportDecorated(new PdfReportFormatter(), null);
        String csvText = reportController.buildWeeklyReportDecorated(new CsvReportFormatter(), null);

        JTextArea area = new JTextArea(
                screenText + "\n\n---\n\n" + pdfLikeText + "\n\n---\n\n" + csvText);
        area.setEditable(false);
        area.setRows(20);
        area.setColumns(52);
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(this, scroll, "Report Decorator demo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportAllItemsToCsv() {
        // Fetch all items from DB
        DAO.ItemDataAccess ida = new DAO.ItemDataAccess();
        java.util.List<Item> allItems = ida.getAllItems();

        // Use the report adapter to generate CSV
        ItemReportAdapter adapter = new ItemReportAdapter(allItems);

        // Save to project root folder as requested: "report adapter.csv"
        String rootDir = System.getProperty("user.dir");
        String filePath = rootDir + java.io.File.separator + "report adapter.csv";

        adapter.exportToCsv(filePath);

        JOptionPane.showMessageDialog(this,
                "Successfully exported items to:\n" + filePath,
                "Export Successful",
                JOptionPane.INFORMATION_MESSAGE);
    }
}