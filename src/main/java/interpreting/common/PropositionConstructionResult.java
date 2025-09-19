package interpreting.common;

import common.PropositionEntry;
import common.propositions.AtomicProposition;
import common.propositions.Proposition;

import java.util.Set;

public record PropositionConstructionResult(
    PropositionEntry value,
    String message
) {}