package common;

import common.propositions.AtomicProposition;
import common.propositions.Proposition;

import java.util.Set;

public record PropositionEntry(
        Proposition value,
        Set<AtomicProposition> atomics
) {}
