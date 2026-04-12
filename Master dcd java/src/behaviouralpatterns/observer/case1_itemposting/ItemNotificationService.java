package behaviouralpatterns.observer.case1_itemposting;

import entity.User;
import core.SessionManager;

/**
 * Observer + Singleton Pattern — Item Notification Service
 *
 * This is a REAL service class used inside the Lost & Found system.
 * It is NOT a demo — it integrates directly with SessionManager,
 * entity.User, and the NotificationManager Singleton.
 *
 * ─────────────────────────────────────────────────────────────────
 *  HOW IT FITS IN THE APPLICATION:
 *
 *  1. When a logged-in user opens SearchWindow and enters search
 *     filters (category, location, keyword), the UI calls:
 *       ItemNotificationService.subscribeCurrentUser(...)
 *     to register them as an observer.
 *
 *  2. When any officer posts a new item via ItemController,
 *     ItemController automatically calls:
 *       NotificationManager.getInstance().notifyObservers(item)
 *     — which triggers every subscribed UserObserver.
 *
 *  3. When the user claims their item or closes the window, the UI
 *     calls:
 *       ItemNotificationService.unsubscribeCurrentUser()
 *     to remove them from the observer list.
 *
 * ─────────────────────────────────────────────────────────────────
 *  CALLED FROM:
 *    • Boundary.SearchWindow  — subscribe on search, unsubscribe on claim
 *    • Boundary.UploadForm    — (via ItemController) fires notifications
 *
 *  DEPENDS ON:
 *    • core.SessionManager    — reads the currently logged-in user
 *    • NotificationManager    — Singleton subject
 *    • UserObserver           — concrete observer wrapping entity.User
 *
 * Role: Application Service (entry point for Observer registration)
 */
public class ItemNotificationService {

    /**
     * The user currently registered for notifications.
     */
    private User activeUser;

    // ─── Subscribe ────────────────────────────────────────────────────────────

    /**
     * Subscribes the currently logged-in user (from SessionManager) to
     * item notifications that match the given preferences.
     *
     * Registers the User entity itself as an Observer with the core.NotificationManager.
     */
    public void subscribeCurrentUser(String preferredCategory,
                                     String preferredLocation,
                                     String keyword) {

        // 1. First unsubscribe any previous registration
        unsubscribeCurrentUser();

        // 2. Load/Build the current user from session
        activeUser = new User();
        activeUser.setUserID(SessionManager.getInstance().getUserId());
        activeUser.setEmail(SessionManager.getInstance().getUsername());

        // 3. Set the observer notification preferences directly on the entity
        activeUser.setSubscribedCategory(preferredCategory);
        activeUser.setSubscribedLocation(preferredLocation);
        activeUser.setSubscribedKeyword(keyword);

        // 4. Register with the core Singleton subject
        core.NotificationManager.getInstance().subscribe(activeUser);
    }

    // ─── Unsubscribe ──────────────────────────────────────────────────────────

    public void unsubscribeCurrentUser() {
        if (activeUser != null) {
            core.NotificationManager.getInstance().unsubscribe(activeUser);
            activeUser = null;
        }
    }

    // ─── Status ───────────────────────────────────────────────────────────────

    /**
     * Returns true if the current user has an active item subscription.
     */
    public boolean isSubscribed() {
        return activeUser != null;
    }

    /**
     * Returns the active user observer.
     */
    public User getActiveUser() {
        return activeUser;
    }

    // ─── Internal helper ──────────────────────────────────────────────────────

    /**
     * Converts the role string from SessionManager into the integer roleID
     * used by entity.User (matching the DB schema in UserDataAccess).
     *
     * Mapping: "Student" → 2 | "Officer" → 1 | "Admin" → 3 | default → 0
     */
    private int resolveRoleID(String role) {
        if (role == null) return 0;
        switch (role) {
            case "Officer": return 1;
            case "Student": return 2;
            case "Admin":   return 3;
            default:        return 0;
        }
    }
}
