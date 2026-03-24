package decorator.description;

import entity.Item;

/**
 * Concrete decorator that adds the item category to the description.
 */
public class CategoryDecorator extends ItemDescriptionDecorator {

    public CategoryDecorator(ItemDescription wrappedDescription, Item item) {
        super(wrappedDescription, item);
    }

    @Override
    public String getDescription() {
        return wrappedDescription.getDescription() + " | Category: " + 
               (item.getCategory() != null ? item.getCategory() : "Unknown");
    }
}
