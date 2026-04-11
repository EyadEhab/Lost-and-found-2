package behaviouralpatterns.strategy.case1_search;

import entity.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy Pattern — Concrete Strategy: Search by Name
 *
 * Searches items whose title contains the given keyword (case-insensitive).
 *
 * Role: ConcreteStrategy
 */
public class SearchByNameStrategy implements SearchStrategy {

    @Override
    public List<Item> search(List<Item> items, String keyword) {
        List<Item> results = new ArrayList<>();
        String keywordLower = keyword.trim().toLowerCase();

        for (Item item : items) {
            String title = item.getTitle();
            if (title != null && title.toLowerCase().contains(keywordLower)) {
                results.add(item);
            }
        }

        return results;
    }
}
