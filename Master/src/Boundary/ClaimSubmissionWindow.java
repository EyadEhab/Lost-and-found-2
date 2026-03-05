package Boundary;

import javax.swing.JOptionPane;

/**
 * GUI Window for submitting item claims
 */
public class ClaimSubmissionWindow {

    // GUI Components (assuming these exist in the actual GUI)
    // private javax.swing.JTextField txtStudentID;
    // private javax.swing.JButton btnSubmitClaim;
    // private javax.swing.JLabel lblSelectedItem;

    /**
     * The ID of the item being claimed (set when user selects an item)
     */
    private int selectedItemID;

    /**
     * Default constructor
     */
    public ClaimSubmissionWindow() {
        this.selectedItemID = 0; // No item selected initially
    }

    /**
     * Constructor with selected item
     * @param selectedItemID the ID of the item to claim
     */
    public ClaimSubmissionWindow(int selectedItemID) {
        this.selectedItemID = selectedItemID;
    }

    // Getters and Setters
    public int getSelectedItemID() {
        return selectedItemID;
    }

    public void setSelectedItemID(int selectedItemID) {
        this.selectedItemID = selectedItemID;
    }


    /**
     * Action listener for the Submit Claim button
     * Captures studentID from GUI and uses selectedItemID for the claim
     */
    public void onInitiateClaim() {
        // Capture studentID from GUI component
        String studentIDText = ""; // txtStudentID.getText();

        // TODO: Uncomment when GUI components are available
        // String studentIDText = txtStudentID.getText();

        // For demo purposes, use sample data
        studentIDText = "12345";
        selectedItemID = 1; // Assume item 1 is selected

        // Parse student ID
        int studentID;
        try {
            studentID = Integer.parseInt(studentIDText.trim());
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter a valid Student ID (numeric only).");
            return;
        }

        // Validate input
        if (selectedItemID <= 0) {
            showErrorMessage("Please select an item to claim first.");
            return;
        }

        if (studentID <= 0) {
            showErrorMessage("Please enter a valid Student ID.");
            return;
        }

        // Create controller instance
        controller.ClaimController claimController = new controller.ClaimController();

        // Call controller to start the claim process
        try {
            claimController.startClaimProcess(selectedItemID, studentID);

            // Success - show confirmation message and reset form
            showSuccessMessage("Claim submitted successfully!\nItem ID: " + selectedItemID + "\nStudent ID: " + studentID + "\n\nYou will be notified of the claim status.");

            // TODO: Clear form and navigate to claim status
            // txtStudentID.setText("");
            // selectedItemID = 0;

        } catch (RuntimeException e) {
            // Controller threw an exception (database error)
            System.err.println("Failed to submit claim: " + e.getMessage());
            showErrorMessage("Failed to submit claim due to database error.\nPlease try again later.");
        } catch (Exception e) {
            // Unexpected error
            System.err.println("Unexpected error during claim submission: " + e.getMessage());
            showErrorMessage("An unexpected error occurred.\nPlease try again.");
        }
    }

    /**
     * Displays a success message to the user
     * @param message the success message to display
     */
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays an error message to the user
     * @param message the error message to display
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}