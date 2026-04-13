package Boundary;

import javax.swing.JOptionPane;

/**
 *
 */
public class HandoverWindow {

    /**
     * Default constructor
     */
    public HandoverWindow() {
    }

    /**
     *
     */
    private int studentIDInput;

    // Getters and Setters
    public int getStudentIDInput() {
        return studentIDInput;
    }

    public void setStudentIDInput(int studentIDInput) {
        this.studentIDInput = studentIDInput;
    }


    /**
     * @return
     */
    public void showHandoverSuccess() {
        JOptionPane.showMessageDialog(null,
                "Handover completed for Student ID: " + studentIDInput,
                "Handover Successful",
                JOptionPane.INFORMATION_MESSAGE);
    }

}