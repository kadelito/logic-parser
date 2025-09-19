package common.propositions;

import common.operators.UnaryOperator;
import interpreting.common.RepresentationTable;

public class UnaryProposition extends Proposition {
    private static RepresentationTable table = RepresentationTable.getInstance();

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
        return table.getRepresentation(operator) + Proposition.formatChild(p);
    }

    @Override
    public String reprRPN() {
        return p.reprRPN() + " " + operator;
    }

    public Proposition getProposition() {
        return p;
    }
}
