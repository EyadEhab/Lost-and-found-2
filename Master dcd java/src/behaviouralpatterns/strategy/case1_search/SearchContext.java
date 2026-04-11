package behaviouralpatterns.strategy.case1_search;

import entity.Item;
import java.util.List;

/**
 * Strategy Pattern — Context
 *
 * Holds a reference to a SearchStrategy and delegates the search operation
 * to whichever strategy is currently set. The strategy can be swapped at
 * runtime without changing the context or the client code.
 *
 * Role: Context
 */
public class SearchContext {

    private SearchStrategy strategy;

    /**
     * Creates a SearchContext with an initial strategy.
     *
     * @param strategy the initial search strategy to use
     */
    public SearchContext(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Replaces the current strategy with a new one at runtime.
     *
     * @param strategy the new strategy to apply
     */
    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Executes the search by delegating to the current strategy.
     *
     * @param items   the list of items to search through
     * @param keyword the search keyword
     * @return the filtered list of matching items
     */
    public List<Item> executeSearch(List<Item> items, String keyword) {
        return strategy.search(items, keyword);
    }
}
