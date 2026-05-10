package notification;

/**
 * Safe, optional demo of the Notification Bridge (not used by core workflows).
 * Kept in this package so sender types resolve with the same compilation unit as the Bridge classes.
 */
public final class NotificationBridgeDemo {

    private NotificationBridgeDemo() {
    }

    /** Runs three notification types over three channels; email/SMS log to console; in-app uses a simple dialog. */
    public static void runConsoleDemo() {
        NotificationSender email = new EmailNotificationSender();
        NotificationSender sms = new SmsNotificationSender();
        NotificationSender inApp = new InAppNotificationSender();

        new ClaimNotification(email, 101, "Blue umbrella", "student@example.edu").deliver();
        new ItemStatusNotification(sms, "Water bottle", "Collected").deliver();
        new GeneralNotification(inApp, "Lost & Found", "Remember to label your items.").deliver();
    }
}
