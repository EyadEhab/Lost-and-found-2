package decorator.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import entity.Item;

/**
 * Adds a status filter on top of the wrapped search.
 *
 * The existing app treats "Available" as:
 * - Not Collected
 * - Found
 * - Processing Claim
 */
public class StatusFilterDecorator extends ItemSearchDecorator {

    public StatusFilterDecorator(ItemSearchComponent wrapped) {
        super(wrapped);
    }

    @Override
    public List<Item> search(ItemSearchCriteria criteria) {
        List<Item> base = wrapped.search(criteria);
        if (criteria == null) {
            return base;
        }

        String statusFilter = criteria.getStatusFilter();
        if (statusFilter == null || statusFilter.trim().isEmpty() || 
            "All Statuses".equalsIgnoreCase(statusFilter) || "All".equalsIgnoreCase(statusFilter)) {
            return base;
        }

        List<String> allowed;
        if ("Available".equalsIgnoreCase(statusFilter)) {
            allowed = Arrays.asList("Not Collected", "Found", "Processing Claim");
        } else {
            allowed = Arrays.asList(statusFilter);
        }

        List<Item> filtered = new ArrayList<>();
        for (Item item : base) {
            String st = item.getStatus();
            if (st == null) {
                continue;
            }
            for (String a : allowed) {
                if (st.equalsIgnoreCase(a)) {
                    filtered.add(item);
                    break;
                }
            }
        }
        return filtered;
    }
}

