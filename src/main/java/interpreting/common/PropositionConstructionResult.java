package interpreting.common;

import propositions.AtomicProposition;
import propositions.Proposition;

import java.util.Set;

public record PropositionConstructionResult(
    Proposition value,
    Set<AtomicProposition> atomics,
    String message
) {}