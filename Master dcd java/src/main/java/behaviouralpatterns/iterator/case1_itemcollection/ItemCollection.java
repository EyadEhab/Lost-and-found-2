package behaviouralpatterns.iterator.case1_itemcollection;

/**
 * Iterator Pattern — Aggregate (Collection) Interface
 *
 * Any class that holds a collection of Items and wants to expose
 * typed iterators must implement this interface.
 * Each factory method returns an iterator pre-filtered to a specific status.
 *
 * Role: Aggregate (interface)
 */
public interface ItemCollection {

    /**
     * Returns an iterator over items whose status is "Lost".
     *
     * @return LostItemIterator
     */
    ItemIterator getLostIterator();

    /**
     * Returns an iterator over items whose status is "Found".
     *
     * @return FoundItemIterator
     */
    ItemIterator getFoundIterator();

    /**
     * Returns an iterator over items whose status is "Unclaimed" / "Available".
     *
     * @return AvailableItemIterator
     */
    ItemIterator getAvailableIterator();

    /**
     * Returns an iterator over items whose status is "Claimed".
     *
     * @return ClaimedItemIterator
     */
    ItemIterator getClaimedIterator();
}
