package controller;

import DAO.HandoverDataAccess;
import behaviouralpatterns.template_method.handover_party.AbstractHandoverProcess;
import behaviouralpatterns.template_method.handover_party.InPersonHandoverProcess;

/**
 * 
 */
public class HandoverController {
    private final AbstractHandoverProcess handoverProcess;

    /**
     * Default constructor
     */
    public HandoverController() {
        this(new InPersonHandoverProcess(new HandoverDataAccess()));
    }

    public HandoverController(AbstractHandoverProcess handoverProcess) {
        this.handoverProcess = handoverProcess;
    }

    /**
     * @param claimID
     * @param itemID
     * @param studentID
     * @return
     */
    public boolean logCollection(int claimID, int itemID, int studentID) {
        return handoverProcess.completeHandover(claimID, itemID, studentID);
    }

    /**
     * Backward-compatible overload when claim ID is not supplied.
     */
    public boolean logCollection(int itemID, int studentID) {
        return handoverProcess.completeHandover(0, itemID, studentID);
    }

    /**
     * @param claimID
     * @param studentID
     * @return
     */
    public boolean verifyCollectionCredentials(int claimID, int studentID) {
        return handoverProcess.canCollect(claimID, studentID);
    }

    /**
     * Backward-compatible overload when claim ID is not supplied.
     */
    public boolean verifyCollectionCredentials(int studentID) {
        return handoverProcess.canCollect(0, studentID);
    }

}