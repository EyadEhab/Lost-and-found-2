package entity;

import core.Observer;

/**
 * 
 */
public class User implements Observer {

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * 
     */
    private int userID;

    /**
     * 
     */
    private int roleID;

    /**
     * 
     */
    private String email;

    // --- Subscription Preferences for Observer Pattern ---
    private String subscribedCategory = "";
    private String subscribedLocation = "";
    private String subscribedKeyword = "";

    // Getters
    public int getUserID() {
        return userID;
    }

    public int getRoleID() {
        return roleID;
    }

    public String getEmail() {
        return email;
    }

    public String getSubscribedCategory() {
        return subscribedCategory;
    }

    public String getSubscribedLocation() {
        return subscribedLocation;
    }

    public String getSubscribedKeyword() {
        return subscribedKeyword;
    }

    // Setters
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSubscribedCategory(String subscribedCategory) {
        this.subscribedCategory = subscribedCategory != null ? subscribedCategory : "";
    }

    public void setSubscribedLocation(String subscribedLocation) {
        this.subscribedLocation = subscribedLocation != null ? subscribedLocation : "";
    }

    public void setSubscribedKeyword(String subscribedKeyword) {
        this.subscribedKeyword = subscribedKeyword != null ? aSubscribedKeywordToLowerCase(subscribedKeyword) : "";
    }
    
    private String aSubscribedKeywordToLowerCase(String kw) { return kw.toLowerCase(); }

    @Override
    public void update(Item postedItem) {
        // 1. Category match
        boolean categoryMatch = !subscribedCategory.isEmpty()
                && subscribedCategory.equalsIgnoreCase(postedItem.getCategory());

        // 2. Location match
        boolean locationMatch = !subscribedLocation.isEmpty()
                && postedItem.getLocation() != null
                && postedItem.getLocation().toLowerCase().contains(subscribedLocation.toLowerCase());

        // 3. Keyword match (checking title, description, tags)
        boolean keywordMatch = false;
        if (!subscribedKeyword.isEmpty()) {
            String title = postedItem.getTitle() != null ? postedItem.getTitle().toLowerCase() : "";
            String desc = postedItem.getDescription() != null ? postedItem.getDescription().toLowerCase() : "";
            String tags = postedItem.getTags() != null ? postedItem.getTags().toLowerCase() : "";
            keywordMatch = title.contains(subscribedKeyword)
                        || desc.contains(subscribedKeyword)
                        || tags.contains(subscribedKeyword);
        }

        // Notify if there is a match
        if (categoryMatch || locationMatch || keywordMatch) {
            String msg = "New matching item posted: " + postedItem.getTitle();
            System.out.println("[Notification] To: User #" + userID + " (" + email + ")");
            System.out.println("   -> " + msg);
            
            // Record the notification for history in the Singleton manager
            core.NotificationManager.getInstance().addNotification(
                new NotificationRecord(this.userID, msg, "In-App")
            );
        }
    }
}