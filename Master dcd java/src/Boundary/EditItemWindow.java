package Boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import DAO.ItemDataAccess;
import entity.Item;
import factory.dao.DataAccessFactory;
import factory.dao.SqlDataAccessFactory;

/**
 * Edit Item Window for modifying existing found items
 */
public class EditItemWindow extends JFrame {

    private JTable itemTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private ItemDataAccess itemDAO;

    // Edit form components
    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JComboBox<String> cmbCategory;
    private JComboBox<String> cmbStatus;
    private JTextField txtLocation;
    private JSpinner spnDateFound;
    private JButton btnSelectPhoto;
    private JLabel lblPhotoPath;
    private String selectedPhotoPath;
    private int selectedItemId = -1;

    private static final String[] CATEGORIES = {
            "Electronics", "Books", "Personal Items", "Bags", "Accessories"
    };

    private static final String[] STATUSES = {
            "Found", "Processing Claim", "Collected", "Archived"
    };

    public EditItemWindow() {
        setTitle("Edit Item - Lost and Found");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        DataAccessFactory dataFactory = new SqlDataAccessFactory();
        itemDAO = dataFactory.createItemDAO();
        selectedPhotoPath = "";

        initComponents();
        refreshItemTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel: Search
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

        // Center: Split Pane with Table and Edit Form
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);

        // Table Panel
        String[] columnNames = { "ID", "Title", "Category", "Location", "Date Found", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemTable = new JTable(tableModel);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedItemToForm();
            }
        });
        JScrollPane scrollPane = new JScrollPane(itemTable);

        // Edit Form Panel
        JPanel editFormPanel = createEditFormPanel();

        splitPane.setTopComponent(scrollPane);
        splitPane.setBottomComponent(editFormPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createEditFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Edit Item Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTitle = new JTextField(30);
        panel.add(txtTitle, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtDescription = new JTextArea(4, 30);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        panel.add(descScroll, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbCategory = new JComboBox<>(CATEGORIES);
        panel.add(cmbCategory, gbc);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Location:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtLocation = new JTextField(30);
        panel.add(txtLocation, gbc);

        // Date Found
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Date Found:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        spnDateFound = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnDateFound, "yyyy-MM-dd");
        spnDateFound.setEditor(dateEditor);
        panel.add(spnDateFound, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbStatus = new JComboBox<>(STATUSES);
        panel.add(cmbStatus, gbc);

        // Photo
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Photo:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel photoPanel = new JPanel(new BorderLayout(10, 0));
        btnSelectPhoto = new JButton("Change Photo");
        btnSelectPhoto.addActionListener(e -> selectPhoto());
        lblPhotoPath = new JLabel("No photo selected");
        lblPhotoPath.setForeground(Color.GRAY);
        photoPanel.add(btnSelectPhoto, BorderLayout.WEST);
        photoPanel.add(lblPhotoPath, BorderLayout.CENTER);
        panel.add(photoPanel, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveChanges());

        JButton cancelButton = new JButton("Clear Form");
        cancelButton.addActionListener(e -> clearForm());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private void refreshItemTable() {
        tableModel.setRowCount(0);
        List<Item> items = itemDAO.getAllItems();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Item item : items) {
            Object[] row = {
                    item.getItemID(),
                    item.getTitle(),
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

    private void loadSelectedItemToForm() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow == -1)
            return;

        selectedItemId = (int) tableModel.getValueAt(selectedRow, 0);
        Item item = itemDAO.findItemByID(selectedItemId);

        if (item != null) {
            txtTitle.setText(item.getTitle());
            txtDescription.setText(item.getDescription());
            cmbCategory.setSelectedItem(item.getCategory());
            txtLocation.setText(item.getLocation());
            cmbStatus.setSelectedItem(item.getStatus() != null ? item.getStatus() : "Found");
            if (item.getDateFound() != null) {
                spnDateFound.setValue(item.getDateFound());
            }
            selectedPhotoPath = item.getPhotoPath() != null ? item.getPhotoPath() : "";
            lblPhotoPath.setText(selectedPhotoPath.isEmpty() ? "No photo" : new File(selectedPhotoPath).getName());
        }
    }

    private void selectPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Item Photo");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedPhotoPath = selectedFile.getAbsolutePath();
            lblPhotoPath.setText("✓ " + selectedFile.getName());
            lblPhotoPath.setForeground(new Color(40, 167, 69));
        }
    }

    private void saveChanges() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.");
            return;
        }

        String title = txtTitle.getText().trim();
        String description = txtDescription.getText().trim();
        String category = (String) cmbCategory.getSelectedItem();
        String status = (String) cmbStatus.getSelectedItem();
        String location = txtLocation.getText().trim();
        Date dateFound = (Date) spnDateFound.getValue();

        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Description are required.");
            return;
        }

        Item item = new Item();
        item.setItemID(selectedItemId);
        item.setTitle(title);
        item.setDescription(description);
        item.setCategory(category);
        item.setLocation(location);
        item.setDateFound(dateFound);
        item.setPhotoPath(selectedPhotoPath);
        item.setStatus(status);

        itemDAO.updateItemRecord(item);
        JOptionPane.showMessageDialog(this, "Item updated successfully!");
        refreshItemTable();
        clearForm();
    }

    private void clearForm() {
        selectedItemId = -1;
        txtTitle.setText("");
        txtDescription.setText("");
        cmbCategory.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        txtLocation.setText("");
        spnDateFound.setValue(new Date());
        selectedPhotoPath = "";
        lblPhotoPath.setText("No photo selected");
        lblPhotoPath.setForeground(Color.GRAY);
        itemTable.clearSelection();
    }

    /**
     * @param id
     * @param newData
     */
    public void onEditConfirm(int id, Object newData) {
        // Legacy method - kept for compatibility
    }

}