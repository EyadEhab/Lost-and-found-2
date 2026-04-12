package Boundary;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import controller.MatchingController;
import entity.Item;
import factory.ui.UIFactory;
// Observer + Singleton pattern integration
import behaviouralpatterns.observer.case1_itemposting.ItemNotificationService;

/**
 * Search Window for Lost and Found items
 * Provides search and filtering functionality following Three-Layer
 * Architecture
 */
public class SearchWindow extends JFrame {

    // UI Components
    private JTextField searchField;
    private JTextField locationField;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> statusCombo;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel searchIcon;
    private JLabel categoryIcon;
    private JLabel locationIcon;
    private JLabel statusIcon;
    private JLabel resultsCountLabel;
    private JPanel searchPanel;
    private JButton claimButton;
    // Observer Pattern: subscribe current user to item alerts
    private JButton subscribeBtn;

    // Controller
    private MatchingController matchingController;

    // Observer + Singleton: service managing the current user's subscription
    private final ItemNotificationService notificationService = new ItemNotificationService();

    // Categories
    private static final String[] CATEGORIES = {
            "All Categories", "Electronics", "Books", "Personal Items", "Bags", "Accessories"
    };

    private static final String[] STATUS_OPTIONS = {
            "Available", "Found", "Processing Claim", "Collected", "Archived", "All Statuses"
    };

