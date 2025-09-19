package common.propositions;

/**
 * An abstract class representing a proposition.
 *
 * @see #evaluate()
 */
public abstract class Proposition {
    private static AtomicProposition TRUE = null;
    private static AtomicProposition FALSE = null;

    /**
     * Returns the truth value of this proposition.
     * <p>
     * Implementations should call this method on child propositions (if any).
     *
     * @return the truth value of this proposition
     */
    public abstract boolean evaluate();

    /**
     * Returns a string representation of this proposition.
     * <p>
     * Implementations should call this method on child propositions (if any).
     * <p>
     * This method is declared <code>protected</code> to force unique implementations.
     * {@link #toString} should instead be called from non-subclasses.
     *
     * @return the complete representation
     */
    protected abstract String repr();

    public String toString() {
        return repr();
    }

    /**
     * @return the constant {@link AtomicProposition} instance TRUE
     */
    public static AtomicProposition getTrue() {
        if (TRUE == null)
             TRUE = new AtomicProposition("T", true, true);
        return TRUE;
    }

    /**
     * @return the constant {@link AtomicProposition} instance FALSE
     */
    public static AtomicProposition getFalse() {
        if (FALSE == null)
            FALSE = new AtomicProposition("F", false, true);
        return FALSE;
    }

    /**
     * Helper method for proposition formatting
     */
    static String formatChild(Proposition p) {
        String str = p.repr();
        if (!(p instanceof AtomicProposition))
            str = '(' + str + ')';
        return str;
    }

}
