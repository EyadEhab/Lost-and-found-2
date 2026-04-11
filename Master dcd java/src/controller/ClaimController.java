package controller;

import factory.dao.DataAccessFactory;
import factory.dao.SqlDataAccessFactory;
import behaviouralpatterns.strategy.case2_claim_processing.ClaimContext;
import behaviouralpatterns.strategy.case2_claim_processing.ClaimStrategy;
import behaviouralpatterns.strategy.case2_claim_processing.StrictClaimStrategy;
import behaviouralpatterns.strategy.case2_claim_processing.FastClaimStrategy;
import behaviouralpatterns.strategy.case2_claim_processing.ManualClaimStrategy;

/**
 * Controller responsible for claim lifecycle operations.
 *
 * Integrates the Strategy Pattern for flexible claim processing:
 * the processing behaviour (strict / fast / manual) is selected at
 * runtime via ClaimContext, with no if/else logic inside the controller.
 */
public class ClaimController {

    /**
     * Default constructor
     */
    public ClaimController() {
    }

    /**
     * Initiates the claim process for an item and saves it to the database.
     *
     * @param itemID    the ID of the item being claimed
     * @param studentID the ID of the student making the claim
     */
    public void startClaimProcess(int itemID, int studentID) {
        // Create new Claim entity
        entity.Claim newClaim = new entity.Claim();
        newClaim.setClaimID(0); // Will be set by database auto-increment
        newClaim.setStatus("Pending");
        newClaim.setRequestDate(new java.util.Date());
        newClaim.setStudentID(studentID);
        newClaim.setItemID(itemID);

        // Save to database via DAO
        try {
            DataAccessFactory factory = new SqlDataAccessFactory();
            DAO.ClaimDataAccess dao = factory.createClaimDAO();
            dao.insertClaim(newClaim);

            System.out.println("Claim process initiated for Item ID: " + itemID + " by Student ID: " + studentID);
        } catch (Exception e) {
            System.err.println("Database error while creating claim: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create claim: " + e.getMessage());
        }
    }

    /**
     * Processes a claim using the Strategy Pattern.
     *
     * Selects the appropriate ClaimStrategy based on the processing type,
     * then delegates entirely to ClaimContext — no processing logic here.
     *
     * @param claim        the Claim entity to process
     * @param claimType    one of: "strict", "fast", "manual"
     */
    public void processClaimWithStrategy(entity.Claim claim, String claimType) {
        ClaimStrategy strategy;

        switch (claimType) {
            case "fast":
                strategy = new FastClaimStrategy();
                break;
            case "manual":
                strategy = new ManualClaimStrategy();
                break;
            case "strict":
            default:
                strategy = new StrictClaimStrategy();
                break;
        }

        ClaimContext context = new ClaimContext(strategy);
        context.processClaim(claim);

        System.out.println("ClaimController: Claim ID " + claim.getClaimID()
                + " processed. Final status: " + claim.getStatus());
    }

    /**
     * Updates the claim status in the database.
     *
     * @param claimId   the ID of the claim
     * @param newStatus the new status string
     */
    public void updateClaimStatus(int claimId, String newStatus) {
        DataAccessFactory factory = new SqlDataAccessFactory();
        DAO.ClaimDataAccess dao = factory.createClaimDAO();
        dao.updateClaimStatus(claimId, newStatus);
    }

    /**
     * @param item
     * @return
     */
    public boolean validateClaim(entity.Item item) {
        // TODO implement here
        return false;
    }

    /**
     * @param id
     * @param decision
     * @return
     */
    public void processResponse(int id, String decision) {
        // TODO implement here
    }

    /**
     * @param id
     * @param decision
     * @return
     */
    public boolean validateDecision(int id, String decision) {
        // TODO implement here
        return false;
    }

}