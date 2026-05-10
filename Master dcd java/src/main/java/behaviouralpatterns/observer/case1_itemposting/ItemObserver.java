package behaviouralpatterns.observer.case1_itemposting;

import entity.Item;

/**
 * Observer Pattern — Observer Interface
 *
 * Any class that wants to be notified when a new item is posted
 * in the Lost & Found system must implement this interface.
 *
 * The update() method receives the actual entity.Item that was just
 * posted — so observers can inspect title, category, location, tags, etc.
 *
 * Role: Observer (interface)
 *
 * Integrated with: entity.Item (real project entity)
 */
public interface ItemObserver {

    /**
     * Called automatically by NotificationManager whenever a new item
     * is registered in the system (via ItemController.registerNewItem).
     *
     * @param postedItem the actual Item entity that was just saved to the DB
     */
    void update(Item postedItem);
}
