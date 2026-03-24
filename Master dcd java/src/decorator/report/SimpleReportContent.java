package decorator.report;

import java.util.List;
import entity.Item;

/**
 * Base component for report content.
 */
public class SimpleReportContent implements ReportContent {

    @Override
    public String render(ReportContext ctx) {
        List<Item> items = ctx != null ? ctx.getItems() : null;
        if (items == null || items.isEmpty()) {
            return "Simple report\nTotal items: 0\nNo items to display.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Simple report\n");
        sb.append("Total items: ").append(items.size()).append("\n\n");
        sb.append("Top items:\n");

        int n = Math.min(5, items.size());
        for (int i = 0; i < n; i++) {
            Item it = items.get(i);
            sb.append("- ");
            sb.append(it.getTitle() != null ? it.getTitle() : "(untitled)");
            sb.append(" [");
            sb.append(it.getStatus() != null ? it.getStatus() : "?");
            sb.append("]\n");
        }

        return sb.toString();
    }
}

