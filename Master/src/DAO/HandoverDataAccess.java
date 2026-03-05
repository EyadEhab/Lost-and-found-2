package DAO;

import entity.HandoverLog;

/**
 *
 */
public class HandoverDataAccess {

    /**
     * Default constructor
     */
    public HandoverDataAccess() {
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
     * @param log
     * @return
     */
    public void createHandoverEntry(HandoverLog log) {
        // TODO implement here
    }

}