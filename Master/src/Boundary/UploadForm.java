package Boundary;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import controller.ItemController;

/**
 * Upload Form for logging found items
 * Provides a comprehensive GUI for item registration following Three-Layer Architecture
 */
public class UploadForm extends JFrame {

    // GUI Components
    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JComboBox<String> cmbCategory;
    private JTextField txtLocation;
    private JSpinner spnDateFound;
    private JButton btnSelectPhoto;
    private JLabel lblPhotoPath;
    private JButton btnLogItem;

    // Data
    private String selectedPhotoPath;
    private ItemController itemController;

    // Colors and styling
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);

    // Category options
    private static final String[] CATEGORIES = {
        "Electronics", "Books", "Personal Items", "Bags", "Accessories"
    };

    /**
     * Default constructor
     */
    public UploadForm() {
        itemController = new ItemController();
        selectedPhotoPath = "";
        initializeComponents();
        setupLayout();
        setupListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Lost & Found - Log Found Item");
        setSize(550, 650);
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
     * Initialize GUI components
     */
    private void initializeComponents() {
        // Title field with enhanced styling
        txtTitle = new JTextField(25);
        txtTitle.setToolTipText("Enter a descriptive title for the found item");
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTitle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtTitle.setBackground(Color.WHITE);

        // Description area with enhanced styling - made much larger for better usability
        txtDescription = new JTextArea(10, 25);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setToolTipText("Provide detailed description of the item (color, brand, condition, size, unique features, markings, etc.)");
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        txtDescription.setBackground(Color.WHITE);
        // Set tab size for better formatting
        txtDescription.setTabSize(4);

        // Category combo box with enhanced styling
        cmbCategory = new JComboBox<>(CATEGORIES);
        cmbCategory.setSelectedIndex(0);
        cmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCategory.setBackground(Color.WHITE);
        cmbCategory.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Location field with enhanced styling
        txtLocation = new JTextField(25);
        txtLocation.setToolTipText("Specify the exact location where the item was found");
        txtLocation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLocation.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtLocation.setBackground(Color.WHITE);

        // Date spinner (default to current date) with enhanced styling
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        spnDateFound = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnDateFound, "yyyy-MM-dd");
        dateEditor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateEditor.getTextField().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        spnDateFound.setEditor(dateEditor);
        spnDateFound.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Photo selection with enhanced styling
        btnSelectPhoto = new JButton("Select Photo");
        btnSelectPhoto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSelectPhoto.setBackground(PRIMARY_COLOR);
        btnSelectPhoto.setForeground(Color.WHITE);
        btnSelectPhoto.setFocusPainted(false);
        btnSelectPhoto.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblPhotoPath = new JLabel("No photo selected");
        lblPhotoPath.setForeground(Color.GRAY);
        lblPhotoPath.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        // Submit button with enhanced styling
        btnLogItem = new JButton("Log Found Item");
        btnLogItem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogItem.setBackground(SUCCESS_COLOR);
        btnLogItem.setForeground(Color.WHITE);
        btnLogItem.setFocusPainted(false);
        btnLogItem.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btnLogItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Setup the layout
     */
    private void setupLayout() {
        setLayout(new BorderLayout(0, 15));
        getContentPane().setBackground(SECONDARY_COLOR);

        // Main form panel with enhanced styling
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 8, 12, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Create styled labels
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        // Row 0: Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtTitle, gbc);

        // Row 1: Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(labelFont);
        descLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(descLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.0;
        // Use the JTextArea directly without JScrollPane for a clean, large writing space
        txtDescription.setPreferredSize(new Dimension(300, 150)); // Width x Height for 10 rows
        formPanel.add(txtDescription, gbc);

        // Row 2: Category
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel catLabel = new JLabel("Category:");
        catLabel.setFont(labelFont);
        catLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(catLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(cmbCategory, gbc);

        // Row 3: Location
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel locLabel = new JLabel("Location Found:");
        locLabel.setFont(labelFont);
        locLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(locLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(txtLocation, gbc);

        // Row 4: Date Found
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel dateLabel = new JLabel("Date Found:");
        dateLabel.setFont(labelFont);
        dateLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(spnDateFound, gbc);

        // Row 5: Photo
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel photoLabel = new JLabel("Photo:");
        photoLabel.setFont(labelFont);
        photoLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(photoLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JPanel photoPanel = new JPanel(new BorderLayout(10, 0));
        photoPanel.setBackground(Color.WHITE);
        photoPanel.add(btnSelectPhoto, BorderLayout.WEST);
        photoPanel.add(lblPhotoPath, BorderLayout.CENTER);
        formPanel.add(photoPanel, gbc);

        // Button panel with enhanced styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        buttonPanel.setBackground(SECONDARY_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        // Add hover effect to button
        btnLogItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogItem.setBackground(SUCCESS_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogItem.setBackground(SUCCESS_COLOR);
            }
        });

        buttonPanel.add(btnLogItem);

        // Add padding around the main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Setup event listeners
     */
    private void setupListeners() {
        // Photo selection button
        btnSelectPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPhoto();
            }
        });

        // Submit button
        btnLogItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logItem();
            }
        });
    }

    /**
     * Handle photo selection using JFileChooser
     */
    private void selectPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Item Photo");

        // Filter for image files
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedPhotoPath = selectedFile.getAbsolutePath();
            lblPhotoPath.setText("✓ " + selectedFile.getName());
            lblPhotoPath.setForeground(SUCCESS_COLOR);
            lblPhotoPath.setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
    }

    /**
     * Handle item logging (submit button action)
     */
    private void logItem() {
        // Show "Logging Item..." state
        btnLogItem.setText("Logging Item...");
        btnLogItem.setEnabled(false);

        try {
            // Capture form data
            String title = txtTitle.getText().trim();
            String description = txtDescription.getText().trim();
            String category = (String) cmbCategory.getSelectedItem();
            String location = txtLocation.getText().trim();
            Date dateFound = (Date) spnDateFound.getValue();

            // Validate required fields (client-side validation)
            if (title.isEmpty()) {
                showErrorMessage("Please enter a title for the item.");
                return;
            }
            if (description.isEmpty()) {
                showErrorMessage("Please enter a description for the item.");
                return;
            }
            if (selectedPhotoPath.isEmpty()) {
                showErrorMessage("Please select a photo for the item.");
                return;
            }

            // Call controller (View → Controller → DAO)
            int newItemId = itemController.registerNewItem(title, description, category,
                                                         location, selectedPhotoPath, dateFound);

            if (newItemId > 0) {
                // Success - clear form and show success message
                showSuccessMessage("Item logged successfully!\nItem ID: " + newItemId);
                clearForm();

            } else if (newItemId == 0) {
                // Validation failed
                showErrorMessage("Please fill in all required fields correctly.");
            } else {
                // Database error
                showErrorMessage("Failed to save item to database.\nPlease try again later.");
            }

        } catch (Exception e) {
            System.err.println("Unexpected error during item logging: " + e.getMessage());
            showErrorMessage("An unexpected error occurred.\nPlease try again.");
        } finally {
            // Reset button state
            btnLogItem.setText("Log Item");
            btnLogItem.setEnabled(true);
        }
    }

    /**
     * Clear the form after successful submission
     */
    private void clearForm() {
        txtTitle.setText("");
        txtDescription.setText("");
        cmbCategory.setSelectedIndex(0);
        txtLocation.setText("");
        spnDateFound.setValue(new Date()); // Reset to current date
        selectedPhotoPath = "";
        lblPhotoPath.setText("No photo selected");
        lblPhotoPath.setForeground(Color.GRAY);
        lblPhotoPath.setFont(new Font("Segoe UI", Font.ITALIC, 12));
    }

    /**
     * Display success message
     */
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Display error message
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UploadForm().setVisible(true);
        });
    }
}