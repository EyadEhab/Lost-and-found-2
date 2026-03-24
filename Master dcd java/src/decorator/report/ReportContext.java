package decorator.report;

import java.util.List;
import entity.Item;

/**
 * Carries report input data for all decorators.
 */
public class ReportContext {
    private final List<Item> items;

    public ReportContext(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
}

