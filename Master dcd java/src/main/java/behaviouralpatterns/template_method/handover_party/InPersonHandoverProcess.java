package behaviouralpatterns.template_method.handover_party;

import DAO.HandoverDataAccess;

public class InPersonHandoverProcess extends AbstractHandoverProcess {

    public InPersonHandoverProcess(HandoverDataAccess handoverDataAccess) {
        super(handoverDataAccess);
    }

    @Override
    protected boolean verifyCollectionCredentials(int claimID, int studentID) {
        return studentID > 0;
    }

    @Override
    protected void notifyHandoverSuccess(int claimID, int itemID, int studentID) {
        System.out.println("In-person handover complete for claim " + claimID + ", item " + itemID
                + " to student " + studentID + ".");
    }
}
