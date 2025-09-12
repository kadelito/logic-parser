package propositions;

public abstract class Proposition {
    public static final AtomicProposition TRUE = new AtomicProposition("T", true, true);
    public static final AtomicProposition FALSE = new AtomicProposition("F", false, true);

    /**
     * Returns the truth value of this proposition.
     * <p>
     * Implementations should use the value of evaluate when called on child propositions.
     *
     * @return the truth value of this proposition
     */
    public abstract boolean evaluate();

    /**
     * Returns a string representation of this proposition.
     * <p>
     * Implementations should include the value of repr when called on child propositions.
     *
     * @return the complete representation
     */
    public abstract String repr();

    public String toString() {
        return repr();
    }
}
