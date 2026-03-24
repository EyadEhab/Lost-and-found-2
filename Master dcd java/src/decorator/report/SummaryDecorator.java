package decorator.report;

import entity.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adds a short textual summary based on the report input.
 */
public class SummaryDecorator extends ReportContentDecorator {

    public SummaryDecorator(ReportContent wrapped) {
        super(wrapped);
    }

    @Override
    public String render(ReportContext ctx) {
        String base = wrapped.render(ctx);
        List<Item> items = ctx != null ? ctx.getItems() : null;

        if (items == null || items.isEmpty()) {
            return base + "\nSummary\n- No items in this period.";
        }

        Map<String, Integer> statusCounts = new HashMap<>();
        Map<String, Integer> categoryCounts = new HashMap<>();

        for (Item it : items) {
            String st = it.getStatus() != null ? it.getStatus() : "unknown";
            statusCounts.put(st, statusCounts.getOrDefault(st, 0) + 1);

            String cat = it.getCategory() != null ? it.getCategory() : "Uncategorized";
            categoryCounts.put(cat, categoryCounts.getOrDefault(cat, 0) + 1);
        }

        String mostStatus = "unknown";
        int mostStatusCount = -1;
        for (Map.Entry<String, Integer> e : statusCounts.entrySet()) {
            if (e.getValue() != null && e.getValue() > mostStatusCount) {
                mostStatusCount = e.getValue();
                mostStatus = e.getKey();
            }
        }

        String mostCategory = "Uncategorized";
        int mostCategoryCount = -1;
        for (Map.Entry<String, Integer> e : categoryCounts.entrySet()) {
            if (e.getValue() != null && e.getValue() > mostCategoryCount) {
                mostCategoryCount = e.getValue();
                mostCategory = e.getKey();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(base);
        sb.append("\nSummary\n");
        sb.append("- Total items: ").append(items.size()).append("\n");
        sb.append("- Most frequent status: ").append(mostStatus).append("\n");
        sb.append("- Most frequent category: ").append(mostCategory).append("\n");
        return sb.toString();
    }
}

