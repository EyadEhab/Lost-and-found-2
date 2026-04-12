package controller;

import factory.dao.DataAccessFactory;
import factory.dao.SqlDataAccessFactory;
import bridge.notification.*;
import core.SessionManager;
// Observer + Singleton integration
import core.NotificationManager;

/**
 *
 */
public class ItemController {

    /**
     * Factory used to obtain DAOs for this controller.
     * Allows swapping in a different DataAccessFactory (e.g., a mock) if needed.
     */
    private final DataAccessFactory dataFactory;

    /**
     * Default constructor uses the concrete SQL factory.
     */
    public ItemController() {
        this(new SqlDataAccessFactory());
    }

    /**
     * Overloaded constructor to allow injecting a custom DataAccessFactory.
     */
    public ItemController(DataAccessFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    /**
     *
     */
    private int activeItemID;

    // Getters and Setters
    public int getActiveItemID() {
        return activeItemID;
    }

    public void setActiveItemID(int activeItemID) {
        this.activeItemID = activeItemID;
    }







    /**
     * @param id
     * @return
     */
    public entity.Item getItemDetails(int id) {
        // TODO implement here
        return null;
    }

    /**
     * @param id 
     * @return
     */
    public boolean deleteItem(int id) {
        // TODO implement here
        return false;
    }

    /**
     * Register a new found item
     * @param title the item title (required)
     * @param description the item description (required)
     * @param category the item category
     * @param location the location where item was found
     * @param photoPath the path to the item photo (required)
     * @param dateFound the date the item was found
     * @return the new item ID if successful, 0 if validation fails, -1 if database error
     */
    public int registerNewItem(String title, String description, String category,
                              String location, String photoPath, java.util.Date dateFound) {
        // Validate required fields
        if (title == null || title.trim().isEmpty()) {
            return 0; // Validation failed - title required
        }
        if (description == null || description.trim().isEmpty()) {
            return 0; // Validation failed - description required
        }
        if (photoPath == null || photoPath.trim().isEmpty()) {
            return 0; // Validation failed - photo required
        }

        // Create new Item entity
        entity.Item newItem = new entity.Item();
        newItem.setTitle(title.trim());
        newItem.setDescription(description.trim());
        newItem.setCategory(category != null ? category : "Personal Items");
        newItem.setLocation(location != null ? location.trim() : "");
        newItem.setPhotoPath(photoPath);
        newItem.setDateFound(dateFound != null ? dateFound : new java.util.Date());
        // Must match FOUND_ITEM.Status CHECK — allowed values: 'Found', 'Processing Claim', 'Collected', 'Archived'
        newItem.setStatus("Found");

        // Set officer information from the active session
        int sessionUserId = SessionManager.getInstance().getUserId();
        newItem.setOfficerID(sessionUserId > 0 ? sessionUserId : 1);
        String sessionUsername = SessionManager.getInstance().getUsername();
        newItem.setOfficerName(sessionUsername != null && !sessionUsername.isEmpty() ? sessionUsername : "Officer");

        // Save to database via DAO obtained from the configured factory
        try {
            DAO.ItemDataAccess dao = dataFactory.createItemDAO();
            int newItemId = dao.saveItem(newItem);

            if (newItemId > 0) {
                // ── Bridge Pattern: in-app notification to the uploading officer ──
                try {
                    NotificationSender sender = new InAppSender();
                    Notification notification = new ItemStatusNotification(sender);
                    notification.notify(SessionManager.getInstance().getUserId(),
                            "Successfully uploaded new item: " + title);
                } catch (Exception ex) {
                    System.err.println("Upload saved; Bridge notification skipped: " + ex.getMessage());
                }

                // ── Observer Pattern: broadcast to all subscribed users ──────────
                try {
                    newItem.setItemID(newItemId); // Needs ID for observers
                    
                    // Triggering NotificationManager Singleton here:
                    core.NotificationManager.getInstance().notifyObservers(newItem);
                    
                } catch (Exception ex) {
                    System.err.println("Observer notification skipped: " + ex.getMessage());
                }
                // ────────────────────────────────────────────────────────────────

                return newItemId;
            } else {
                System.err.println("Failed to save item to database: Invalid response from DAO");
                return -1; // Database error
            }
        } catch (Exception e) {
            System.err.println("Database error while saving item: " + e.getMessage());
            e.printStackTrace();
            return -1; // Database error
        }
    }

    /**
     * @param id 
     * @param data 
     * @return
     */
    public boolean modifyItem(int id, Object data) {
        // TODO implement here
        return false;
    }

    /**
     * @param desc 
     * @param loc 
     * @return
     */
    public boolean validateItem(String desc, String loc) {
        // TODO implement here
        return false;
    }

}