package decorator.search;

import java.util.List;
import entity.Item;

/**
 * Component in the Decorator search chain.
 */
public interface ItemSearchComponent {
    List<Item> search(ItemSearchCriteria criteria);
}

