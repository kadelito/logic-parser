package propositions;

import operators.UnaryOperator;

public class UnaryProposition extends Proposition {
    private Proposition p;
    private UnaryOperator operator;

    public UnaryProposition(Proposition p, UnaryOperator operator) {
        this.p = p;
        this.operator = operator;
    }

    @Override
    public boolean evaluate() {
        return operator.apply(p);
    }

    @Override
    public String repr() {
        return operator + Proposition.formatChild(p);
    }

    @Override
    public String reprRPN() {
        return Proposition.formatChild(p) + " " + operator;
    }

    public Proposition getProposition() {
        return p;
    }
}
