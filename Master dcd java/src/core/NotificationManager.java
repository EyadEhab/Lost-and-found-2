package core;

import entity.Item;
import entity.NotificationRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton manager for storing and retrieving notifications in memory.
 * Observer Pattern — Subject (Observable)
 */
public class NotificationManager {
    private static NotificationManager instance;
    private final List<NotificationRecord> notifications;
    
    // Observer pattern registry
    private final List<Observer> observers;

    private NotificationManager() {
        notifications = java.util.Collections.synchronizedList(new ArrayList<>());
        observers = java.util.Collections.synchronizedList(new ArrayList<>());
    }

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    // --- Observer Pattern Implementation ---
    
    public synchronized void subscribe(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public synchronized void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Broadcasts the newly posted Item to ALL subscribed observers.
     */
    public void notifyObservers(Item postedItem) {
        System.out.println("[NotificationManager] System broadcasting new item posting: " + postedItem.getTitle());
        for (Observer observer : observers) {
            observer.update(postedItem);
        }
    }

    // --- Original NotificationRecord Implementation ---

    public void addNotification(NotificationRecord record) {
        notifications.add(record);
    }

    public List<NotificationRecord> getNotificationsForUser(int userId) {
        return notifications.stream()
                .filter(n -> n.getUserId() == userId)
                .collect(Collectors.toList());
    }
}
