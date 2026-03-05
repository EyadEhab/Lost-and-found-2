package entity;

import java.util.*;
import java.awt.Image;

/**
 * 
 */
public class Item {

    /**
     * Default constructor
     */
    public Item() {
    }

    /**
     * 
     */
    private int itemID;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Image photo;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private Date dateFound;

    /**
     *
     */
    private String tags;

    /**
     *
     */
    private String category;

    /**
     * Item title
     */
    private String title;

    /**
     * Location where item was found
     */
    private String location;

    /**
     * Path to the item photo
     */
    private String photoPath;

    /**
     * Officer who logged the item
     */
    private String officerName;

    /**
     * Officer ID who logged the item
     */
    private int officerID;

    // Getters
    public int getItemID() {
        return itemID;
    }

    public String getDescription() {
        return description;
    }

    public Image getPhoto() {
        return photo;
    }

    public String getStatus() {
        return status;
    }

    public Date getDateFound() {
        return dateFound;
    }

    public String getTags() {
        return tags;
    }

    public String getCategory() {
        return category;
    }

    // Setters
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDateFound(Date dateFound) {
        this.dateFound = dateFound;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public int getOfficerID() {
        return officerID;
    }

    public void setOfficerID(int officerID) {
        this.officerID = officerID;
    }

    /**
     * @param desc
     * @param loc
     */
    public void updateItemDetails(String desc, String loc) {
        // TODO implement here
    }

    /**
     * @param keywords
     * @return
     */
    public int matchScore(String keywords) {
        // TODO implement here
        return 0;
    }

    /**
     * Marks the item as reserved
     */
    public void markAsReserved() {
        // TODO implement here
    }

}