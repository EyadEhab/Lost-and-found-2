package decorator.description;

import entity.Item;

/**
 * Concrete decorator that adds the item location to the description.
 */
public class LocationDecorator extends ItemDescriptionDecorator {

    public LocationDecorator(ItemDescription wrappedDescription, Item item) {
        super(wrappedDescription, item);
    }

    @Override
    public String getDescription() {
        return wrappedDescription.getDescription() + " | Location: " + 
               (item.getLocation() != null ? item.getLocation() : "Unknown");
    }
}
