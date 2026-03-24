package controller;

import java.util.*;
import entity.Claim;
import entity.Item;
import factory.dao.DataAccessFactory;
import factory.dao.SqlDataAccessFactory;
import decorator.report.*;
import report.ClaimedItemsReport;
import report.LostItemsReport;
import report.ReportFormatter;
import report.WeeklyReport;

/**
 * 
 */
public class ReportController {

    /**
     * Factory used to obtain DAOs for this controller.
     * Allows swapping in a different DataAccessFactory (e.g., a mock) if needed.
     */
    private final DataAccessFactory dataFactory;

    /**
     * Default constructor uses the concrete SQL factory.
     */
    public ReportController() {
        this(new SqlDataAccessFactory());
    }

    /**
     * Overloaded constructor to allow injecting a custom DataAccessFactory.
     */
    public ReportController(DataAccessFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    /**
     * @param dateRange
     * @return
     */
    public List<Item> createWeeklyReport(Object dateRange) {
        DAO.ReportDataAccess dao = dataFactory.createReportDAO();
        
        // Calculate date range for the past 7 days
        Calendar cal = Calendar.getInstance();
        Date end = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -7);
        Date start = cal.getTime();
        
        return dao.fetchItemsByDate(start, end);
    }

    /**
     * Fetch lost/found items using ItemDAO
     */
    public List<Item> fetchLostItems() {
        DAO.ItemDataAccess dao = dataFactory.createItemDAO();
        // Combine 'Found' and 'Not Collected' if we want all available items
        List<Item> found = dao.fetchItemsByStatus("Found");
        List<Item> notCollected = dao.fetchItemsByStatus("Not Collected");
        List<Item> allLost = new ArrayList<>(found);
        allLost.addAll(notCollected);
        return allLost;
    }

    /**
     * Fetch all claim records using ClaimDAO
     */
    public List<Claim> fetchClaimedItems() {
        DAO.ClaimDataAccess dao = dataFactory.createClaimDAO();
        return dao.getAllClaims();
    }

    /**
     * @param itemList
     * @return
     */
    public String aggregateReportData(List<Item> itemList) {
        if (itemList == null || itemList.isEmpty()) {
            return "No items to report.";
        }
        return "Report for " + itemList.size() + " items.";
    }

    /**
     * Bridge-based weekly report: same data source as {@link #createWeeklyReport(Object)}; output shape depends on formatter.
     */
    public String buildWeeklyReportBridge(ReportFormatter formatter, Object dateRange) {
        List<Item> items = createWeeklyReport(dateRange);
        if (items == null) {
            items = Collections.emptyList();
        }
        return new WeeklyReport(formatter, items).export();
    }

    /**
     * Bridge-based list of items (lost/found style lines).
     */
    public String buildLostItemsReportBridge(ReportFormatter formatter, List<Item> items) {
        List<Item> safe = items != null ? items : fetchLostItems();
        return new LostItemsReport(formatter, safe).export();
    }

    /**
     * Bridge-based claims listing.
     */
    public String buildClaimedItemsReportBridge(ReportFormatter formatter, List<Claim> claims) {
        List<Claim> safe = claims != null ? claims : fetchClaimedItems();
        return new ClaimedItemsReport(formatter, safe).export();
    }

    /**
     * Decorator-based weekly report: base content + extra sections.
     * Does not replace existing Bridge-based report flow.
     */
    public String buildWeeklyReportDecorated(ReportFormatter formatter, Object dateRange) {
        List<Item> items = createWeeklyReport(dateRange);
        if (items == null) {
            items = Collections.emptyList();
        }

        ReportContext ctx = new ReportContext(items);

        ReportContent content = new SimpleReportContent();
        content = new StatisticsDecorator(content);
        content = new SummaryDecorator(content);
        content = new FooterDecorator(content);
        content = new DetailsDecorator(content);

        String title = "Weekly lost & found report (decorators)";
        return formatter.format(title, content.render(ctx));
    }

}