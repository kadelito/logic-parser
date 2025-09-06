package propositions;

import java.util.Set;

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
     * This method should not modify self or any child propositions.
     *
     * @return the truth value
     */
    boolean evaluate();

    /**
     * Returns a string representation of this proposition.
     * For compound propositions, this method is typically called on the child proposition(s),
     * and their respective toString return values are concatenated with any other symbols
     * associated with the compound proposition.
     * This method should not modify self or any child propositions.
     *
     * @return the complete representation
     */
    String toString();
}
