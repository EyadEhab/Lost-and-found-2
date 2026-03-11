package controller;

import java.util.*;
import entity.Item;
import factory.dao.DataAccessFactory;
import factory.dao.SqlDataAccessFactory;

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

}