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
     * Default constructor
     */
    public ReportController() {
    }

    /**
     * @param dateRange
     * @return
     */
    public List<Item> createWeeklyReport(Object dateRange) {
        DataAccessFactory factory = new SqlDataAccessFactory();
        DAO.ReportDataAccess dao = factory.createReportDAO();
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