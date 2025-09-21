package logic;

import common.PropositionEntry;

import java.util.Collection;

/**
 * An interface allowing for different implementations of two key operations:
 * <ul>
 *     <li>Checking for equivalence between two {@link PropositionEntry propositions} with {@link #areEqual}, and</li>
 *     <li>Evaluating validity of an argument with {@link #isArgumentValid}.</li>
 * </ul>
 * These methods should return a consistent value regardless
 * of the values of each {@link common.propositions.AtomicProposition AtomicProposition}
 * associated with each {@link PropositionEntry}.
 * <p>
 * After calling these methods, the value of each <code>AtomicProposition</code> may be different
 * from what they originally were.
 */
public interface Reasoner {
    /**
     * Determines whether, for all combinations of truth values
     * for their {@link common.propositions.AtomicProposition AtomicPropositions},
     * two {@link common.PropositionEntry Propositions} have equivalent truth values.
     * <p>
     * Both propositions should be in the same context,
     * which means any <code>AtomicPropositions</code> with identical names should share an instance.
     * <p>
     * A return value of <code>null</code> signifies something went wrong during the process.
     *
     * @param p1 the first proposition
     * @param p2 the second proposition
     * @return whether the two propositions are equivalent
     */
    Boolean areEqual(PropositionEntry p1, PropositionEntry p2);

    /**
     * Determines whether an array of premises (propositions) form a valid argument with a conclusion (also a proposition)
     * <p>
     * An argument is considered valid if and only if, when the premises are true, the conclusion is always true.
     * <p>
     * All premises and the conclusion should be in the same context,
     * which means any AtomicPropositions with identical names should share an instance.
     * <p>
     * A return value of <code>null</code> signifies something went wrong during the process.
     *
     * @param conclusion the conclusion to be proved
     * @param premises   the premises used to prove the conclusion
     * @return whether the premises and conclusion form a <strong>valid</strong> argument
     *
     * @see <a href="https://en.wikipedia.org/wiki/Validity_(logic)">Wikipedia's definition of validity</a>
     */
    Boolean isArgumentValid(PropositionEntry conclusion, Collection<PropositionEntry> premises);
}