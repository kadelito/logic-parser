package interpreting.common;

import common.PropositionEntry;

public record PropositionConstructionResult(
    PropositionEntry value,
    String message
) {}