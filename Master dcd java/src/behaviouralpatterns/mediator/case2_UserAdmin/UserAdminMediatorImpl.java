package behaviouralpatterns.mediator.case2_UserAdmin;

import dataaccess.DBConnection;
import behaviouralpatterns.mediator.case2_UserAdmin.ActionColleague.ConfirmData;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Mediator Pattern - User Admin Mediator Implementation
 *
 * Concrete mediator that coordinates all user admin UI components.
 * Handles user CRUD operations through DBConnection.
 *
 * Role: ConcreteMediator
 */
public class UserAdminMediatorImpl implements UserAdminMediator {

    private final Map<ColleagueType, UserAdminColleague> colleagues;
    private int selectedUserId = -1;
    private String selectedUserName = null;

    public UserAdminMediatorImpl() {
        this.colleagues = new EnumMap<>(ColleagueType.class);
    }

    @Override
    public void registerColleague(ColleagueType type, UserAdminColleague colleague) {
        colleagues.put(type, colleague);
        colleague.setMediator(this);
    }

    @Override
    public void notifyTableSelectionChanged(Object[] userData) {
        if (userData != null && userData.length >= 2) {
            selectedUserId = (Integer) userData[0];
            selectedUserName = (String) userData[1];

            // Enable action buttons since a user is selected
            ActionColleague actionColleague =
                (ActionColleague) colleagues.get(ColleagueType.ACTIONS);
            actionColleague.onMediatorEvent("ENABLE_ACTIONS", null);

            // Enable role dropdown
            RoleDropdownColleague roleColleague =
                (RoleDropdownColleague) colleagues.get(ColleagueType.ROLE_DROPDOWN);
            roleColleague.onMediatorEvent("ENABLE_DROPDOWN", null);
        }
    }

    @Override
    public void notifySearchRequested(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            ActionColleague actionColleague =
                (ActionColleague) colleagues.get(ColleagueType.ACTIONS);
            actionColleague.onMediatorEvent("SHOW_WARNING", "Please enter a User ID to search.");
            return;
        }