    /**
     * Default constructor
     */
    public SearchWindow() {
        matchingController = new MatchingController();
        initializeComponents();
        setupLayout();
        setupListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Lost & Found - Search Items");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(true);

        // Set application icon (optional, using default if not available)
        try {
            setIconImage(Toolkit.getDefaultToolkit().createImage(""));
        } catch (Exception e) {
            // Ignore if icon not available
        }

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }
    }

    /**
     * Initialize UI components
     */
    private void initializeComponents() {
        UIFactory factory = core.ThemeManager.getInstance().getFactory();

        // Search field with enhanced styling
        searchField = factory.createTextField(25);
        searchField.setToolTipText("Enter keywords to search for lost items (e.g., 'wallet', 'phone', 'keys')");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Search icon label
        searchIcon = factory.createLabel("Search:");
        searchIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Category combo box with enhanced styling
        categoryCombo = new JComboBox<>(CATEGORIES);
        categoryCombo.setSelectedIndex(0); // Default to "All Categories"
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryCombo.setBackground(factory.getSurfaceColor());
        categoryCombo.setForeground(factory.getTextColor());
        categoryCombo.setBorder(BorderFactory.createLineBorder(
                core.ThemeManager.getInstance().isDarkMode() ? new Color(80, 80, 80) : new Color(200, 200, 200), 1));

        // Category icon label
        categoryIcon = factory.createLabel("Category:");
        categoryIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Location field with enhanced styling
        locationField = factory.createTextField(15);
        locationField.setToolTipText("Filter by location (e.g., 'Library', 'Gym')");
        locationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Location icon label
        locationIcon = factory.createLabel("Location:");
        locationIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Status combo box with enhanced styling
        statusCombo = new JComboBox<>(STATUS_OPTIONS);
        statusCombo.setSelectedIndex(0); // Default to "Available"
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusCombo.setBackground(factory.getSurfaceColor());
        statusCombo.setForeground(factory.getTextColor());
        statusCombo.setBorder(BorderFactory.createLineBorder(
                core.ThemeManager.getInstance().isDarkMode() ? new Color(80, 80, 80) : new Color(200, 200, 200), 1));

        // Status icon label
        statusIcon = factory.createLabel("Status:");
        statusIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Results count label
        resultsCountLabel = factory.createLabel("Ready to search...");
        resultsCountLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 14));
        resultsCountLabel.setForeground(factory.getTextColor());

        // Observer Pattern: "Notify Me" toggle button
        // Subscribes the logged-in user to alerts when a new item matches
        // their current search filters (category / location / keyword).
        subscribeBtn = factory.createButton("\uD83D\uDD14 Notify Me");
        subscribeBtn.setToolTipText("Subscribe to alerts when a new item matches your current search filters");

        // Results table with enhanced styling
        String[] columnNames = { "ID", "Photo", "Title", "Category", "Description", "Location", "Date Found", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.getTableHeader().setReorderingAllowed(false);

        // Enhanced table styling
        resultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        resultsTable.setRowHeight(40);
        resultsTable.setGridColor(
                core.ThemeManager.getInstance().isDarkMode() ? new Color(70, 70, 70) : new Color(210, 210, 210));
        resultsTable.setShowGrid(true);
        resultsTable.setIntercellSpacing(new Dimension(1, 1));
        resultsTable.setSelectionBackground(factory.getAccentColor());
        resultsTable.setSelectionForeground(Color.WHITE);

        // Custom table header
        JTableHeader header = resultsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(factory.getAccentColor());
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Custom cell renderer for alternating row colors
        resultsTable.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        // Set column widths
        resultsTable.getColumnModel().getColumn(0).setMinWidth(0); // ID (Hidden)
        resultsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(80); // Photo
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(160); // Title
        resultsTable.getColumnModel().getColumn(3).setPreferredWidth(110); // Category
        resultsTable.getColumnModel().getColumn(3).setPreferredWidth(220); // Description
        resultsTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Location
        resultsTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Date Found
        resultsTable.getColumnModel().getColumn(6).setPreferredWidth(90); // Status
    }

    /**
     * Setup the layout
     */
    private void setupLayout() {
        setLayout(new BorderLayout(0, 10));
        UIFactory factory = core.ThemeManager.getInstance().getFactory();
        getContentPane().setBackground(factory.getBackgroundColor());

        // Top toolbar for Theme & Sign Out
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbar.setBackground(factory.getBackgroundColor());

        JButton toggleThemeBtn = factory.createButton("Toggle Theme");
        toggleThemeBtn.addActionListener(e -> {
            core.ThemeManager.getInstance().toggleTheme();
            dispose();
            new SearchWindow().setVisible(true);
        });

        JButton signOutBtn = factory.createButton("Sign Out");
        signOutBtn.addActionListener(e -> {
            core.SessionManager.getInstance().clearSession();
            dispose();
            new LoginWindow().setVisible(true);
        });

        JButton notificationsBtn = factory.createButton("Notifications");
        notificationsBtn.addActionListener(e -> new NotificationWindow().setVisible(true));

        toolbar.add(notificationsBtn);
        toolbar.add(toggleThemeBtn);
        toolbar.add(signOutBtn);

        // Claim button for students
        claimButton = factory.createButton("Claim Selected Item");
        claimButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        claimButton.setBackground(new Color(40, 167, 69));
        claimButton.setForeground(Color.WHITE);
        claimButton.addActionListener(e -> initiateClaim());

        // Observer unsubscribe — clean up when window is closed
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Remove the user from NotificationManager when they close the window
                notificationService.unsubscribeCurrentUser();
            }
        });

        // Search panel setup
        searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(factory.getAccentColor(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        searchPanel.setBackground(factory.getSurfaceColor());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Search section
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        searchPanel.add(searchIcon, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        searchPanel.add(searchField, gbc);

        // Category section
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        searchPanel.add(categoryIcon, gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(categoryCombo, gbc);

        // --- Second Row: Location & Status ---
        gbc.gridy = 1;
        gbc.weightx = 0.0;

        // Location section
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        searchPanel.add(locationIcon, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        searchPanel.add(locationField, gbc);

        // Status section
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        searchPanel.add(statusIcon, gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(statusCombo, gbc);

        // Center panel for results with enhanced styling
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(factory.getAccentColor(), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        scrollPane.setBackground(factory.getBackgroundColor());
        scrollPane.getViewport().setBackground(factory.getBackgroundColor());
        resultsTable.setBackground(factory.getSurfaceColor());
        resultsTable.setForeground(factory.getTextColor());
        resultsTable.setGridColor(
                core.ThemeManager.getInstance().isDarkMode() ? new Color(60, 60, 60) : new Color(230, 230, 230));

        // Add padding around the main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(factory.getBackgroundColor());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(factory.getBackgroundColor());
        bottomPanel.add(claimButton);
        bottomPanel.add(subscribeBtn);  // Observer: subscribe button next to claim
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(factory.getBackgroundColor());
        container.add(toolbar, BorderLayout.NORTH);
        container.add(searchPanel, BorderLayout.CENTER);

        add(container, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Setup event listeners
     */
    private void setupListeners() {
        // Document listener for search field (real-time search)
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });

        // Action listener for category combo box
        categoryCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        // Document listener for location field
        locationField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            @Override
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            @Override
            public void changedUpdate(DocumentEvent e) { performSearch(); }
        });

        // Action listener for status combo box
        statusCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        // ── Observer Pattern: Subscribe button listener ──────────────────────
        // When the user clicks "Notify Me", subscribe them using their
        // current search filters as preferences.
        // The NotificationManager (Singleton) stores this subscription and
        // will call the observer automatically when ItemController posts a
        // new item that matches.
        subscribeBtn.addActionListener(e -> {
            if (notificationService.isSubscribed()) {
                // Already subscribed — toggle OFF
                notificationService.unsubscribeCurrentUser();
                subscribeBtn.setText("\uD83D\uDD14 Notify Me");
                JOptionPane.showMessageDialog(this,
                        "You will no longer receive item alerts.",
                        "Unsubscribed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Subscribe with the current filter values
                String category = (String) categoryCombo.getSelectedItem();
                String location = locationField.getText().trim();
                String keyword  = searchField.getText().trim();

                if ((category == null || category.equals("All Categories"))
                        && location.isEmpty() && keyword.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter at least one filter (category, location, or keyword)\nbefore subscribing.",
                            "No Filter Set", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Register with the Singleton NotificationManager via the service
                notificationService.subscribeCurrentUser(
                        category != null && !category.equals("All Categories") ? category : "",
                        location,
                        keyword);

                subscribeBtn.setText("\u2705 Subscribed — Click to Stop");
                JOptionPane.showMessageDialog(this,
                        "You are now subscribed!\n"
                        + "You will be notified when a new item matching your\n"
                        + "current filters is posted in the system.",
                        "Subscribed", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // ─────────────────────────────────────────────────────────────────────
    }

    /**
     * Perform search using the controller
     */
    private void performSearch() {
        try {
            String query = searchField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String location = locationField.getText().trim();
            String status = (String) statusCombo.getSelectedItem();

            // Call controller with all filters
            List<Item> results = matchingController.performSmartSearch(query, category, location, status);

            // Update UI with results
            updateResultsTable(results);

            // Update results count
            updateResultsCount(results.size(), query, category);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error performing search: " + e.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE);
            resultsCountLabel.setText("Search error occurred");
            resultsCountLabel.setForeground(Color.RED);
        }
    }

    /**
     * Update the results table with search results
     */
    private void updateResultsTable(List<Item> items) {
        // Clear existing rows
        tableModel.setRowCount(0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Add items to table
        for (Item item : items) {
            String photoStatus = (item.getPhotoPath() != null && !item.getPhotoPath().trim().isEmpty()) ? "[PHOTO]"
                    : "[NO PHOTO]";
            String title = generateTitle(item);
            String category = item.getCategory() != null ? item.getCategory() : "Unknown";
            String description = truncateDescription(item.getDescription());
            String location = item.getLocation() != null ? item.getLocation() : "Unknown";
            String dateFound = item.getDateFound() != null ? dateFormat.format(item.getDateFound()) : "Unknown";
            String status = item.getStatus();

            // Add status indicator for processing claims
            if ("Processing Claim".equals(status)) {
                status = "[PENDING] Claim Pending";
            } else if ("Available".equals(status) || "Found".equals(status)) {
                status = "[AVAILABLE] Available";
            } else if ("Claimed".equals(status) || "Collected".equals(status)) {
                status = "[CLAIMED] Claimed";
            }

            Object[] row = { item.getItemID(), photoStatus, title, category, description, location, dateFound, status };
            tableModel.addRow(row);
        }

        // Update table
        tableModel.fireTableDataChanged();
    }

    /**
     * Initiate the claim process for the selected item
     */
    private void initiateClaim() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item from the table first.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get Item ID from the first column (hidden)
        int itemID = (Integer) resultsTable.getValueAt(selectedRow, 0);
        String status = (String) resultsTable.getValueAt(selectedRow, 7); // Status column

        if (status.contains("Claim Pending") || status.contains("Claimed")) {
            JOptionPane.showMessageDialog(this, "This item is already " + status + ".", "Invalid Action",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Observer Pattern: after opening the claim window, unsubscribe the user
        // because they found their item — no more notifications needed.
        notificationService.unsubscribeCurrentUser();
        subscribeBtn.setText("\uD83D\uDD14 Notify Me");

        new ClaimSubmissionWindow(itemID).setVisible(true);
    }

    /**
     * Generate a title for the item based on description
     */
    private String generateTitle(Item item) {
        String description = item.getDescription();
        if (description == null || description.trim().isEmpty()) {
            return "Untitled Item";
        }

        // Take first 30 characters as title
        String title = description.length() > 30 ? description.substring(0, 30) + "..." : description;
        return title;
    }

    /**
     * Truncate description for display
     */
    private String truncateDescription(String description) {
        if (description == null)
            return "";
        return description.length() > 50 ? description.substring(0, 50) + "..." : description;
    }

    /**
     * Getters and Setters for backward compatibility
     */
    public Object getResultsGrid() {
        return resultsTable;
    }

    public void setResultsGrid(Object resultsGrid) {
        // For backward compatibility, but we use JTable now
    }

    /**
     * Legacy method for backward compatibility
     */
    public void onSearchClick(java.util.Map<String, Object> filters) {
        // This method is kept for backward compatibility
        // The real search is handled by DocumentListener and ActionListener
        performSearch();
    }

    /**
     * Update the results count display
     */
    private void updateResultsCount(int count, String query, String category) {
        String countText;
        if (count == 0) {
            countText = "No items found";
            resultsCountLabel.setForeground(new Color(220, 53, 69));
        } else if (count == 1) {
            countText = "Found 1 item";
            resultsCountLabel.setForeground(new Color(40, 167, 69));
        } else {
            countText = "Found " + count + " items";
            resultsCountLabel.setForeground(new Color(40, 167, 69));
        }

        if (!query.isEmpty() || !"All Categories".equals(category)) {
            String searchInfo = "";
            if (!query.isEmpty()) {
                searchInfo += " for '" + query + "'";
            }
            if (!"All Categories".equals(category)) {
                searchInfo += " in " + category;
            }
            countText += searchInfo;
        }

        resultsCountLabel.setText(countText);
    }

    /**
     * Custom table cell renderer for alternating row colors and status highlighting
     */
    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            UIFactory factory = core.ThemeManager.getInstance().getFactory();
            Color bgColor = factory.getBackgroundColor();
            Color surfaceColor = factory.getSurfaceColor();
            Color textColor = factory.getTextColor();

            // Set alternating row colors
            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(surfaceColor);
                } else {
                    c.setBackground(bgColor);
                }
                c.setForeground(textColor);
            } else {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }

            boolean isDark = core.ThemeManager.getInstance().isDarkMode();

            // Special styling for photo column
            if (column == 0 && value != null) { // Photo column
                String photoStatus = value.toString();
                if (photoStatus.contains("[PHOTO]")) {
                    c.setForeground(isDark ? new Color(100, 255, 100) : new Color(40, 167, 69)); // Brighter green for
                                                                                                 // dark mode
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (photoStatus.contains("[NO PHOTO]")) {
                    c.setForeground(isDark ? new Color(150, 150, 150) : Color.GRAY);
                    c.setFont(c.getFont().deriveFont(Font.ITALIC));
                }
            }
            // Special styling for status column
            else if (column == 6 && value != null) { // Status column
                String status = value.toString();
                if (status.contains("[AVAILABLE]")) {
                    c.setForeground(isDark ? new Color(100, 255, 100) : new Color(40, 167, 69));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (status.contains("[PENDING]") || "Processing Claim".equals(status)) {
                    c.setForeground(isDark ? new Color(255, 215, 0) : new Color(200, 150, 0)); // Gold/Orange
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (status.contains("[CLAIMED]")) {
                    c.setForeground(isDark ? new Color(255, 100, 100) : new Color(220, 53, 69)); // Bright red
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                }
            } else {
                c.setForeground(textColor);
                c.setFont(c.getFont().deriveFont(Font.PLAIN));
            }

            // Add padding to cells
            if (c instanceof JLabel) {
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            }

            return c;
        }
    }

    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SearchWindow().setVisible(true);
        });
    }
}