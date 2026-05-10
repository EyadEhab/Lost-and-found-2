package behaviouralpatterns.strategy.case2_claim_processing;

import entity.Claim;

/**
 * Strategy Pattern — Claim Strategy Interface
 *
 * Defines the common contract for all claim processing strategies.
 * Each concrete strategy encapsulates a different way to process a claim.
 *
 * Role: Strategy (interface)
 */
public interface ClaimStrategy {

    /**
     * Processes the given claim according to this strategy's rules.
     *
     * @param claim the claim submitted by the student
     */
    void processClaim(Claim claim);
}
