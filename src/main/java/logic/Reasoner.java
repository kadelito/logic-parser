package logic;

import common.PropositionEntry;

import java.util.Collection;

/**
 * The interface Reasoner.
 */
public interface Reasoner {
    /**
     * Determines whether, for all combinations of truth values
     * for their {@link common.propositions.AtomicProposition AtomicProposition}s,
     * two {@link common.PropositionEntry Proposition}s have equivalent truth values.
     * <p>
     * Both propositions should be in the same context, which means any AtomicPropositions with identical names should share an instance.
     * <p>
     * A return value of null signifies something went wrong.
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
     * A return value of null signifies something went wrong.
     *
     * @param conclusion the conclusion to be proved
     * @param premises   the premises used to prove the conclusion
     * @return whether the premises and conclusion form a <strong>valid</strong> argument
     *
     * @see <a href="https://en.wikipedia.org/wiki/Validity_(logic)">Wikipedia's definition of validity</a>
     */
    Boolean isArgumentValid(PropositionEntry conclusion, Collection<PropositionEntry> premises);
}