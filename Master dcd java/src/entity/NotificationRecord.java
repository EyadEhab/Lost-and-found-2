package entity;

import java.util.Date;

/**
 * Model representing a single notification record.
 */
public class NotificationRecord {
    private int userId;
    private String message;
    private String channel;
    private Date timestamp;

    public NotificationRecord(int userId, String message, String channel) {
        this.userId = userId;
        this.message = message;
        this.channel = channel;
        this.timestamp = new Date();
    }

    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public String getChannel() { return channel; }
    public Date getTimestamp() { return timestamp; }
}
