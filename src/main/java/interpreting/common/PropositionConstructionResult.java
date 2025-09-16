package interpreting.common;

import common.propositions.AtomicProposition;
import common.propositions.Proposition;

import java.util.Set;

public record PropositionConstructionResult(
    Proposition value,
    Set<AtomicProposition> atomics,
    String message
) {}