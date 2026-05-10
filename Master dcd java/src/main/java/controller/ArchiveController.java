package controller;

import java.util.*;

/**
 *
 */
public class ArchiveController {

    /**
     * Default constructor
     */
    public ArchiveController() {
    }

    /**
     *
     */
    private Date thresholdDate;

    /**
     *
     */
    private int batchSize;

    // Getters and Setters
    public Date getThresholdDate() {
        return thresholdDate;
    }

    public void setThresholdDate(Date thresholdDate) {
        this.thresholdDate = thresholdDate;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }



    /**
     * @return
     */
    public int archiveExpiredItems() {
        // TODO implement here
        return 0;
    }

}