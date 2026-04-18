package behaviouralpatterns.observer.case1_itemposting;

import entity.User;
import core.SessionManager;

/**
 * Observer + Singleton Pattern — Item Notification Service
 *
 * This is the refined service that correctly integrates the Observer Pattern.
 * It uses the UserObserver wrapper to keep entity.User clean and focused.
 */
public class ItemNotificationService {

    /**
     * The active observer wrapper for the current session user.
     */
    private ItemObserver activeObserver;

    // ─── Subscribe ────────────────────────────────────────────────────────────

    /**
     * Subscribes the currently logged-in user to notifications.
     */
    public void subscribeCurrentUser(String preferredCategory,
                                     String preferredLocation,
                                     String keyword) {

        // 1. Clear any existing registration
        unsubscribeCurrentUser();

        // 2. Fetch basic user info from session
        User user = new User();
        user.setUserID(SessionManager.getInstance().getUserId());
        user.setEmail(SessionManager.getInstance().getUsername());

        // 3. Wrap User in a UserObserver (Separation of Concerns)
        activeObserver = new UserObserver(user, preferredCategory, preferredLocation, keyword);

        // 4. Register with the local Singleton subject
        NotificationManager.getInstance().subscribe(activeObserver);
    }

    // ─── Unsubscribe ──────────────────────────────────────────────────────────

    public void unsubscribeCurrentUser() {
        if (activeObserver != null) {
            NotificationManager.getInstance().unsubscribe(activeObserver);
            activeObserver = null;
        }
    }

    // ─── Status ───────────────────────────────────────────────────────────────

    public boolean isSubscribed() {
        return activeObserver != null;
    }

    public ItemObserver getActiveObserver() {
        return activeObserver;
    }
}

