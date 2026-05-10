package notification;

/**
 * Refined abstraction: message when an item's status changes.
 */
public class ItemStatusNotification extends Notification {

    private final String itemTitle;
    private final String newStatus;

    public ItemStatusNotification(NotificationSender sender, String itemTitle, String newStatus) {
        super(sender);
        this.itemTitle = itemTitle;
        this.newStatus = newStatus;
    }

    @Override
    public String buildMessage() {
        return "Item \"" + itemTitle + "\" is now: " + newStatus + ".";
    }
}
