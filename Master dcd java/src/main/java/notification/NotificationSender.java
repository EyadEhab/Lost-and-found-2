package notification;

/**
 * Bridge implementor: how a notification is delivered (email, SMS, in-app, etc.).
 */
public interface NotificationSender {

    void send(String message);
}
