package notification;

import javax.swing.JOptionPane;

/**
 * Concrete implementor: in-app delivery (simple dialog; for coursework demo).
 */
public class InAppNotificationSender implements NotificationSender {

    @Override
    public void send(String message) {
        JOptionPane.showMessageDialog(null, message, "In-app notification", JOptionPane.INFORMATION_MESSAGE);
    }
}
