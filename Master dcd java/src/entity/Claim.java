package entity;

import java.util.*;

/**
 * 
 */
public class Claim {

    /**
     * Default constructor
     */
    public Claim() {
    }

    /**
     * 
     */
    private int claimID;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private Date requestDate;

    // Getters
    public int getClaimID() {
        return claimID;
    }

    public String getStatus() {
        return status;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    // Setters
    public void setClaimID(int claimID) {
        this.claimID = claimID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

}