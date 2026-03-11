package controller;

import factory.dao.DataAccessFactory;
import factory.dao.SqlDataAccessFactory;

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
        newItem.setStatus("Found"); // Default status for uploaded items

        // Set officer information (placeholders for now)
        newItem.setOfficerID(1); // TODO: Get from current logged-in officer
        newItem.setOfficerName("Admin Officer"); // TODO: Get from current logged-in officer

        // Save to database via DAO obtained from the configured factory
        try {
            DAO.ItemDataAccess dao = dataFactory.createItemDAO();
            int newItemId = dao.saveItem(newItem);

            if (newItemId > 0) {
                return newItemId; // Success
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