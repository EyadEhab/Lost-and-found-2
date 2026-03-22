package notification;

/**
 * Bridge abstraction: notification *type*; delivery is delegated to NotificationSender.
 */
public abstract class Notification {

    protected final NotificationSender sender;

    protected Notification(NotificationSender sender) {
        this.sender = sender;
    }

    /** Each notification type builds its own message text. */
    public abstract String buildMessage();

    /** Sends using the chosen channel (implementor). */
    public void deliver() {
        sender.send(buildMessage());
    }
}
