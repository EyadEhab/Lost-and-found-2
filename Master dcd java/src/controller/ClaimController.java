package controller;

import factory.dao.DataAccessFactory;
import factory.dao.SqlDataAccessFactory;

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