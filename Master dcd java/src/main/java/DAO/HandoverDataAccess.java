package DAO;

import entity.HandoverLog;
import java.util.ArrayList;
import java.util.List;

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
    private final List<HandoverLog> handoverLogs = new ArrayList<>();

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
        if (log == null) {
            return;
        }
        handoverLogs.add(log);
    }

}