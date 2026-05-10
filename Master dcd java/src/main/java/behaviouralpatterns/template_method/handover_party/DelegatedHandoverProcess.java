package behaviouralpatterns.template_method.handover_party;

import DAO.HandoverDataAccess;
import entity.HandoverLog;

public class DelegatedHandoverProcess extends AbstractHandoverProcess {

    public DelegatedHandoverProcess(HandoverDataAccess handoverDataAccess) {
        super(handoverDataAccess);
    }

    @Override
    protected boolean verifyCollectionCredentials(int claimID, int studentID) {
        // Example stricter check for delegated collection.
        return claimID > 0 && studentID > 0;
    }

    @Override
    protected void notifyHandoverSuccess(int claimID, int itemID, int studentID) {
        System.out.println("Delegated handover complete for claim " + claimID + ", item " + itemID
                + " on behalf of student " + studentID + ".");
    }

    @Override
    protected void afterHandover(HandoverLog log) {
        // Hook extension for delegated handovers.
        System.out.println("Delegated handover audit recorded at: " + log.getTimestamp());
    }
}
