package propositions;

import operators.BinaryOperator;

public class BinaryProposition extends Proposition {
    private Proposition p;
    private Proposition q;
    private BinaryOperator operator;

    public BinaryProposition(Proposition p, Proposition q, BinaryOperator operator) {
        this.p = p;
        this.q = q;
        this.operator = operator;
    }

    @Override
    public boolean evaluate() {
        return operator.apply(p, q);
    }

    @Override
    public String repr() {
        return String.format("%s %s %s", Proposition.formatChild(p), operator, Proposition.formatChild(q));
    }

    @Override
    public String reprRPN() {
        return String.format("%s %s %s", Proposition.formatChild(p), Proposition.formatChild(q), operator);
    }

    public Proposition getLeftProposition() {
        return p;
    }

    public Proposition getRightProposition() {
        return q;
    }
}
