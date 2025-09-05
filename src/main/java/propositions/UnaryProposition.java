package propositions;

import operators.UnaryOperator;

public class UnaryProposition implements Proposition {
    private Proposition p;
    private UnaryOperator operator;

    public UnaryProposition(Proposition p, UnaryOperator operator) {
        this.p = p;
        this.operator = operator;
    }

    public boolean evaluate() {
        return operator.apply(p);
    }

    public Proposition getProposition() {
        return p;
    }

    public String toString() {
        return operator.toString() + p.toString();
    }
}
