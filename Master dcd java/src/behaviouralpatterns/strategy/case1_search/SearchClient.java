package behaviouralpatterns.strategy.case1_search;

import entity.Item;
import factory.ItemTypeFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy Pattern — Client
 *
 * Demonstrates how the Strategy Pattern works in the Lost & Found system.
 * Creates sample items, then applies each search strategy interchangeably
 * through a single SearchContext — without modifying any search logic.
 *
 * Role: Client
 */
public class SearchClient {

    public static void main(String[] args) {

        // ----------------------------------------------------------------
        // 1. Prepare sample data
        // ----------------------------------------------------------------
        List<Item> items = buildSampleItems();

        // ----------------------------------------------------------------
        // 2. Create a SearchContext with an initial strategy
        // ----------------------------------------------------------------
        SearchContext context = new SearchContext(new SearchByNameStrategy());

        // ----------------------------------------------------------------
        // 3. Search by Name
        // ----------------------------------------------------------------
        System.out.println("=== Search by Name: 'wallet' ===");
        List<Item> byName = context.executeSearch(items, "wallet");
        printResults(byName);

        // ----------------------------------------------------------------
        // 4. Switch strategy at runtime — Search by Category
        // ----------------------------------------------------------------
        context.setStrategy(new SearchByCategoryStrategy());
        System.out.println("\n=== Search by Category: 'electronics' ===");
        List<Item> byCategory = context.executeSearch(items, "electronics");
        printResults(byCategory);

        // ----------------------------------------------------------------
        // 5. Switch strategy at runtime — Search by Location
        // ----------------------------------------------------------------
        context.setStrategy(new SearchByLocationStrategy());
        System.out.println("\n=== Search by Location: 'library' ===");
        List<Item> byLocation = context.executeSearch(items, "library");
        printResults(byLocation);
    }

    // ----------------------------------------------------------------
    // Helper: build sample Item objects using the existing Item entity
    // ----------------------------------------------------------------
    private static List<Item> buildSampleItems() {
        List<Item> items = new ArrayList<>();

        Item item1 = new Item();
        item1.setItemID(1);
        item1.setTitle("Black Leather Wallet");
        item1.setItemType(ItemTypeFactory.getItemType("Accessories", "Unclaimed"));
        item1.setLocation("Main Gate");

        Item item2 = new Item();
        item2.setItemID(2);
        item2.setTitle("iPhone 13");
        item2.setItemType(ItemTypeFactory.getItemType("Electronics", "Unclaimed"));
        item2.setLocation("Library - Floor 2");

        Item item3 = new Item();
        item3.setItemID(3);
        item3.setTitle("Blue Backpack");
        item3.setItemType(ItemTypeFactory.getItemType("Bags", "Unclaimed"));
        item3.setLocation("Cafeteria");

        Item item4 = new Item();
        item4.setItemID(4);
        item4.setTitle("Wireless Earbuds");
        item4.setItemType(ItemTypeFactory.getItemType("Electronics", "Unclaimed"));
        item4.setLocation("Library - Study Room");

        Item item5 = new Item();
        item5.setItemID(5);
        item5.setTitle("Student ID Card");
        item5.setItemType(ItemTypeFactory.getItemType("Documents", "Unclaimed"));
        item5.setLocation("Main Gate");

        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);

        return items;
    }

    // ----------------------------------------------------------------
    // Helper: print search results in a readable format
    // ----------------------------------------------------------------
    private static void printResults(List<Item> results) {
        if (results.isEmpty()) {
            System.out.println("  No items found.");
        } else {
            for (Item item : results) {
                System.out.println("  [ID: " + item.getItemID() + "] "
                        + item.getTitle()
                        + " | Category: " + item.getCategory()
                        + " | Location: " + item.getLocation()
                        + " | Status: " + item.getStatus());
            }
        }
    }
}
