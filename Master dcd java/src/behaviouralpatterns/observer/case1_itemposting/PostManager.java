package behaviouralpatterns.observer.case1_itemposting;

/**
 * NOTE — PostManager is NOT used in the integrated implementation.
 *
 * In the previous (standalone) version, PostManager simulated the role
 * of the class that posts items and triggers notifications.
 *
 * In the INTEGRATED version, this responsibility belongs to:
 *   → controller.ItemController.registerNewItem()
 *
 * After dao.saveItem() succeeds, ItemController calls:
 *   NotificationManager.getInstance().notifyObservers(newItem);
 *
 * This means the Observer pattern is now wired directly into the
 * real application flow — no separate PostManager is needed.
 *
 * This class is kept only for reference / documentation.
 * See: controller/ItemController.java (lines ~112-135)
 */
public class PostManager {
    // Replaced by the integration in controller.ItemController.
    // See registerNewItem() for the actual Observer trigger point.
}
