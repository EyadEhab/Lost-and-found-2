package Boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import DAO.ItemDataAccess;
import entity.Item;
import factory.dao.DataAccessFactory;
import factory.dao.SqlDataAccessFactory;

/**
 * Delete Item Window for removing items from the database
 */
public class DeleteItemWindow extends JFrame {

    private JTable itemTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private ItemDataAccess itemDAO;

    public DeleteItemWindow() {
        setTitle("Delete Item - Lost and Found");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        DataAccessFactory dataFactory = new SqlDataAccessFactory();
        itemDAO = dataFactory.createItemDAO();

        initComponents();
        refreshItemTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel: Search and Actions
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search by ID");
        searchButton.addActionListener(e -> searchItem());

        JButton refreshButton = new JButton("Refresh All");
        refreshButton.addActionListener(e -> refreshItemTable());

        topPanel.add(new JLabel("Item ID:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center: Table
        String[] columnNames = { "ID", "Title", "Description", "Category", "Location", "Date Found", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemTable = new JTable(tableModel);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(itemTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom: Delete Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton deleteButton = new JButton("Delete Selected Item");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.addActionListener(e -> deleteSelectedItem());
        bottomPanel.add(deleteButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void refreshItemTable() {
        tableModel.setRowCount(0);
        List<Item> items = itemDAO.getAllItems();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Item item : items) {
            Object[] row = {
                    item.getItemID(),
                    item.getTitle(),
                    truncateText(item.getDescription(), 50),
                    item.getCategory(),
                    item.getLocation(),
                    item.getDateFound() != null ? sdf.format(item.getDateFound()) : "",
                    item.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void searchItem() {
        String idStr = searchField.getText().trim();
        if (idStr.isEmpty())
            return;

        try {
            int itemId = Integer.parseInt(idStr);
            Item item = itemDAO.findItemByID(itemId);

            tableModel.setRowCount(0);
            if (item != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Object[] row = {
                        item.getItemID(),
                        item.getTitle(),
                        truncateText(item.getDescription(), 50),
                        item.getCategory(),
                        item.getLocation(),
                        item.getDateFound() != null ? sdf.format(item.getDateFound()) : "",
                        item.getStatus()
                };
                tableModel.addRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Item not found with ID: " + itemId);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format");
        }
    }

    private void deleteSelectedItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.");
            return;
        }

        int itemId = (int) tableModel.getValueAt(selectedRow, 0);
        String itemTitle = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to PERMANENTLY DELETE item:\n" +
                        "ID: " + itemId + "\nTitle: " + itemTitle + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            itemDAO.removeItem(itemId);
            JOptionPane.showMessageDialog(this, "Item deleted successfully!");
            refreshItemTable();
        }
    }

    private String truncateText(String text, int maxLength) {
        if (text == null)
            return "";
        if (text.length() <= maxLength)
            return text;
        return text.substring(0, maxLength) + "...";
    }
}