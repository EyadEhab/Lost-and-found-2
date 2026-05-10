package Boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import entity.Claim;
import controller.ClaimController;
import factory.dao.DataAccessFactory;
import factory.dao.SqlDataAccessFactory;
import DAO.ClaimDataAccess;

public class OfficerClaimsWindow extends JFrame {

    private JTable claimsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> strategyCombo;
    private JButton processButton;
    private ClaimController claimController;
    private List<Claim> pendingClaims;

    public OfficerClaimsWindow() {
        claimController = new ClaimController();
        setTitle("Officer - Claims Management");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadPendingClaims();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        factory.ui.UIFactory uiFactory = core.ThemeManager.getInstance().getFactory();
        getContentPane().setBackground(uiFactory.getBackgroundColor());

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(uiFactory.getBackgroundColor());
        JLabel titleLabel = uiFactory.createLabel("Pending Claims Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel (Table)
        String[] columnNames = {"Claim ID", "Item ID", "Student ID", "Date Initiated", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        claimsTable = new JTable(tableModel);
        claimsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        claimsTable.setBackground(uiFactory.getSurfaceColor());
        claimsTable.setForeground(uiFactory.getTextColor());
        JScrollPane scrollPane = new JScrollPane(claimsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel (Process Action)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(uiFactory.getBackgroundColor());
        
        JLabel strategyLabel = uiFactory.createLabel("Processing Strategy:");
        strategyCombo = new JComboBox<>(new String[]{"Strict", "Fast", "Manual"});
        
        processButton = uiFactory.createButton("Process Selected Claim");
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSelectedClaim();
            }
        });

        bottomPanel.add(strategyLabel);
        bottomPanel.add(strategyCombo);
        bottomPanel.add(processButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadPendingClaims() {
        tableModel.setRowCount(0);
        try {
            DataAccessFactory factory = new SqlDataAccessFactory();
            ClaimDataAccess dao = factory.createClaimDAO();
            pendingClaims = dao.getPendingClaims();
            
            for (Claim claim : pendingClaims) {
                Object[] row = {
                    claim.getClaimID(),
                    claim.getItemID(),
                    claim.getStudentID(),
                    claim.getRequestDate(),
                    claim.getStatus()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading claims: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void processSelectedClaim() {
        int selectedRow = claimsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a claim to process.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Claim selectedClaim = pendingClaims.get(selectedRow);
        String selectedStrategy = ((String) strategyCombo.getSelectedItem()).toLowerCase();

        try {
            // Process the claim using the strategy
            claimController.processClaimWithStrategy(selectedClaim, selectedStrategy);
            
            // Debug print to verify strategy applied
            System.out.println("Strategy set status: " + selectedClaim.getStatus());
            
            // Persist the status change
            claimController.updateClaimStatus(selectedClaim.getClaimID(), selectedClaim.getStatus());

            JOptionPane.showMessageDialog(this, "Claim processed successfully. New status: " + selectedClaim.getStatus(), "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh table 
            loadPendingClaims(); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing claim: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
