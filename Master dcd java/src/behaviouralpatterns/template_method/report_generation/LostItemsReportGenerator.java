package behaviouralpatterns.template_method.report_generation;

import entity.Item;
import java.util.List;

public class LostItemsReportGenerator extends AbstractReportGenerator {

    private final List<Item> items;

    public LostItemsReportGenerator(List<Item> items) {
        this.items = items;
    }

    @Override
    protected String buildHeader() {
        return "=== Lost / Found Items Report ===\n";
    }

    @Override
    protected String buildBody() {
        if (items == null || items.isEmpty()) {
            return "No items to list.\n";
        }

        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append("ID ").append(item.getItemID());
            sb.append(": ").append(item.getTitle() != null ? item.getTitle() : "(untitled)");
            sb.append(" | Category: ").append(item.getCategory() != null ? item.getCategory() : "No Category");
            sb.append(" | Location: ").append(item.getLocation() != null ? item.getLocation() : "Unknown Location");
            sb.append(" | Status: ").append(item.getStatus() != null ? item.getStatus() : "unknown status");
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    protected String buildFooter() {
        int total = items == null ? 0 : items.size();
        return "Total items: " + total + "\n";
    }
}
