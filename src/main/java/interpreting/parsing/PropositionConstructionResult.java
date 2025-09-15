package interpreting.parsing;

import propositions.AtomicProposition;
import propositions.Proposition;

import java.util.Set;

record PropositionConstructionResult(
    Proposition value,
    Set<AtomicProposition> atomics,
    String message
) {}