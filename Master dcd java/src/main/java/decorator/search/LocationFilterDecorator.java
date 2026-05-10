package decorator.search;

import java.util.ArrayList;
import java.util.List;
import entity.Item;

/**
 * Adds a location filter on top of the wrapped search.
 */
public class LocationFilterDecorator extends ItemSearchDecorator {

    public LocationFilterDecorator(ItemSearchComponent wrapped) {
        super(wrapped);
    }

    @Override
    public List<Item> search(ItemSearchCriteria criteria) {
        List<Item> base = wrapped.search(criteria);
        if (criteria == null) {
            return base;
        }

        String location = criteria.getLocation();
        if (location == null || location.trim().isEmpty()) {
            return base;
        }

        String locationLower = location.toLowerCase();
        List<Item> filtered = new ArrayList<>();
        for (Item item : base) {
            String itemLoc = item.getLocation();
            if (itemLoc != null && itemLoc.toLowerCase().contains(locationLower)) {
                filtered.add(item);
            }
        }
        return filtered;
    }
}

