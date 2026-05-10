package bridge.notification;

/**
 * Abstraction in the Bridge pattern.
 */
public abstract class Notification {
    protected NotificationSender sender;

    public Notification(NotificationSender sender) {
        this.sender = sender;
    }

    public abstract void notify(int userId, String message);
}
