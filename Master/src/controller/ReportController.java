package controller;

import java.util.*;
import entity.Item;
import DAO.ReportDataAccess;

/**
 * 
 */
public class ReportController {

    /**
     * Default constructor
     */
    public ReportController() {
    }

    /**
     * @param dateRange
     * @return
     */
    public List<Item> createWeeklyReport(Object dateRange) {
        ReportDataAccess dao = new ReportDataAccess();
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