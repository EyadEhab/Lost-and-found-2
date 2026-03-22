package notification;

/**
 * Concrete implementor: simulated SMS delivery (no carrier API).
 */
public class SmsNotificationSender implements NotificationSender {

    @Override
    public void send(String message) {
        System.out.println("[SMS] " + message);
    }
}
