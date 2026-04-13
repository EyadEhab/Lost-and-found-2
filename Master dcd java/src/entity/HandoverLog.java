package entity;

import java.util.*;

/**
 * 
 */
public class HandoverLog {

    /**
     * Default constructor
     */
    public HandoverLog() {
    }

    /**
     * 
     */
    private int logID;

    /**
     *
     */
    private int claimID;

    /**
     * 
     */
    private Date timestamp;


    // Getters
    public int getLogID() {
        return logID;
    }

    public int getClaimID() {
        return claimID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setLogID(int logID) {
        this.logID = logID;
    }

    public void setClaimID(int claimID) {
        this.claimID = claimID;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}