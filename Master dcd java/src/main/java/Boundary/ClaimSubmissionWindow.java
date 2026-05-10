package Boundary;

import javax.swing.*;
import java.awt.*;
import core.SessionManager;
import core.ThemeManager;
import factory.ui.UIFactory;
import bridge.notification.*;

/**
 * GUI Window for submitting item claims
 */
public class ClaimSubmissionWindow extends JFrame {

    private int selectedItemID;
    private JTextField txtStudentID;
    private JLabel lblItemInfo;

    public ClaimSubmissionWindow(int selectedItemID) {
        this.selectedItemID = selectedItemID;
        setTitle("Submit Claim");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        UIFactory factory = ThemeManager.getInstance().getFactory();
        getContentPane().setBackground(factory.getBackgroundColor());
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        mainPanel.setBackground(factory.getBackgroundColor());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblItemInfo = factory.createLabel("Claiming Item ID: " + selectedItemID);
        lblItemInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblItemInfo.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        idPanel.setBackground(factory.getBackgroundColor());
        idPanel.add(factory.createLabel("Student ID:"));
        
        // Default to current user's ID if available
        int currentUserId = SessionManager.getInstance().getUserId();
        txtStudentID = factory.createTextField(15);
        if (currentUserId > 0) {
            txtStudentID.setText(String.valueOf(currentUserId));
        }
        idPanel.add(txtStudentID);

        JButton btnSubmit = factory.createButton("Submit Claim");
        btnSubmit.addActionListener(e -> onInitiateClaim());

        mainPanel.add(lblItemInfo);
        mainPanel.add(idPanel);
        mainPanel.add(btnSubmit);

        add(mainPanel, BorderLayout.CENTER);
    }

    public void onInitiateClaim() {
        String studentIDText = txtStudentID.getText().trim();

        if (studentIDText.isEmpty()) {
            showErrorMessage("Please enter your Student ID.");
            return;
        }

        int studentID;
        try {
            studentID = Integer.parseInt(studentIDText);
        } catch (NumberFormatException e) {
            showErrorMessage("Student ID must be numeric.");
            return;
        }

        controller.ClaimController claimController = new controller.ClaimController();

        try {
            claimController.startClaimProcess(selectedItemID, studentID);

            // Trigger Notification (Bridge Pattern)
            NotificationSender sender = new InAppSender();
            Notification notification = new ClaimNotification(sender);
            notification.notify(SessionManager.getInstance().getUserId(), 
                "New claim submitted for Item #" + selectedItemID);

            showSuccessMessage("Claim submitted successfully!\nOur team will review your request.");
            dispose();
        } catch (Exception e) {
            showErrorMessage("Failed to submit claim: " + e.getMessage());
        }
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}