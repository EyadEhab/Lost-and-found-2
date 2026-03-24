package decorator.search;

import java.util.ArrayList;
import java.util.List;
import entity.Item;

/**
 * Adds a category filter on top of the wrapped search.
 */
public class CategoryFilterDecorator extends ItemSearchDecorator {

    public CategoryFilterDecorator(ItemSearchComponent wrapped) {
        super(wrapped);
    }

    @Override
    public List<Item> search(ItemSearchCriteria criteria) {
        List<Item> base = wrapped.search(criteria);
        if (criteria == null) {
            return base;
        }

        String category = criteria.getCategory();
        if (category == null || category.trim().isEmpty() || "All Categories".equals(category)) {
            return base;
        }

        String categoryLower = category.toLowerCase();
        List<Item> filtered = new ArrayList<>();
        for (Item item : base) {
            String itemCategory = item.getCategory();
            if (itemCategory != null && itemCategory.toLowerCase().contains(categoryLower)) {
                filtered.add(item);
            }
        }
        return filtered;
    }
}