        try {
            int id = Integer.parseInt(userId.trim());
            Object[] user = DBConnection.searchUserById(id);

            UserTableColleague tableColleague =
                (UserTableColleague) colleagues.get(ColleagueType.TABLE);

            if (user != null) {
                // Clear table and show only the found user
                tableColleague.onMediatorEvent("CLEAR_TABLE", null);
                tableColleague.onMediatorEvent("REFRESH_TABLE", Collections.singletonList(user));
            } else {
                tableColleague.onMediatorEvent("CLEAR_TABLE", null);
                ActionColleague actionColleague =
                    (ActionColleague) colleagues.get(ColleagueType.ACTIONS);
                actionColleague.onMediatorEvent("SHOW_ERROR",
                    "User not found with ID: " + id);
            }

            // Reset selection
            selectedUserId = -1;
            selectedUserName = null;
            ActionColleague actionColleague =
                (ActionColleague) colleagues.get(ColleagueType.ACTIONS);
            actionColleague.onMediatorEvent("DISABLE_ACTIONS", null);

        } catch (NumberFormatException e) {
            ActionColleague actionColleague =
                (ActionColleague) colleagues.get(ColleagueType.ACTIONS);
            actionColleague.onMediatorEvent("SHOW_ERROR", "Invalid ID format. Please enter a number.");
        }
    }

    @Override
    public void notifyRoleUpdateRequested(int userId, String newRole) {
        // Get actual values from colleagues since parameters are placeholders
        if (selectedUserId == -1) {
            ActionColleague actionColleague =
                (ActionColleague) colleagues.get(ColleagueType.ACTIONS);
            actionColleague.onMediatorEvent("SHOW_WARNING", "Please select a user from the table.");
            return;
        }

        RoleDropdownColleague roleColleague =
            (RoleDropdownColleague) colleagues.get(ColleagueType.ROLE_DROPDOWN);
        String selectedRole = roleColleague.getSelectedRole();

        // Get current role from table
        UserTableColleague tableColleague =
            (UserTableColleague) colleagues.get(ColleagueType.TABLE);
        int selectedRow = tableColleague.getSelectedRow();
        String currentRole = null;
        if (selectedRow != -1) {
            currentRole = (String) tableColleague.getTableModel().getValueAt(selectedRow, 3);
        }

        if (selectedRole != null && selectedRole.equalsIgnoreCase(currentRole)) {
            ActionColleague actionColleague =
                (ActionColleague) colleagues.get(ColleagueType.ACTIONS);
            actionColleague.onMediatorEvent("SHOW_WARNING",
                "User is already a " + selectedRole);
            return;
        }

        // Confirm the role change
        String message = "Are you sure you want to change User " + selectedUserId +
                        " from " + currentRole + " to " + selectedRole + "?";

        ActionColleague actionColleague =
            (ActionColleague) colleagues.get(ColleagueType.ACTIONS);

        actionColleague.onMediatorEvent("SHOW_CONFIRM_AND_EXECUTE",
            new ConfirmData(message, "Confirm Role Change", () -> {
                boolean success = DBConnection.updateUserRole(selectedUserId, selectedRole);
                if (success) {
                    actionColleague.onMediatorEvent("SHOW_SUCCESS",
                        "Role updated successfully.");
                    // Refresh the table
                    notifyRefreshRequested();
                } else {
                    actionColleague.onMediatorEvent("SHOW_ERROR",
                        "Failed to update role.");
                }
            }));
    }

    @Override
    public void notifyUserDeleteRequested(int userId) {
        if (selectedUserId == -1) {
            ActionColleague actionColleague =
                (ActionColleague) colleagues.get(ColleagueType.ACTIONS);
            actionColleague.onMediatorEvent("SHOW_WARNING",
                "Please select a user to remove.");
            return;
        }

        // Confirm the deletion
        String message = "Are you sure you want to PERMANENTLY DELETE user: " +
                        selectedUserName + " (ID: " + selectedUserId + ")?";

        ActionColleague actionColleague =
            (ActionColleague) colleagues.get(ColleagueType.ACTIONS);

        actionColleague.onMediatorEvent("SHOW_CONFIRM_AND_EXECUTE",
            new ConfirmData(message, "Confirm Delete", () -> {
                boolean success = DBConnection.deleteUser(selectedUserId);
                if (success) {
                    actionColleague.onMediatorEvent("SHOW_SUCCESS",
                        "User deleted successfully.");
                    // Refresh the table
                    notifyRefreshRequested();
                } else {
                    actionColleague.onMediatorEvent("SHOW_ERROR",
                        "Failed to delete user.");
                }
            }));
    }

    @Override
    public void notifyAddUserRequested() {
        // Open add user dialog
        JDialog dialog = new JDialog((Frame) null, "Add New User", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
                // Refresh the table
                notifyRefreshRequested();
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Failed to add user (Email might exist).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    @Override
    public void notifyRefreshRequested() {
        List<Object[]> users = DBConnection.getAllUsersWithRoles();

        UserTableColleague tableColleague =
            (UserTableColleague) colleagues.get(ColleagueType.TABLE);

        // Clear and repopulate table
        tableColleague.onMediatorEvent("CLEAR_TABLE", null);
        tableColleague.onMediatorEvent("REFRESH_TABLE", users);

        // Reset selection state
        selectedUserId = -1;
        selectedUserName = null;

        // Disable action buttons
        ActionColleague actionColleague =
            (ActionColleague) colleagues.get(ColleagueType.ACTIONS);
        actionColleague.onMediatorEvent("DISABLE_ACTIONS", null);

        // Disable role dropdown
        RoleDropdownColleague roleColleague =
            (RoleDropdownColleague) colleagues.get(ColleagueType.ROLE_DROPDOWN);
        roleColleague.onMediatorEvent("DISABLE_DROPDOWN", null);
    }
}
