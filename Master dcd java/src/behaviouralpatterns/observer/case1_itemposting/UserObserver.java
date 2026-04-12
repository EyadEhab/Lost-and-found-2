package behaviouralpatterns.observer.case1_itemposting;

import entity.Item;
import entity.User;

/**
 * Observer Pattern — Concrete Observer
 *
 * Wraps the real entity.User with subscription preferences so that it
 * can act as an Observer in the notification system.
 *
 * WHY WRAP instead of modifying entity.User directly?
 *   entity.User is a database entity (maps to the DB USER table).
 *   Adding observer logic to it would mix two concerns: data persistence
 *   and event handling. Wrapping keeps each class focused on ONE job
 *   (Single Responsibility Principle).
 *
 * HOW IT INTEGRATES:
 *   A UserObserver is created when a user "subscribes" to alerts
 *   (e.g., by selecting a category/location on the search page).
 *   It is registered with NotificationManager.getInstance().subscribe().
 *   When ItemController posts a new item, NotificationManager calls
 *   update(Item) on every registered UserObserver.
 *
 * FILTERING LOGIC:
 *   The user is notified ONLY if the new item matches either:
 *     • their subscribed CATEGORY (e.g., "Electronics")
 *     • their subscribed LOCATION (partial match, e.g., "Library")
 *     • their subscribed KEYWORD  (found in title, description, or tags)
 *
 * Role: Concrete Observer
 * Integrates with: entity.User, entity.Item
 */
public class UserObserver implements ItemObserver {

    // ── The real system User this observer represents ─────────────────────────
    private final User user;

    // ── Subscription preferences set by the user ──────────────────────────────
    private final String subscribedCategory;   // e.g. "Electronics"
    private final String subscribedLocation;   // e.g. "Library"
    private final String subscribedKeyword;    // e.g. "phone", "wallet"

    /**
     * Creates a UserObserver for a given user with their notification preferences.
     *
     * @param user               the actual User entity (has userID and email)
     * @param subscribedCategory the item category the user is interested in
     * @param subscribedLocation the location the user is watching
     * @param subscribedKeyword  a keyword to match against item title/description/tags
     */
    public UserObserver(User user, String subscribedCategory,
                        String subscribedLocation, String subscribedKeyword) {
        this.user               = user;
        this.subscribedCategory = subscribedCategory != null ? subscribedCategory : "";
        this.subscribedLocation = subscribedLocation != null ? subscribedLocation : "";
        this.subscribedKeyword  = subscribedKeyword  != null ? subscribedKeyword.toLowerCase() : "";
    }

    /**
     * Called by NotificationManager when a new item is posted.
     *
     * Checks if the item matches this user's preferences.
     * If YES → prints a notification (simulates email/in-app alert).
     * If NO  → silently ignored (no output, no performance cost).
     */
    @Override
    public void update(Item postedItem) {

        // ── Match checks ──────────────────────────────────────────────────────

        // 1. Category match (case-insensitive)
        boolean categoryMatch = !subscribedCategory.isEmpty()
                && subscribedCategory.equalsIgnoreCase(postedItem.getCategory());

        // 2. Location match (partial, case-insensitive)
        boolean locationMatch = !subscribedLocation.isEmpty()
                && postedItem.getLocation() != null
                && postedItem.getLocation().toLowerCase().contains(subscribedLocation.toLowerCase());

        // 3. Keyword match — checks title, description, and tags
        boolean keywordMatch = false;
        if (!subscribedKeyword.isEmpty()) {
            String title       = postedItem.getTitle()       != null ? postedItem.getTitle().toLowerCase()       : "";
            String description = postedItem.getDescription() != null ? postedItem.getDescription().toLowerCase() : "";
            String tags        = postedItem.getTags()        != null ? postedItem.getTags().toLowerCase()        : "";
            keywordMatch = title.contains(subscribedKeyword)
                        || description.contains(subscribedKeyword)
                        || tags.contains(subscribedKeyword);
        }

        // ── Notify only if at least one criterion matches ─────────────────────
        if (categoryMatch || locationMatch || keywordMatch) {
            System.out.println("  ✔ [NOTIFICATION]"
                    + " → User #" + user.getUserID()
                    + " (" + user.getEmail() + ")"
                    + "\n      A new item matches your subscription!"
                    + "\n      Item #" + postedItem.getItemID()
                    + " | Title: \""    + postedItem.getTitle() + "\""
                    + " | Category: "   + postedItem.getCategory()
                    + " | Location: "   + postedItem.getLocation()
                    + " | Status: "     + postedItem.getStatus()
                    + "\n      Matched on: "
                    + (categoryMatch ? "[Category=" + subscribedCategory + "] " : "")
                    + (locationMatch ? "[Location=" + subscribedLocation + "] " : "")
                    + (keywordMatch  ? "[Keyword="  + subscribedKeyword  + "]"  : ""));
        }
        // else: item does not match this user's interests → silent, no output
    }

    // ── Getters for display / testing ─────────────────────────────────────────

    public User   getUser()               { return user; }
    public String getSubscribedCategory() { return subscribedCategory; }
    public String getSubscribedLocation() { return subscribedLocation; }
    public String getSubscribedKeyword()  { return subscribedKeyword; }

    @Override
    public String toString() {
        return "UserObserver{userID=" + user.getUserID()
                + ", email=" + user.getEmail()
                + ", category=" + subscribedCategory
                + ", location=" + subscribedLocation
                + ", keyword="  + subscribedKeyword + "}";
    }
}
