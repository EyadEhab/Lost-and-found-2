package notification;

/**
 * Concrete implementor: simulated email delivery (no real mail API).
 */
public class EmailNotificationSender implements NotificationSender {

    @Override
    public void send(String message) {
        System.out.println("[EMAIL] " + message);
    }
}
