package behaviouralpatterns.strategy.case1_search;

import entity.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy Pattern — Concrete Strategy: Search by Location
 *
 * Searches items whose location contains the given keyword (case-insensitive).
 *
 * Role: ConcreteStrategy
 */
public class SearchByLocationStrategy implements SearchStrategy {

    @Override
    public List<Item> search(List<Item> items, String keyword) {
        List<Item> results = new ArrayList<>();
        String keywordLower = keyword.trim().toLowerCase();

        for (Item item : items) {
            String location = item.getLocation();
            if (location != null && location.toLowerCase().contains(keywordLower)) {
                results.add(item);
            }
        }

        return results;
    }
}
