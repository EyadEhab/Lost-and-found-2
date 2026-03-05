package controller;

import java.util.*;
import entity.Item;

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
     * @return list of matching items with status 'Found' or 'Processing Claim'
     */
    public List<Item> performSmartSearch(String keywords, String category) {
        DAO.ItemDataAccess dao = new DAO.ItemDataAccess();

        // Call DAO to perform the search with filters
        List<Item> results = dao.searchByKeywordsAndCategory(keywords, category);

        // Filter results to only include items that are still available
        List<Item> filteredResults = new ArrayList<>();
        for (Item item : results) {
            if ("Found".equals(item.getStatus()) || "Processing Claim".equals(item.getStatus())) {
                filteredResults.add(item);
            }
        }

        return filteredResults;
    }

}