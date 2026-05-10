package Boundary;

import java.awt.Image;

/**
 *
 */
public class ItemDetailsWindow {

    /**
     * Default constructor
     */
    public ItemDetailsWindow() {
    }

    /**
     *
     */
    private Image displayImage;

    /**
     *
     */
    private String descriptionText;

    /**
     *
     */
    private String locationLabel;

    // Getters and Setters
    public Image getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(Image displayImage) {
        this.displayImage = displayImage;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }


    /**
     * @param itemID
     * @return
     */
    public void onSelect(int itemID) {
        // TODO implement here
    }

}