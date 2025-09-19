package common.propositions;

/**
 * An abstract class representing a proposition.
 *
 * @see #evaluate()
 */
public abstract class Proposition {
    public static final AtomicProposition TRUE = new AtomicProposition("T", true, true);
    public static final AtomicProposition FALSE = new AtomicProposition("F", false, true);

    /**
     * Returns the truth proposition of this proposition.
     * <p>
     * Implementations should call this method on child propositions (if any) and use the resulting proposition.
     *
     * @return the truth proposition of this proposition
     */
    public abstract boolean evaluate();

    /**
     * Returns a string representation of this proposition.
     * <p>
     * Implementations should include the proposition of repr when called on child propositions.
     *
     * @return the complete representation
     */
    public abstract String repr();

    /**
     * Returns a string representation of the proposition in Reverse Polish Notation.
     * <p>
     * This means that operations (if any) must appear directly after the propositions on which they are applied.
     *
     * @return the complete representation in Reverse Polish Notation
     */
    public abstract String reprRPN();

    // Helper method for proposition formatting
    static String formatChild(Proposition p) {
        String str = p.repr();
        if (!(p instanceof AtomicProposition))
            str = '(' + str + ')';
        return str;
    }

    public String toString() {
        return repr();
    }
}
