package decorator.search;

import java.util.List;
import entity.Item;

/**
 * Base component: keyword search (with default "available" statuses via DAO).
 */
public class BasicItemSearch implements ItemSearchComponent {

    @Override
    public List<Item> search(ItemSearchCriteria criteria) {
        DAO.ItemDataAccess dao = new DAO.ItemDataAccess();
        String keywords = criteria != null ? criteria.getKeywords() : null;
        return dao.searchByKeywords(keywords);
    }
}

