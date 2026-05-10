package behaviouralpatterns.strategy.case2_claim_processing;

import entity.Claim;
import java.util.Date;

/**
 * Strategy Pattern — Client
 *
 * Demonstrates how the Strategy Pattern works for claim processing
 * in the Lost & Found system. Creates sample claims and processes each
 * one through a different strategy using the same ClaimContext — showing
 * that the processing behaviour changes without modifying any claim logic.
 *
 * Role: Client
 */
public class ClaimClient {

    public static void main(String[] args) {

        // ----------------------------------------------------------------
        // 1. Prepare sample claims using the existing Claim entity
        // ----------------------------------------------------------------
        Claim claim1 = buildClaim(101, 1, 201);  // High-value item — needs strict verification
        Claim claim2 = buildClaim(102, 2, 202);  // Low-risk item — fast track
        Claim claim3 = buildClaim(103, 3, 203);  // Disputed item — needs admin review

        // ----------------------------------------------------------------
        // 2. Create a ClaimContext with an initial strategy
        // ----------------------------------------------------------------
        ClaimContext context = new ClaimContext(new StrictClaimStrategy());

        // ----------------------------------------------------------------
        // 3. Process claim1 with Strict strategy
        // ----------------------------------------------------------------
        System.out.println("=== Claim 1: Strict Processing ===");
        context.processClaim(claim1);
        System.out.println("  Final Status: " + claim1.getStatus());

        // ----------------------------------------------------------------
        // 4. Switch strategy at runtime — Fast processing
        // ----------------------------------------------------------------
        context.setStrategy(new FastClaimStrategy());
        System.out.println("\n=== Claim 2: Fast Processing ===");
        context.processClaim(claim2);
        System.out.println("  Final Status: " + claim2.getStatus());

        // ----------------------------------------------------------------
        // 5. Switch strategy at runtime — Manual review
        // ----------------------------------------------------------------
        context.setStrategy(new ManualClaimStrategy());
        System.out.println("\n=== Claim 3: Manual Processing ===");
        context.processClaim(claim3);
        System.out.println("  Final Status: " + claim3.getStatus());
    }

    // ----------------------------------------------------------------
    // Helper: build a sample Claim using the existing entity
    // ----------------------------------------------------------------
    private static Claim buildClaim(int claimID, int studentID, int itemID) {
        Claim claim = new Claim();
        claim.setClaimID(claimID);
        claim.setStudentID(studentID);
        claim.setItemID(itemID);
        claim.setStatus("Pending");
        claim.setRequestDate(new Date());
        return claim;
    }
}
