package controller;

import java.util.*;
import entity.Item;
import decorator.search.*;

/**
 * 
 */
public class MatchingController {

    /**
     * Default constructor
     */
    public MatchingController() {
    }




    /**
     * @param keywords 
     * @return
     */
    public List<Item> performSmartSearch(String keywords) {
        // TODO implement here
        return null;
    }

    /**
     * @param keywords
     * @param data
     * @return
     */
    public List<Item> applyMatchingLogic(String keywords, List<Item> data) {
        // TODO implement here
        return null;
    }

    /**
     * Performs smart search with category, location, and status filtering
     * @param keywords search keywords
     * @param category category filter ('All Categories' for no filter)
     * @param location location filter (substring match)
     * @param status status filter ('All Statuses' for no filter)
     * @return list of matching items
     */
    public List<Item> performSmartSearch(String keywords, String category, String location, String status) {
        // Decorator chain: basic search + category + status + location + date.
        ItemSearchCriteria criteria = ItemSearchCriteria.forAllFilters(keywords, category, location, status);

        ItemSearchComponent search = new BasicItemSearch();
        search = new CategoryFilterDecorator(search);
        search = new StatusFilterDecorator(search);
        search = new LocationFilterDecorator(search);
        search = new DateFilterDecorator(search);

        List<Item> results = search.search(criteria);

        // If a specific status was requested, we trust the decorator.
        // If "Available" or "All Statuses" was requested, we might want to ensure consistency.
        // For simplicity and to allow searching for 'Collected' items, we return the results from the decorators.
        return results;
    }

    /**
     * Legacy version for backward compatibility
     */
    public List<Item> performSmartSearch(String keywords, String category) {
        return performSmartSearch(keywords, category, null, "Available");
    }

}