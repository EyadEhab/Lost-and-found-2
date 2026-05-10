package bridge.notification;

public class ClaimNotification extends Notification {
    public ClaimNotification(NotificationSender sender) {
        super(sender);
    }

    @Override
    public void notify(int userId, String message) {
        sender.send(userId, "Claim Alert: " + message);
    }
}
