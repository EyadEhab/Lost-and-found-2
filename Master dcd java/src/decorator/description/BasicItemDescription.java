package decorator.description;

import entity.Item;

/**
 * Concrete component for the Item Description.
 * Returns the basic item description (title).
 */
public class BasicItemDescription implements ItemDescription {
    private Item item;

    public BasicItemDescription(Item item) {
        this.item = item;
    }

    @Override
    public String getDescription() {
        return item.getTitle() != null ? item.getTitle() : "Untitled Item";
    }
}
