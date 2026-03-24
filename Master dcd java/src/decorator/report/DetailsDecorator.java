package decorator.report;

import entity.Item;

import java.util.List;

/**
 * Adds a full details section listing items.
 */
public class DetailsDecorator extends ReportContentDecorator {

    public DetailsDecorator(ReportContent wrapped) {
        super(wrapped);
    }

    @Override
    public String render(ReportContext ctx) {
        String base = wrapped.render(ctx);
        List<Item> items = ctx != null ? ctx.getItems() : null;

        if (items == null || items.isEmpty()) {
            return base + "\nDetails\n(No item details)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(base);
        sb.append("\nDetails\n");
        for (Item it : items) {
            sb.append("ID ").append(it.getItemID());
            sb.append(": ").append(it.getTitle() != null ? it.getTitle() : "(untitled)");
            sb.append(" [").append(it.getCategory() != null ? it.getCategory() : "No Category").append("]");
            sb.append(" at ").append(it.getLocation() != null ? it.getLocation() : "Unknown Location");
            sb.append(" — ").append(it.getStatus() != null ? it.getStatus() : "unknown status");
            sb.append("\n");
        }
        return sb.toString();
    }
}

