package operators;

import interpreting.common.RepresentationTable;
import propositions.Proposition;

public enum BinaryOperator {

    AND,
    OR,
    IMPLY,
    BICONDITIONAL;

    private String[] repr;

    public boolean apply(Proposition p, Proposition q) {
        return switch (this) {
            case AND -> p.evaluate() && q.evaluate();
            case OR -> p.evaluate() || q.evaluate();
            case IMPLY -> !p.evaluate() || q.evaluate();
            case BICONDITIONAL -> p.evaluate() == q.evaluate();
            default -> throw new RuntimeException();
        };
    }

    public String toString() {
        return RepresentationTable.getInstance().getRepresentation(this);
    }
}
