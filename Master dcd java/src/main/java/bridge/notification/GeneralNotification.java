package bridge.notification;

public class GeneralNotification extends Notification {
    public GeneralNotification(NotificationSender sender) {
        super(sender);
    }

    @Override
    public void notify(int userId, String message) {
        sender.send(userId, "System Update: " + message);
    }
}
