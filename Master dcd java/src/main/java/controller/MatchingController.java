package controller;

import java.util.*;
import entity.Item;
import decorator.search.*;
import behaviouralpatterns.strategy.case1_search.SearchByNameStrategy;
import behaviouralpatterns.strategy.case1_search.SearchByCategoryStrategy;
import behaviouralpatterns.strategy.case1_search.SearchByLocationStrategy;
import behaviouralpatterns.strategy.case1_search.SearchContext;
import behaviouralpatterns.strategy.case1_search.SearchStrategy;

/**
 * Controller responsible for item matching and search operations.
 *
 * Integrates two search mechanisms:
 *  - Decorator Pattern: multi-filter search (category + status + location + date)
 *  - Strategy Pattern: single-focus search (by name, category, or location)
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
     * Performs multi-filter search using the Decorator Pattern.
     * Used when multiple filters (category, status, location) are applied together.
     *
     * @param keywords search keywords
     * @param category category filter ('All Categories' for no filter)
     * @param location location filter (substring match)
     * @param status   status filter ('All Statuses' for no filter)
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

        return search.search(criteria);
    }

    /**
     * Performs a focused search using the Strategy Pattern.
     *
     * Selects the appropriate SearchStrategy based on the search type,
     * then delegates to SearchContext — no if/else filtering logic in this method.
     *
     * @param searchType one of: "name", "category", "location"
     * @param keyword    the search term entered by the user
     * @return filtered list of items matching the criterion
     */
    public List<Item> performStrategySearch(String searchType, String keyword) {
        List<Item> allItems = getAllItems();

        SearchStrategy strategy;
        switch (searchType) {
            case "category":
                strategy = new SearchByCategoryStrategy();
                break;
            case "location":
                strategy = new SearchByLocationStrategy();
                break;
            case "name":
            default:
                strategy = new SearchByNameStrategy();
                break;
        }

        SearchContext context = new SearchContext(strategy);
        return context.executeSearch(allItems, keyword);
    }

    /**
     * Legacy version for backward compatibility
     */
    public List<Item> performSmartSearch(String keywords, String category) {
        return performSmartSearch(keywords, category, null, "Available");
    }

    /**
     * Retrieves all searchable items from the database.
     * Used as the item pool for Strategy Pattern searches.
     *
     * @return list of all available items
     */
    private List<Item> getAllItems() {
        try {
            factory.dao.DataAccessFactory dataFactory = new factory.dao.SqlDataAccessFactory();
            DAO.ItemDataAccess dao = dataFactory.createItemDAO();
            return dao.getAllItems();
        } catch (Exception e) {
            System.err.println("MatchingController: Could not load items — " + e.getMessage());
            return new ArrayList<>();
        }
    }
}