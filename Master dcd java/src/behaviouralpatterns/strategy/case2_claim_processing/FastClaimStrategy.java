package behaviouralpatterns.strategy.case2_claim_processing;

import entity.Claim;

/**
 * Strategy Pattern — Concrete Strategy: Fast Claim Processing
 *
 * Performs minimal validation and immediately approves the claim.
 * Used for low-risk items such as documents or cards where
 * quick turnaround is more important than deep verification.
 *
 * Role: ConcreteStrategy
 */
public class FastClaimStrategy implements ClaimStrategy {

    @Override
    public void processClaim(Claim claim) {
        System.out.println("[FastClaimStrategy] Processing Claim ID: " + claim.getClaimID());
        System.out.println("  >> Step 1: Basic identity check (Student ID: " + claim.getStudentID() + ")");
        System.out.println("  >> Step 2: Skipping deep verification — fast track enabled");

        // Fast approval — minimal checks only
        claim.setStatus("Approved");
        System.out.println("  >> Result: APPROVED via fast processing.");
    }
}
