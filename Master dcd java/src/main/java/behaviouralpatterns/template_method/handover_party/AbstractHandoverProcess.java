package behaviouralpatterns.template_method.handover_party;

import DAO.HandoverDataAccess;
import DAO.ClaimDataAccess;
import entity.Claim;
import entity.HandoverLog;
import java.util.Date;
import java.util.Objects;

public abstract class AbstractHandoverProcess {

    private final HandoverDataAccess handoverDataAccess;
    private final ClaimDataAccess claimDataAccess;

    protected AbstractHandoverProcess(HandoverDataAccess handoverDataAccess) {
        this.handoverDataAccess = handoverDataAccess;
        this.claimDataAccess = new ClaimDataAccess();
    }

    public final boolean completeHandover(int claimID, int itemID, int studentID) {
        if (!validateApprovedClaim(claimID, itemID, studentID)) {
            return false;
        }

        if (!verifyCollectionCredentials(claimID, studentID)) {
            return false;
        }

        HandoverLog log = createHandoverLog(claimID, itemID, studentID);
        persistHandoverLog(log);
        notifyHandoverSuccess(claimID, itemID, studentID);
        afterHandover(log);
        return true;
    }

    public final boolean completeHandover(int itemID, int studentID) {
        return completeHandover(0, itemID, studentID);
    }

    public final boolean canCollect(int claimID, int studentID) {
        return verifyCollectionCredentials(claimID, studentID);
    }

    public final boolean canCollect(int studentID) {
        return canCollect(0, studentID);
    }

    protected abstract boolean verifyCollectionCredentials(int claimID, int studentID);

    protected boolean validateApprovedClaim(int claimID, int itemID, int studentID) {
        if (claimID <= 0 || itemID <= 0 || studentID <= 0) {
            return false;
        }

        Claim claim = claimDataAccess.getClaimById(claimID);
        if (claim == null) {
            return false;
        }

        if (claim.getItemID() != itemID || claim.getStudentID() != studentID) {
            return false;
        }

        String status = claim.getStatus();
        return status != null && status.toLowerCase().contains("approved");
    }

    protected HandoverLog createHandoverLog(int claimID, int itemID, int studentID) {
        HandoverLog log = new HandoverLog();
        log.setLogID(Math.abs(Objects.hash(claimID, itemID, studentID, System.nanoTime())));
        log.setClaimID(claimID);
        log.setTimestamp(new Date());
        return log;
    }

    protected void persistHandoverLog(HandoverLog log) {
        handoverDataAccess.createHandoverEntry(log);
    }

    protected abstract void notifyHandoverSuccess(int claimID, int itemID, int studentID);

    protected void afterHandover(HandoverLog log) {
        // Optional hook.
    }
}
