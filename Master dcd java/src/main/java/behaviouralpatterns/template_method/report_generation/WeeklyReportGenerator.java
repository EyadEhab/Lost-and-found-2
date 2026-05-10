package behaviouralpatterns.template_method.report_generation;

import entity.Item;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeklyReportGenerator extends AbstractReportGenerator {

    private final List<Item> items;

    public WeeklyReportGenerator(List<Item> items) {
        this.items = items;
    }

    @Override
    protected String buildHeader() {
        return "=== Weekly Lost & Found Report (" + LocalDate.now() + ") ===\n";
    }

    @Override
    protected String buildBody() {
        if (items == null || items.isEmpty()) {
            return "No items in this period.\n";
        }

        Map<String, Integer> categoryCounts = new HashMap<>();
        for (Item item : items) {
            String category = item.getCategory() != null ? item.getCategory() : "Uncategorized";
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Total items found: ").append(items.size()).append("\n");
        sb.append("Breakdown by category:\n");
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            sb.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    @Override
    protected String buildFooter() {
        return "End of weekly report.\n";
    }
}
