package behaviouralpatterns.strategy.case2_claim_processing;

import entity.Claim;

/**
 * Strategy Pattern — Concrete Strategy: Manual Claim Processing
 *
 * Does not process the claim automatically.
 * Instead, it flags the claim for admin review and places it in a pending queue.
 * Used for disputed, high-value, or ambiguous claims.
 *
 * Role: ConcreteStrategy
 */
public class ManualClaimStrategy implements ClaimStrategy {

    @Override
    public void processClaim(Claim claim) {
        System.out.println("[ManualClaimStrategy] Processing Claim ID: " + claim.getClaimID());
        System.out.println("  >> Claim flagged for manual admin review.");
        System.out.println("  >> Item ID " + claim.getItemID() + " is on hold pending admin decision.");
        System.out.println("  >> Student ID " + claim.getStudentID() + " will be notified once reviewed.");

        // Mark as awaiting admin action — no automatic approval
        claim.setStatus("Processing Claim");
        System.out.println("  >> Result: FORWARDED to admin for manual decision.");
    }
}
