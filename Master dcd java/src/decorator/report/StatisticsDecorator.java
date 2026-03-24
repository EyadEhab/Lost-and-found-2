package decorator.report;

import entity.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Adds statistics sections: status breakdown and category breakdown.
 */
public class StatisticsDecorator extends ReportContentDecorator {

    public StatisticsDecorator(ReportContent wrapped) {
        super(wrapped);
    }

    @Override
    public String render(ReportContext ctx) {
        String base = wrapped.render(ctx);
        List<Item> items = ctx != null ? ctx.getItems() : null;

        Map<String, Integer> statusCounts = new TreeMap<>();
        Map<String, Integer> categoryCounts = new TreeMap<>();

        if (items != null) {
            for (Item it : items) {
                String st = it.getStatus() != null ? it.getStatus() : "unknown";
                statusCounts.put(st, statusCounts.getOrDefault(st, 0) + 1);

                String cat = it.getCategory() != null ? it.getCategory() : "Uncategorized";
                categoryCounts.put(cat, categoryCounts.getOrDefault(cat, 0) + 1);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(base);
        sb.append("\nStatistics\n");
        sb.append("Statuses:\n");

        List<Map.Entry<String, Integer>> statusEntries = new ArrayList<>(statusCounts.entrySet());
        statusEntries.sort((a, b) -> {
            int cmp = b.getValue().compareTo(a.getValue()); // value desc
            if (cmp != 0) {
                return cmp;
            }
            // stable-ish tie-breaker: alphabetical
            return a.getKey().compareToIgnoreCase(b.getKey());
        });

        for (Map.Entry<String, Integer> e : statusEntries) {
            sb.append("- ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }

        sb.append("\nCategories:\n");
        List<Map.Entry<String, Integer>> categoryEntries = new ArrayList<>(categoryCounts.entrySet());
        categoryEntries.sort((a, b) -> {
            int cmp = b.getValue().compareTo(a.getValue()); // value desc
            if (cmp != 0) {
                return cmp;
            }
            return a.getKey().compareToIgnoreCase(b.getKey());
        });

        for (Map.Entry<String, Integer> e : categoryEntries) {
            sb.append("- ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }

        return sb.toString();
    }
}

