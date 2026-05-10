package behaviouralpatterns.iterator.case1_itemcollection;

import entity.Item;

/**
 * Iterator Pattern — Iterator Interface
 *
 * Defines the traversal contract for any item collection.
 * Concrete iterators implement this to filter and walk through
 * a list of Item objects without exposing the underlying structure.
 *
 * Role: Iterator (interface)
 */
public interface ItemIterator {

    /**
     * Returns true if there is at least one more item to iterate.
     *
     * @return true if a next element exists
     */
    boolean hasNext();

    /**
     * Returns the next Item in the iteration.
     *
     * @return the next Item
     */
    Item next();
}
