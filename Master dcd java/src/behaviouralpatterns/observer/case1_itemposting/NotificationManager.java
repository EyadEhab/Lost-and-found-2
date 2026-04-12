package behaviouralpatterns.observer.case1_itemposting;

import entity.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern — Subject (Observable)
 * Singleton Pattern  — One shared instance for the entire system
 *
 * ┌─────────────────────────────────────────────────────────────┐
 * │  WHY SINGLETON?                                             │
 * │  ItemController, ClaimController, and any future service    │
 * │  all post items to the SAME system. If each held a separate │
 * │  NotificationManager, observers registered in one instance  │
 * │  would never receive events fired through another.          │
 * │  → One hub, one registry, zero missed notifications.        │
 * └─────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────┐
 * │  WHY SUBJECT?                                               │
 * │  It holds the list of all subscribed UserObservers and      │
 * │  broadcasts a notification (the real Item object) to each   │
 * │  one when ItemController posts a new item.                  │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Integration point:
 *   Called inside controller.ItemController.registerNewItem()
 *   after a successful dao.saveItem() call.
 *
 * Role: Singleton + Subject
 */
public class NotificationManager {

    // =========================================================================
    // SINGLETON — private constructor + static getInstance()
    // =========================================================================

    /** The one and only instance. */
    private static NotificationManager instance;

    /**
     * Private constructor — nobody outside can call new NotificationManager().
     */
    private NotificationManager() {
        this.observers = new ArrayList<>();
    }

    /**
     * Returns the single shared NotificationManager.
     * Thread-safe via double-checked locking.
     *
     * @return the singleton instance
     */
    public static NotificationManager getInstance() {
        if (instance == null) {
            synchronized (NotificationManager.class) {
                if (instance == null) {
                    instance = new NotificationManager();
                }
            }
        }
        return instance;
    }

    // =========================================================================
    // OBSERVER REGISTRY — subscribe / unsubscribe
    // =========================================================================

    /** All currently subscribed user observers. */
    private final List<ItemObserver> observers;

    /**
     * Registers a user (observer) to receive notifications for new items.
     * Duplicate registrations are ignored.
     *
     * @param observer the UserObserver to register
     */
    public void subscribe(ItemObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[NotificationManager] Subscribed: " + observer);
        }
    }

    /**
     * Removes a user (observer) — they will no longer receive notifications.
     * Called when a user claims their item or opts out.
     *
     * @param observer the UserObserver to remove
     */
    public void unsubscribe(ItemObserver observer) {
        if (observers.remove(observer)) {
            System.out.println("[NotificationManager] Unsubscribed: " + observer);
        }
    }

    /** Returns the number of currently subscribed observers (useful for testing). */
    public int getObserverCount() {
        return observers.size();
    }

    // =========================================================================
    // BROADCAST — called by ItemController after a successful item save
    // =========================================================================

    /**
     * Broadcasts the newly posted Item to ALL subscribed observers.
     *
     * Each observer independently checks whether the item matches its
     * own preferences (category / location / keyword). If it matches,
     * the observer prints / sends the notification.
     *
     * This method is intentionally simple — it does not filter here.
     * Filtering responsibility belongs to each concrete observer (Single
     * Responsibility Principle).
     *
     * @param postedItem the real entity.Item that was just saved to the DB
     */
    public void notifyObservers(Item postedItem) {
        System.out.println("\n[NotificationManager] New item posted (ID=" + postedItem.getItemID()
                + ", Title=\"" + postedItem.getTitle() + "\")."
                + " Notifying " + observers.size() + " subscriber(s)...");

        for (ItemObserver observer : observers) {
            observer.update(postedItem);   // pass the real Item entity
        }
    }
}
