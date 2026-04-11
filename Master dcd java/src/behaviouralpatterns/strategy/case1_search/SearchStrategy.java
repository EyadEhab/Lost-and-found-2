package behaviouralpatterns.strategy.case1_search;

import entity.Item;
import java.util.List;

/**
 * Strategy Pattern — Search Strategy Interface
 *
 * Defines the common contract for all search strategies.
 * Each concrete strategy provides a different search behaviour.
 *
 * Role: Strategy (interface)
 */
public interface SearchStrategy {

    /**
     * Searches through the given list of items using a keyword.
     *
     * @param items   the full list of items to search through
     * @param keyword the search term entered by the user
     * @return a filtered list of items matching the search criterion
     */
    List<Item> search(List<Item> items, String keyword);
}
