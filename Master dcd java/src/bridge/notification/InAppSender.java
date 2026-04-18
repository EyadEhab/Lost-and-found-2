package bridge.notification;

import behaviouralpatterns.observer.case1_itemposting.NotificationManager;
import entity.NotificationRecord;

public class InAppSender implements NotificationSender {
    @Override
    public void send(int userId, String message) {
        NotificationManager.getInstance().addNotificationRecord(new NotificationRecord(userId, message, "In-App"));
        System.out.println("[IN-APP] Notification stored for user " + userId + ": " + message);
    }
}
