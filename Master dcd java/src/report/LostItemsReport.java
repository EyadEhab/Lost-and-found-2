package report;

import java.util.List;
import entity.Item;

/**
 * Refined abstraction: lists lost/found items (uses domain {@link Item}).
 */
public class LostItemsReport extends Report {

    private final List<Item> items;

    public LostItemsReport(ReportFormatter formatter, List<Item> items) {
        super(formatter);
        this.items = items;
    }

    @Override
    public String getTitle() {
        return "Lost / found items report";
    }

    @Override
    public String generateBody() {
        if (items == null || items.isEmpty()) {
            return "No items to list.";
        }
        StringBuilder sb = new StringBuilder();
        for (Item it : items) {
            sb.append("ID ").append(it.getItemID());
            sb.append(": ").append(it.getTitle() != null ? it.getTitle() : "(untitled)");
            sb.append(" — ").append(it.getStatus() != null ? it.getStatus() : "unknown status");
            sb.append("\n");
        }
        return sb.toString();
    }
}
