package behaviouralpatterns.strategy.case2_claim_processing;

import entity.Claim;

/**
 * Strategy Pattern — Context
 *
 * Holds a reference to a ClaimStrategy and delegates all claim processing
 * to whichever strategy is currently set. The strategy can be swapped at
 * runtime without any change to how the context or client work.
 *
 * Role: Context
 */
public class ClaimContext {

    private ClaimStrategy strategy;

    /**
     * Creates a ClaimContext with an initial processing strategy.
     *
     * @param strategy the initial claim strategy to use
     */
    public ClaimContext(ClaimStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Replaces the current strategy with a new one at runtime.
     *
     * @param strategy the new claim strategy to apply
     */
    public void setStrategy(ClaimStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Processes the given claim by delegating entirely to the current strategy.
     * The context contains no processing logic of its own.
     *
     * @param claim the claim to process
     */
    public void processClaim(Claim claim) {
        strategy.processClaim(claim);
    }
}
