package decorator.search;

import java.util.Date;

/**
 * Carries all optional filters for item searching.
 */
public class ItemSearchCriteria {
    private final String keywords;
    private final String category;
    private final String statusFilter;
    private final String location;
    private final Date startDate;
    private final Date endDate;

    public ItemSearchCriteria(
            String keywords,
            String category,
            String statusFilter,
            String location,
            Date startDate,
            Date endDate) {
        this.keywords = keywords;
        this.category = category;
        this.statusFilter = statusFilter;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static ItemSearchCriteria forKeywordsAndCategory(String keywords, String category) {
        // Keep behavior aligned with existing search: "available" statuses only.
        return new ItemSearchCriteria(keywords, category, "Available", null, null, null);
    }

    public String getKeywords() {
        return keywords;
    }

    public String getCategory() {
        return category;
    }

    public String getStatusFilter() {
        return statusFilter;
    }

    public String getLocation() {
        return location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}

