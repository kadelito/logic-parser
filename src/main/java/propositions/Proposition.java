package propositions;

/**
 * The interface Proposition.
 */
public interface Proposition {
    AtomicProposition TRUE = new AtomicProposition("T", true, true);
    AtomicProposition FALSE = new AtomicProposition("F", false, true);

    /**
     * Returns the truth value of this proposition.
     * Compound propositions will first evaluate the child proposition(s),
     * and the associated operation will be applied to the resulting truth value(s).
     *
     * @return the truth value
     */
    boolean evaluate();

    /**
     * Returns a string representation of this proposition.
     * If a proposition is a member, repr should be called on that
     *
     * @return the complete representation
     */
    String repr();
}
