package common;

import common.propositions.AtomicProposition;
import common.propositions.Proposition;

import java.util.Set;

/**
 * A record holding a proposition and a set of its related atomic propositions.
 * @param proposition
 * @param atomics
 */
public record PropositionEntry(
        Proposition proposition,
        Set<AtomicProposition> atomics
) {}
