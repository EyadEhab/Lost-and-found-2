package bridge.notification;

import core.NotificationManager;
import entity.NotificationRecord;

public class SmsSender implements NotificationSender {
    @Override
    public void send(int userId, String message) {
        NotificationManager.getInstance().addNotification(new NotificationRecord(userId, message, "SMS"));
        System.out.println("[SMS] Simulated SMS sent to user " + userId + ": " + message);
    }
}
