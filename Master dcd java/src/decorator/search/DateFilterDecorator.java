package decorator.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import entity.Item;

/**
 * Adds a date-range filter on top of the wrapped search.
 */
public class DateFilterDecorator extends ItemSearchDecorator {

    public DateFilterDecorator(ItemSearchComponent wrapped) {
        super(wrapped);
    }

    @Override
    public List<Item> search(ItemSearchCriteria criteria) {
        List<Item> base = wrapped.search(criteria);
        if (criteria == null) {
            return base;
        }

        Date start = criteria.getStartDate();
        Date end = criteria.getEndDate();
        if (start == null && end == null) {
            return base;
        }

        List<Item> filtered = new ArrayList<>();
        for (Item item : base) {
            Date itemDate = item.getDateFound();
            if (itemDate == null) {
                continue;
            }

            if (start != null && itemDate.before(start)) {
                continue;
            }
            if (end != null && itemDate.after(end)) {
                continue;
            }

            filtered.add(item);
        }
        return filtered;
    }
}

