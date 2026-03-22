package bridge.notification;

import core.NotificationManager;
import entity.NotificationRecord;

public class EmailSender implements NotificationSender {
    @Override
    public void send(int userId, String message) {
        NotificationManager.getInstance().addNotification(new NotificationRecord(userId, message, "Email"));
        System.out.println("[EMAIL] Simulated email sent to user " + userId + ": " + message);
    }
}
