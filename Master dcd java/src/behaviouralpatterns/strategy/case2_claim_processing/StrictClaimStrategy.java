package behaviouralpatterns.strategy.case2_claim_processing;

import entity.Claim;

/**
 * Strategy Pattern — Concrete Strategy: Strict Claim Processing
 *
 * Performs full verification before approving a claim:
 *  - Verifies the student's ownership evidence
 *  - Confirms the item is still unclaimed
 *  - Only approves after all checks pass
 *
 * Used for high-value or disputed items.
 *
 * Role: ConcreteStrategy
 */
public class StrictClaimStrategy implements ClaimStrategy {

    @Override
    public void processClaim(Claim claim) {
        System.out.println("[StrictClaimStrategy] Processing Claim ID: " + claim.getClaimID());
        System.out.println("  >> Step 1: Verifying student ownership (Student ID: " + claim.getStudentID() + ")");
        System.out.println("  >> Step 2: Confirming item is still unclaimed (Item ID: " + claim.getItemID() + ")");
        System.out.println("  >> Step 3: Cross-referencing submission date with item log");

        // Simulate all checks passing
        boolean ownershipVerified = true;
        boolean itemUnclaimed     = true;

        if (ownershipVerified && itemUnclaimed) {
            claim.setStatus("Processing Claim");
            System.out.println("  >> Result: APPROVED after full verification.");
        } else {
            claim.setStatus("Processing Claim");
            System.out.println("  >> Result: Verification failed, further review needed.");
        }
    }
}
