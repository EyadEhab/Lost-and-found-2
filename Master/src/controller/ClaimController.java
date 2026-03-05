package controller;

/**
 * 
 */
public class ClaimController {

    /**
     * Default constructor
     */
    public ClaimController() {
    }





    /**
     * Initiates the claim process for an item
     * @param itemID the ID of the item being claimed
     * @param studentID the ID of the student making the claim
     */
    public void startClaimProcess(int itemID, int studentID) {
        // Create new Claim entity
        entity.Claim newClaim = new entity.Claim();
        newClaim.setClaimID(0); // Will be set by database auto-increment
        newClaim.setStatus("Pending");
        newClaim.setRequestDate(new java.util.Date());

        // Note: The Claim entity doesn't have itemID or studentID fields
        // We may need to extend the Claim entity or store this information elsewhere
        // For now, we'll proceed with the basic claim creation

        // Save to database via DAO
        try {
            DAO.ClaimDataAccess dao = new DAO.ClaimDataAccess();
            dao.insertClaim(newClaim);
            // TODO: Associate the claim with the item and student
            // This might require additional entity relationships or database schema changes
        } catch (Exception e) {
            System.err.println("Database error while creating claim: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create claim: " + e.getMessage());
        }
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