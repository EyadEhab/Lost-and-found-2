package behaviouralpatterns.strategy.case1_search;

import entity.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy Pattern — Concrete Strategy: Search by Category
 *
 * Searches items whose category contains the given keyword (case-insensitive).
 *
 * Role: ConcreteStrategy
 */
public class SearchByCategoryStrategy implements SearchStrategy {

    @Override
    public List<Item> search(List<Item> items, String keyword) {
        List<Item> results = new ArrayList<>();
        String keywordLower = keyword.trim().toLowerCase();

        for (Item item : items) {
            String category = item.getCategory();
            if (category != null && category.toLowerCase().contains(keywordLower)) {
                results.add(item);
            }
        }

        return results;
    }
}
