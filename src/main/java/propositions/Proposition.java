package propositions;

public abstract class Proposition {
    public static final AtomicProposition TRUE = new AtomicProposition("T", true, true);
    public static final AtomicProposition FALSE = new AtomicProposition("F", false, true);

    /**
     * Returns the truth value of this proposition.
     * Compound propositions will first evaluate the child proposition(s),
     * and the associated operation will be applied to the resulting truth value(s).
     *
     * @return the truth value
     */
    public abstract boolean evaluate();

    /**
     * Returns a string representation of this proposition.
     * If a proposition is a member, repr should be called on that
     *
     * @return the complete representation
     */
    public abstract String repr();

    public String toString() {
        return repr();
    }
}
