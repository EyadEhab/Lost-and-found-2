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
     * Performs smart search with category filtering
     * @param keywords search keywords
     * @param category category filter ('All Categories' for no filter)
     * @return list of matching items with status still available (not collected, etc.)
     */
    public List<Item> performSmartSearch(String keywords, String category) {
        // Decorator chain: basic search + category + status (+ optional location/date).
        ItemSearchCriteria criteria = ItemSearchCriteria.forKeywordsAndCategory(keywords, category);

        ItemSearchComponent search = new BasicItemSearch();
        search = new CategoryFilterDecorator(search);
        search = new StatusFilterDecorator(search);
        search = new LocationFilterDecorator(search);
        search = new DateFilterDecorator(search);

        List<Item> results = search.search(criteria);

        // Preserve existing "available-only" behavior (defensive: DAO and decorators should match).
        List<Item> filteredResults = new ArrayList<>();
        for (Item item : results) {
            String st = item.getStatus();
            if ("Not Collected".equals(st) || "Found".equals(st) || "Processing Claim".equals(st)) {
                filteredResults.add(item);
            }
        }

        return filteredResults;
    }

}