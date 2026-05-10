package behaviouralpatterns.iterator.case1_itemcollection;

import entity.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Iterator Pattern — Concrete Aggregate
 *
 * Stores all system items in an internal List and exposes four
 * typed iterators without leaking the List to outside callers.
 * This is the central collection the client works with.
 *
 * Role: ConcreteAggregate
 * Integrated with: entity.Item (real project entity)
 */
public class ItemRepository implements ItemCollection {

    /** Internal storage — never exposed directly to callers */
    private final List<Item> items = new ArrayList<>();

    /**
     * Adds a single item to the repository.
     *
     * @param item the item to add
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Returns a read-only view of all items (used by the client for display).
     *
     * @return unmodifiable list of all items
     */
    public List<Item> getAllItems() {
        return java.util.Collections.unmodifiableList(items);
    }

    /** @return iterator over items with status "Lost" */
    @Override
    public ItemIterator getLostIterator() {
        return new LostItemIterator(items);
    }

    /** @return iterator over items with status "Found" */
    @Override
    public ItemIterator getFoundIterator() {
        return new FoundItemIterator(items);
    }

    /** @return iterator over items with status "Unclaimed" or "Available" */
    @Override
    public ItemIterator getAvailableIterator() {
        return new AvailableItemIterator(items);
    }

    /** @return iterator over items with status "Claimed" */
    @Override
    public ItemIterator getClaimedIterator() {
        return new ClaimedItemIterator(items);
    }
}
