package behaviouralpatterns.iterator.case1_itemcollection;

import entity.Item;
import factory.ItemTypeFactory;

/**
 * Iterator Pattern — Client / Demo
 *
 * Demonstrates and tests all four item iterators in the Lost & Found system.
 * Creates a pool of 10 sample items spread across all statuses, adds them to
 * an ItemRepository, then exercises LostItemIterator, FoundItemIterator,
 * AvailableItemIterator, and ClaimedItemIterator — printing each result and
 * verifying the counts match expectations.
 *
 * Expected counts from the sample data below:
 *   Lost      → 3 items
 *   Found     → 2 items
 *   Available → 3 items  (all have status "Unclaimed")
 *   Claimed   → 2 items
 *
 * Role: Client
 */
public class IteratorClient {

    public static void main(String[] args) {

        // ----------------------------------------------------------------
        // 1. Build the repository with sample data
        // ----------------------------------------------------------------
        ItemRepository repo = buildRepository();

        System.out.println("=================================================");
        System.out.println(" Iterator Pattern — Lost & Found Item Collections");
        System.out.println("=================================================");
        System.out.println("Total items in repository: " + repo.getAllItems().size());
        System.out.println();

        // ----------------------------------------------------------------
        // 2. Iterate LOST items
        // ----------------------------------------------------------------
        System.out.println("--- Lost Items ---");
        ItemIterator lostIt = repo.getLostIterator();
        int lostCount = 0;
        while (lostIt.hasNext()) {
            printItem(lostIt.next());
            lostCount++;
        }
        if (lostCount == 0) System.out.println("  (none)");
        System.out.println();

        // ----------------------------------------------------------------
        // 3. Iterate FOUND items
        // ----------------------------------------------------------------
        System.out.println("--- Found Items ---");
        ItemIterator foundIt = repo.getFoundIterator();
        int foundCount = 0;
        while (foundIt.hasNext()) {
            printItem(foundIt.next());
            foundCount++;
        }
        if (foundCount == 0) System.out.println("  (none)");
        System.out.println();

        // ----------------------------------------------------------------
        // 4. Iterate AVAILABLE items (Unclaimed / Available)
        // ----------------------------------------------------------------
        System.out.println("--- Available Items (Unclaimed) ---");
        ItemIterator availIt = repo.getAvailableIterator();
        int availCount = 0;
        while (availIt.hasNext()) {
            printItem(availIt.next());
            availCount++;
        }
        if (availCount == 0) System.out.println("  (none)");
        System.out.println();

        // ----------------------------------------------------------------
        // 5. Iterate CLAIMED items
        // ----------------------------------------------------------------
        System.out.println("--- Claimed Items ---");
        ItemIterator claimedIt = repo.getClaimedIterator();
        int claimedCount = 0;
        while (claimedIt.hasNext()) {
            printItem(claimedIt.next());
            claimedCount++;
        }
        if (claimedCount == 0) System.out.println("  (none)");
        System.out.println();

        // ----------------------------------------------------------------
        // 6. Verify expected counts
        // ----------------------------------------------------------------
        System.out.println("=================================================");
        System.out.println(" Verification Results");
        System.out.println("=================================================");
        boolean ok = true;
        ok &= verify("Lost",      lostCount,    3);
        ok &= verify("Found",     foundCount,   2);
        ok &= verify("Available", availCount,   3);
        ok &= verify("Claimed",   claimedCount, 2);
        System.out.println();
        if (ok) {
            System.out.println("✓ All iterator counts verified successfully.");
        } else {
            System.out.println("✗ One or more counts did not match expected values.");
        }
    }

    // ----------------------------------------------------------------
    // Helper: build sample Item objects (no DB needed)
    // ----------------------------------------------------------------
    private static ItemRepository buildRepository() {
        ItemRepository repo = new ItemRepository();

        // --- Lost items (3) ---
        repo.addItem(makeItem(1, "Blue Backpack",       "Bags",         "Lost",     "Building A - Room 101"));
        repo.addItem(makeItem(2, "Student ID Card",     "Documents",    "Lost",     "Cafeteria"));
        repo.addItem(makeItem(3, "Silver Bracelet",     "Accessories",  "Lost",     "Sports Hall"));

        // --- Found items (2) ---
        repo.addItem(makeItem(4, "iPhone 13",           "Electronics",  "Found",    "Library - Floor 2"));
        repo.addItem(makeItem(5, "Wireless Earbuds",    "Electronics",  "Found",    "Library - Study Room"));

        // --- Unclaimed / Available items (3) ---
        repo.addItem(makeItem(6, "Black Leather Wallet","Accessories",  "Unclaimed","Main Gate"));
        repo.addItem(makeItem(7, "Prescription Glasses","Accessories",  "Unclaimed","Reception"));
        repo.addItem(makeItem(8, "USB Charging Cable",  "Electronics",  "Unclaimed","Computer Lab"));

        // --- Claimed items (2) ---
        repo.addItem(makeItem(9,  "Red Umbrella",       "Accessories",  "Claimed",  "Main Entrance"));
        repo.addItem(makeItem(10, "Laptop Charger",     "Electronics",  "Claimed",  "Auditorium"));

        return repo;
    }

    /** Creates a configured Item using the existing entity and Flyweight factory. */
    private static Item makeItem(int id, String title, String category,
                                 String status, String location) {
        Item item = new Item();
        item.setItemID(id);
        item.setTitle(title);
        item.setLocation(location);
        item.setItemType(ItemTypeFactory.getItemType(category, status));
        return item;
    }

    /** Prints one item line to the console. */
    private static void printItem(Item item) {
        System.out.printf("  [ID: %2d] %-26s | Category: %-14s | Status: %-10s | Location: %s%n",
                item.getItemID(),
                item.getTitle(),
                item.getCategory(),
                item.getStatus(),
                item.getLocation());
    }

    /**
     * Checks an actual count against an expected value and prints the result.
     *
     * @return true if the count matches
     */
    private static boolean verify(String label, int actual, int expected) {
        boolean pass = (actual == expected);
        System.out.printf("  %-10s : expected=%d  actual=%d  [%s]%n",
                label, expected, actual, pass ? "PASS" : "FAIL");
        return pass;
    }
}
