package notification;

/**
 * Refined abstraction: message content for a claim-related event.
 */
public class ClaimNotification extends Notification {

    private final int claimId;
    private final String itemTitle;
    private final String studentHint;

    public ClaimNotification(NotificationSender sender, int claimId, String itemTitle, String studentHint) {
        super(sender);
        this.claimId = claimId;
        this.itemTitle = itemTitle;
        this.studentHint = studentHint;
    }

    @Override
    public String buildMessage() {
        return "Claim #" + claimId + " for \"" + itemTitle + "\" (submitter: " + studentHint + ").";
    }
}
