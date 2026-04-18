package bridge.notification;

import behaviouralpatterns.observer.case1_itemposting.NotificationManager;
import entity.NotificationRecord;

public class EmailSender implements NotificationSender {
    @Override
    public void send(int userId, String message) {
        NotificationManager.getInstance().addNotificationRecord(new NotificationRecord(userId, message, "Email"));
        System.out.println("[EMAIL] Simulated email sent to user " + userId + ": " + message);
    }
}
