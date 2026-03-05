package DAO;

import java.util.*;
import entity.Item;

/**
 *
 */
public class ReportDataAccess {

    /**
     * Default constructor
     */
    public ReportDataAccess() {
    }

    /**
     *
     */
    private String connectionString;

    // Getters and Setters
    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }


    /**
     * @param start 
     * @param end 
     * @return
     */
    public List<Item> fetchItemsByDate(Date start, Date end) {
        // TODO implement here
        return null;
    }

}