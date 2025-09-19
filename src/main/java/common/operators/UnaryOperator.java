package common.operators;

import common.propositions.Proposition;

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
}
