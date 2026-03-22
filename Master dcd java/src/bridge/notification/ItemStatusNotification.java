package bridge.notification;

public class ItemStatusNotification extends Notification {
    public ItemStatusNotification(NotificationSender sender) {
        super(sender);
    }

    @Override
    public void notify(int userId, String message) {
        sender.send(userId, "Item Status Update: " + message);
    }
}
