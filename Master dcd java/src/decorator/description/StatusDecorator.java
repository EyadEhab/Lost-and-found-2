package decorator.description;

import entity.Item;

/**
 * Concrete decorator that adds the item status to the description.
 */
public class StatusDecorator extends ItemDescriptionDecorator {

    public StatusDecorator(ItemDescription wrappedDescription, Item item) {
        super(wrappedDescription, item);
    }

    @Override
    public String getDescription() {
        return wrappedDescription.getDescription() + " | Status: " + 
               (item.getStatus() != null ? item.getStatus() : "Unknown");
    }
}
