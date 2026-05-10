package report;

import java.util.List;
import entity.Item;

/**
 * Refined abstraction: weekly summary using items from the existing report flow.
 */
public class WeeklyReport extends Report {

    private final List<Item> items;

    public WeeklyReport(ReportFormatter formatter, List<Item> items) {
        super(formatter);
        this.items = items;
    }

    @Override
    public String getTitle() {
        return "Weekly lost & found report";
    }

    @Override
    public String generateBody() {
        if (items == null || items.isEmpty()) {
            return "No items in this period.";
        }
        
        // Count by category
        java.util.Map<String, Integer> categoryCounts = new java.util.HashMap<>();
        for (Item it : items) {
            String cat = it.getCategory() != null ? it.getCategory() : "Uncategorized";
            categoryCounts.put(cat, categoryCounts.getOrDefault(cat, 0) + 1);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Weekly Summary (Past 7 Days)\n");
        sb.append("----------------------------\n");
        sb.append("Total items found: ").append(items.size()).append("\n\n");
        
        sb.append("Breakdown by Category:\n");
        for (java.util.Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            sb.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        sb.append("\nRecent Activity Sample:\n");
        int n = Math.min(5, items.size());
        for (int i = 0; i < n; i++) {
            Item it = items.get(i);
            sb.append("- ").append(it.getTitle() != null ? it.getTitle() : "(no title)");
            sb.append(" [").append(it.getStatus() != null ? it.getStatus() : "?").append("]\n");
        }
        
        if (items.size() > n) {
            sb.append("... and ").append(items.size() - n).append(" more.");
        }
        return sb.toString();
    }
}
