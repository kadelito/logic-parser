package common;

import common.propositions.AtomicProposition;
import common.propositions.Proposition;

import java.util.Set;

/**
 * A top-level proposition with the context of its related atomic propositions.
 * <p>
 * Use this class when evaluating, displaying, or storing propositions in a context.
 * For direct access to the internal logical structure, see {@link Proposition}.
 *
 * @param proposition the head node of a propositional tree
 * @param atomics the atomic propositions present within the tree
 */
public record PropositionEntry(
        Proposition proposition,
        Set<AtomicProposition> atomics
) {}
