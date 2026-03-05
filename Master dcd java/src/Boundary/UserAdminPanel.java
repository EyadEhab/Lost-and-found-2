package Boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import dataaccess.DBConnection;

public class UserAdminPanel extends javax.swing.JFrame {

    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> roleDropdown;

    public UserAdminPanel() {
        setTitle("Manage Users & Roles");
        setSize(800, 500);
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshUserTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel: Search and Add
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search ID");
        searchButton.addActionListener(e -> searchUser());

        JButton refreshButton = new JButton("Refresh All");
        refreshButton.addActionListener(e -> refreshUserTable());

        JButton addUserButton = new JButton("Add New User");
        addUserButton.addActionListener(e -> showAddUserDialog());

        topPanel.add(new JLabel("User ID:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(addUserButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center: Table
        String[] columnNames = { "ID", "Name", "Email", "Role" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(userTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom: Update Role
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        roleDropdown = new JComboBox<>(new String[] { "Student", "Officer", "Admin" });
        JButton updateRoleButton = new JButton("Update Selected User Role");
        updateRoleButton.addActionListener(e -> updateSelectedUserRole());

        bottomPanel.add(new JLabel("New Role:"));
        bottomPanel.add(roleDropdown);
        bottomPanel.add(updateRoleButton);

        JButton removeUserButton = new JButton("Remove Selected User");
        removeUserButton.setForeground(Color.RED);
        removeUserButton.addActionListener(e -> removeSelectedUser());
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(removeUserButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void refreshUserTable() {
        tableModel.setRowCount(0);
        List<Object[]> users = DBConnection.getAllUsersWithRoles();
        for (Object[] user : users) {
            tableModel.addRow(user);
        }
    }

    private void searchUser() {
        String idStr = searchField.getText().trim();
        if (idStr.isEmpty())
            return;

        try {
            int userId = Integer.parseInt(idStr);
            Object[] user = DBConnection.searchUserById(userId);
            tableModel.setRowCount(0);
            if (user != null) {
                tableModel.addRow(user);
            } else {
                JOptionPane.showMessageDialog(this, "User not found with ID: " + userId);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format");
        }
    }

    private void updateSelectedUserRole() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table.");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentRole = (String) tableModel.getValueAt(selectedRow, 3);
        String newRole = (String) roleDropdown.getSelectedItem();

        if (newRole.equalsIgnoreCase(currentRole)) {
            JOptionPane.showMessageDialog(this, "User is already a " + newRole);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to change User " + userId + " from " + currentRole + " to " + newRole + "?",
                "Confirm Role Change", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = DBConnection.updateUserRole(userId, newRole);
            if (success) {
                JOptionPane.showMessageDialog(this, "Role updated successfully.");
                refreshUserTable(); // Refresh list
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update role.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to remove.");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String userName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to PERMANENTLY DELETE user: " + userName + " (ID: " + userId + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = DBConnection.deleteUser(userId);
            if (success) {
                JOptionPane.showMessageDialog(this, "User deleted successfully.");
                refreshUserTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddUserDialog() {
        JDialog dialog = new JDialog(this, "Add New User", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10)); // From 5 to 4
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Removed idField as it's auto-generated by the database
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JComboBox<String> roleCombo = new JComboBox<>(new String[] { "Student", "Officer", "Admin" });

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(new JLabel("Role:"));
        panel.add(roleCombo);

        JButton saveButton = new JButton("Save User");
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            String role = (String) roleCombo.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.");
                return;
            }

            boolean success = DBConnection.addUser(name, email, pass, role);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "User added successfully.");
                dialog.dispose();
                refreshUserTable();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add user (Email might exist).", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}