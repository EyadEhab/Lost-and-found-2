package Boundary;

import behaviouralpatterns.observer.case1_itemposting.NotificationManager;
import core.SessionManager;
import core.ThemeManager;
import entity.NotificationRecord;
import factory.ui.UIFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationWindow extends JFrame {

    public NotificationWindow() {
        setTitle("My Notifications");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        UIFactory factory = ThemeManager.getInstance().getFactory();
        getContentPane().setBackground(factory.getBackgroundColor());
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = factory.createLabel("Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Channel", "Message", "Time"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setBackground(factory.getSurfaceColor());
        table.setForeground(factory.getTextColor());
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);

        int userId = SessionManager.getInstance().getUserId();
        List<NotificationRecord> records = NotificationManager.getInstance().getNotificationsForUser(userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (NotificationRecord record : records) {
            model.addRow(new Object[]{
                    record.getChannel(),
                    record.getMessage(),
                    sdf.format(record.getTimestamp())
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = factory.createButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(factory.getBackgroundColor());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
