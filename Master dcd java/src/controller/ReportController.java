package controller;

import java.util.*;
import entity.Claim;
import entity.Item;
import factory.dao.DataAccessFactory;
import factory.dao.SqlDataAccessFactory;
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
        // TODO: Extract actual dates from dateRange object
        return dao.fetchItemsByDate(new Date(), new Date());
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
        List<Item> safe = items != null ? items : Collections.emptyList();
        return new LostItemsReport(formatter, safe).export();
    }

    /**
     * Bridge-based claims listing.
     */
    public String buildClaimedItemsReportBridge(ReportFormatter formatter, List<Claim> claims) {
        List<Claim> safe = claims != null ? claims : Collections.emptyList();
        return new ClaimedItemsReport(formatter, safe).export();
    }

}