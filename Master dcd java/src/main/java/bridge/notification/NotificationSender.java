package bridge.notification;

/**
 * Implementor interface for the Bridge pattern.
 */
public interface NotificationSender {
    void send(int userId, String message);
}
