package behaviouralpatterns.observer.case1_itemposting;

import entity.Item;
import entity.NotificationRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Observer Pattern — Subject (Observable)
 * Singleton Pattern  — One shared instance for the entire system
 *
 * This version now handles BOTH:
 *  1. Live Observer broadcasting (Observer Pattern)
 *  2. Notification history storage (in-memory persistence)
 *
 * Role: Singleton + Subject
 */
public class NotificationManager {

    // =========================================================================
    // SINGLETON — private constructor + static getInstance()
    // =========================================================================

    private static NotificationManager instance;

    /**
     * Private constructor. Initializes thread-safe lists.
     */
    private NotificationManager() {
        this.observers = java.util.Collections.synchronizedList(new ArrayList<>());
        this.notifications = java.util.Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Returns the single shared NotificationManager.
     * @return the singleton instance
     */
    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    // =========================================================================
    // STATE — Observers and History
    // =========================================================================

    /** All currently subscribed observers (e.g. UserObserver). */
    private final List<ItemObserver> observers;

    /** In-memory storage for notification history records. */
    private final List<NotificationRecord> notifications;

    // =========================================================================
    // OBSERVER REGISTRY — subscribe / unsubscribe
    // =========================================================================

    public synchronized void subscribe(ItemObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[NotificationManager] Subscribed: " + observer);
        }
    }

    public synchronized void unsubscribe(ItemObserver observer) {
        if (observers.remove(observer)) {
            System.out.println("[NotificationManager] Unsubscribed: " + observer);
        }
    }

    // =========================================================================
    // HISTORY MANAGEMENT
    // =========================================================================

    /**
     * Adds a record to the system-wide notification history.
     * Called by ConcreteObservers when they find a match.
     */
    public void addNotificationRecord(NotificationRecord record) {
        notifications.add(record);
    }

    /**
     * Retrieves all notifications belonging to a specific user.
     */
    public List<NotificationRecord> getNotificationsForUser(int userId) {
        synchronized (notifications) {
            return notifications.stream()
                    .filter(n -> n.getUserId() == userId)
                    .collect(Collectors.toList());
        }
    }

    // =========================================================================
    // BROADCAST
    // =========================================================================

    /**
     * Broadcasts the newly posted Item to ALL subscribed observers.
     */
    public void notifyObservers(Item postedItem) {
        System.out.println("\n[NotificationManager] System broadcasting new item posting: " + postedItem.getTitle()
                + " (ID=" + postedItem.getItemID() + ")");

        // Defensive copy to avoid ConcurrentModificationException if someone unsubscribes during loop
        List<ItemObserver> currentObservers;
        synchronized (observers) {
            currentObservers = new ArrayList<>(observers);
        }

        for (ItemObserver observer : currentObservers) {
            observer.update(postedItem);
        }
    }
}

