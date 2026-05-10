package bridge.notification;

import behaviouralpatterns.observer.case1_itemposting.NotificationManager;
import entity.NotificationRecord;

public class SmsSender implements NotificationSender {
    @Override
    public void send(int userId, String message) {
        NotificationManager.getInstance().addNotificationRecord(new NotificationRecord(userId, message, "SMS"));
        System.out.println("[SMS] Simulated SMS sent to user " + userId + ": " + message);
    }
}
