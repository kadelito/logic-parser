package common.propositions;

import common.operators.BinaryOperator;
import interpreting.common.RepresentationTable;

public class BinaryProposition extends Proposition {
    private static RepresentationTable table = RepresentationTable.getInstance();

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
        return String.format("%s %s %s", Proposition.formatChild(p), table.getRepresentation(operator), Proposition.formatChild(q));
    }

    @Override
    public String reprRPN() {
        return String.format("%s %s %s", p.reprRPN(), q.reprRPN(), operator);
    }

    public Proposition getLeftProposition() {
        return p;
    }

    public Proposition getRightProposition() {
        return q;
    }
}
