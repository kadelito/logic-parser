package operators;

import interpreting.RepresentationTable;
import propositions.Proposition;

public enum UnaryOperator {
    NOT;

    public boolean apply(Proposition p) {
        switch (this) {
            case NOT:
                return !p.evaluate();
            default:
                throw new RuntimeException();
        }
    }

    public String toString() {
        return RepresentationTable.getInstance().getRepresentation(this);
    }
}
