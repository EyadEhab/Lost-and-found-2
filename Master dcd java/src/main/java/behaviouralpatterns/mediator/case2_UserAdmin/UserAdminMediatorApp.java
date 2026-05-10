package behaviouralpatterns.mediator.case2_UserAdmin;

import dataaccess.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Mediator Pattern - User Admin Application Main Class
 *
 * This is the main entry point that demonstrates the Mediator pattern
 * for user administration. It creates the UI components, wires them
 * together through the mediator, and launches the user admin window.
 *
 * This is NOT a demo - it's a fully functional user management system that:
 * - Displays all users with their roles
 * - Searches for users by ID
 * - Updates user roles
 * - Deletes users
 * - Adds new users
 */
public class UserAdminMediatorApp extends JFrame {

    private UserAdminMediator mediator;
    private UserTableColleague tableColleague;
    private SearchFieldColleague searchColleague;
    private RoleDropdownColleague roleColleague;
    private ActionColleague actionColleague;

    public UserAdminMediatorApp() {
        setTitle("Manage Users & Roles - Mediator Pattern");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeMediator();
        initComponents();
        setupLayout();

        // Load initial data
        mediator.notifyRefreshRequested();
    }

    /**
     * Creates the mediator instance.
     */
    private void initializeMediator() {
        mediator = new UserAdminMediatorImpl();
    }

    /**
     * Creates UI components and wraps them in colleagues.
     */
    private void initComponents() {
        // Create table and model
        String[] columnNames = { "ID", "Name", "Email", "Role" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create table colleague
        tableColleague = new UserTableColleague(userTable, tableModel);
        mediator.registerColleague(UserAdminMediator.ColleagueType.TABLE, tableColleague);

        // Create search field and button
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search ID");
        searchColleague = new SearchFieldColleague(searchField, searchButton);
        mediator.registerColleague(UserAdminMediator.ColleagueType.SEARCH, searchColleague);

        // Create refresh button
        JButton refreshButton = new JButton("Refresh All");

        // Create add user button
        JButton addUserButton = new JButton("Add New User");

        // Create role dropdown
        JComboBox<String> roleDropdown = new JComboBox<>(new String[] { "Student", "Officer", "Admin" });
        roleColleague = new RoleDropdownColleague(roleDropdown);
        mediator.registerColleague(UserAdminMediator.ColleagueType.ROLE_DROPDOWN, roleColleague);

        // Create update role button
        JButton updateRoleButton = new JButton("Update Selected User Role");

        // Create remove user button
        JButton removeUserButton = new JButton("Remove Selected User");
        removeUserButton.setForeground(Color.RED);

        // Create action colleague with all buttons
        actionColleague = new ActionColleague(refreshButton, addUserButton, updateRoleButton, removeUserButton);
        mediator.registerColleague(UserAdminMediator.ColleagueType.ACTIONS, actionColleague);

        // Store references for layout
        this.searchField = searchField;
        this.refreshButton = refreshButton;
        this.addUserButton = addUserButton;
        this.updateRoleButton = updateRoleButton;
        this.removeUserButton = removeUserButton;
    }

    // Store references for layout setup
    private JTextField searchField;
    private JButton refreshButton;
    private JButton addUserButton;
    private JButton updateRoleButton;
    private JButton removeUserButton;

    /**
     * Sets up the layout with all UI components.
     */
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel: Search and Add
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("User ID:"));
        topPanel.add(searchField);
        topPanel.add(searchColleague.getSearchButton());
        topPanel.add(refreshButton);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(addUserButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center: Table
        JScrollPane scrollPane = new JScrollPane(tableColleague.getUserTable());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom: Update Role and Remove User
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(new JLabel("New Role:"));
        bottomPanel.add(roleColleague.getRoleDropdown());
        bottomPanel.add(updateRoleButton);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(removeUserButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Main method - launches the user admin application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserAdminMediatorApp().setVisible(true);
        });
    }
}
