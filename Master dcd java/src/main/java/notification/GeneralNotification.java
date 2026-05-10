package notification;

/**
 * Refined abstraction: generic announcement-style message.
 */
public class GeneralNotification extends Notification {

    private final String topic;
    private final String text;

    public GeneralNotification(NotificationSender sender, String topic, String text) {
        super(sender);
        this.topic = topic;
        this.text = text;
    }

    @Override
    public String buildMessage() {
        return "[" + topic + "] " + text;
    }
}
