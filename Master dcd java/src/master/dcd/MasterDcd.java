/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package master.dcd;

import Boundary.UploadForm;
import Boundary.ClaimSubmissionWindow;

/**
 * Main application class for Lost and Found System
 * 
 * @author Yassin
 */
public class MasterDcd {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Application started.");
        System.out.println("Classpath: " + System.getProperty("java.class.path"));

        // Launch Login Window
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Boundary.LoginWindow().setVisible(true);
            }
        });
    }

}
