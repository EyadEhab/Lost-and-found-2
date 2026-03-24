package decorator.description;

import entity.Item;

/**
 * Abstract decorator for the Item Description.
 * Implements ItemDescription and stores a wrapped ItemDescription object.
 */
public abstract class ItemDescriptionDecorator implements ItemDescription {
    protected ItemDescription wrappedDescription;
    protected Item item;

    public ItemDescriptionDecorator(ItemDescription wrappedDescription, Item item) {
        this.wrappedDescription = wrappedDescription;
        this.item = item;
    }

    @Override
    public abstract String getDescription();
}
