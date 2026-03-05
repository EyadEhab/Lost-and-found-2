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

/**
 * Search Window for Lost and Found items
 * Provides search and filtering functionality following Three-Layer Architecture
 */
public class SearchWindow extends JFrame {

    // UI Components
    private JTextField searchField;
    private JComboBox<String> categoryCombo;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel searchIcon;
    private JLabel categoryIcon;
    private JLabel resultsCountLabel;
    private JPanel searchPanel;

    // Controller
    private MatchingController matchingController;

    // Colors and styling
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private static final Color HEADER_COLOR = new Color(100, 149, 237);
    private static final Color EVEN_ROW_COLOR = new Color(248, 250, 252);
    private static final Color ODD_ROW_COLOR = Color.WHITE;

    // Categories
    private static final String[] CATEGORIES = {
        "All Categories", "Electronics", "Books", "Personal Items", "Bags", "Accessories"
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
        // Search field with enhanced styling
        searchField = new JTextField(25);
        searchField.setToolTipText("Enter keywords to search for lost items (e.g., 'wallet', 'phone', 'keys')");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        searchField.setBackground(Color.WHITE);

        // Search icon label
        searchIcon = new JLabel("Search:");
        searchIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchIcon.setForeground(PRIMARY_COLOR);

        // Category combo box with enhanced styling
        categoryCombo = new JComboBox<>(CATEGORIES);
        categoryCombo.setSelectedIndex(0); // Default to "All Categories"
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryCombo.setBackground(Color.WHITE);
        categoryCombo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Category icon label
        categoryIcon = new JLabel("Category:");
        categoryIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoryIcon.setForeground(PRIMARY_COLOR);

        // Results count label
        resultsCountLabel = new JLabel("Ready to search...");
        resultsCountLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        resultsCountLabel.setForeground(new Color(100, 100, 100));

        // Results table with enhanced styling
        String[] columnNames = {"Photo", "Title", "Category", "Description", "Location", "Date Found", "Status"};
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
        resultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultsTable.setRowHeight(30);
        resultsTable.setGridColor(new Color(230, 230, 230));
        resultsTable.setShowGrid(true);
        resultsTable.setIntercellSpacing(new Dimension(1, 1));

        // Custom table header
        JTableHeader header = resultsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Custom cell renderer for alternating row colors
        resultsTable.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        // Set column widths
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Photo
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(160); // Title
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(110); // Category
        resultsTable.getColumnModel().getColumn(3).setPreferredWidth(220); // Description
        resultsTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Location
        resultsTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Date Found
        resultsTable.getColumnModel().getColumn(6).setPreferredWidth(90);  // Status
    }

    /**
     * Setup the layout
     */
    private void setupLayout() {
        setLayout(new BorderLayout(0, 10));
        getContentPane().setBackground(SECONDARY_COLOR);

        // Top panel for search controls with enhanced styling
        searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        searchPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Search section
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        searchPanel.add(searchIcon, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        searchPanel.add(searchField, gbc);

        // Category section
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        searchPanel.add(categoryIcon, gbc);

        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(categoryCombo, gbc);

        // Results count
        gbc.gridx = 4; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        searchPanel.add(resultsCountLabel, gbc);

        // Center panel for results with enhanced styling
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Add padding around the main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(searchPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Setup event listeners
     */
    private void setupListeners() {
        // Document listener for search field (real-time search)
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            @Override
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            @Override
            public void changedUpdate(DocumentEvent e) { performSearch(); }
        });

        // Action listener for category combo box
        categoryCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
    }

    /**
     * Perform search using the controller
     */
    private void performSearch() {
        try {
            String query = searchField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();

            // Call controller (View → Controller → DAO)
            List<Item> results = matchingController.performSmartSearch(query, category);

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
            String photoStatus = (item.getPhotoPath() != null && !item.getPhotoPath().trim().isEmpty()) ? "[PHOTO]" : "[NO PHOTO]";
            String title = generateTitle(item);
            String category = item.getCategory() != null ? item.getCategory() : "Unknown";
            String description = truncateDescription(item.getDescription());
            String location = extractLocation(item.getTags());
            String dateFound = item.getDateFound() != null ? dateFormat.format(item.getDateFound()) : "Unknown";
            String status = item.getStatus();

            // Add status indicator for processing claims
            if ("Processing Claim".equals(status)) {
                status = "[PENDING] Claim Pending";
            } else if ("Available".equals(status)) {
                status = "[AVAILABLE] Available";
            } else if ("Claimed".equals(status)) {
                status = "[CLAIMED] Claimed";
            }

            Object[] row = {photoStatus, title, category, description, location, dateFound, status};
            tableModel.addRow(row);
        }

        // Update table
        tableModel.fireTableDataChanged();
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
        if (description == null) return "";
        return description.length() > 50 ? description.substring(0, 50) + "..." : description;
    }

    /**
     * Extract location from tags
     */
    private String extractLocation(String tags) {
        if (tags == null) return "Unknown";

        // Look for "Location: " pattern in tags
        int locationIndex = tags.indexOf("Location: ");
        if (locationIndex != -1) {
            String location = tags.substring(locationIndex + 10);
            // Remove any trailing content after comma or semicolon
            int endIndex = Math.min(
                location.indexOf(',') != -1 ? location.indexOf(',') : location.length(),
                location.indexOf(';') != -1 ? location.indexOf(';') : location.length()
            );
            return location.substring(0, endIndex).trim();
        }

        return "Unknown";
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

            // Set alternating row colors
            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(EVEN_ROW_COLOR);
                } else {
                    c.setBackground(ODD_ROW_COLOR);
                }
            } else {
                c.setBackground(table.getSelectionBackground());
            }

            // Special styling for photo column
            if (column == 0 && value != null) { // Photo column
                String photoStatus = value.toString();
                if (photoStatus.contains("[PHOTO]")) {
                    c.setForeground(new Color(40, 167, 69)); // Green for has photo
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (photoStatus.contains("[NO PHOTO]")) {
                    c.setForeground(Color.GRAY); // Gray for no photo
                    c.setFont(c.getFont().deriveFont(Font.ITALIC));
                } else {
                    c.setForeground(Color.BLACK);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }
            }
            // Special styling for status column
            else if (column == 6 && value != null) { // Status column
                String status = value.toString();
                if (status.contains("[AVAILABLE]")) {
                    c.setForeground(new Color(40, 167, 69)); // Green for available
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (status.contains("[PENDING]") || "Processing Claim".equals(status)) {
                    c.setForeground(new Color(255, 193, 7)); // Orange for pending
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (status.contains("[CLAIMED]")) {
                    c.setForeground(new Color(220, 53, 69)); // Red for claimed
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setForeground(Color.BLACK);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }
            } else {
                c.setForeground(Color.BLACK);
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