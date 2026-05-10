package decorator.search;

import java.util.List;
import entity.Item;

/**
 * Base decorator for item search.
 */
public abstract class ItemSearchDecorator implements ItemSearchComponent {
    protected final ItemSearchComponent wrapped;

    protected ItemSearchDecorator(ItemSearchComponent wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public abstract List<Item> search(ItemSearchCriteria criteria);
}

