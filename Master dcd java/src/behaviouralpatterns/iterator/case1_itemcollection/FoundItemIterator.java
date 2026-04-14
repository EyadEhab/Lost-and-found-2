package behaviouralpatterns.iterator.case1_itemcollection;

import entity.Item;
import java.util.List;

/**
 * Iterator Pattern — Concrete Iterator
 *
 * Traverses a list of Items, returning only those whose status is "Found".
 * The caller never touches the underlying List directly.
 *
 * Role: ConcreteIterator
 * Integrated with: entity.Item (real project entity)
 */
public class FoundItemIterator implements ItemIterator {

    private final List<Item> items;
    private int position;
    private Item next;

    /**
     * @param items the full list of items to filter
     */
    public FoundItemIterator(List<Item> items) {
        this.items = items;
        this.position = 0;
        advance();
    }

    /** Pre-loads the next matching item into the buffer. */
    private void advance() {
        next = null;
        while (position < items.size()) {
            Item candidate = items.get(position++);
            if (candidate.getStatus() != null &&
                candidate.getStatus().equalsIgnoreCase("Found")) {
                next = candidate;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public Item next() {
        Item toReturn = next;
        advance();
        return toReturn;
    }
}
