package core;

import entity.NotificationRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton manager for storing and retrieving notifications in memory.
 */
public class NotificationManager {
    private static NotificationManager instance;
    private final List<NotificationRecord> notifications;

    private NotificationManager() {
        notifications = new ArrayList<>();
    }

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void addNotification(NotificationRecord record) {
        notifications.add(record);
    }

    public List<NotificationRecord> getNotificationsForUser(int userId) {
        return notifications.stream()
                .filter(n -> n.getUserId() == userId)
                .collect(Collectors.toList());
    }
}
