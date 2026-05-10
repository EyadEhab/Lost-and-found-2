package decorator.description;

import entity.Item;
import java.text.SimpleDateFormat;

/**
 * Concrete decorator that adds the item found date to the description.
 */
public class DateDecorator extends ItemDescriptionDecorator {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public DateDecorator(ItemDescription wrappedDescription, Item item) {
        super(wrappedDescription, item);
    }

    @Override
    public String getDescription() {
        String dateStr = (item.getDateFound() != null) ? dateFormat.format(item.getDateFound()) : "Unknown";
        return wrappedDescription.getDescription() + " | Date: " + dateStr;
    }
}
