package common.propositions;

import common.operators.UnaryOperator;
import interpreting.common.RepresentationTable;

/**
 * A class representing a proposition with some unary operator.
 */
public class UnaryProposition extends Proposition {
    private static RepresentationTable table = RepresentationTable.getInstance();

    private Proposition p;
    private UnaryOperator operator;

    /**
     * Instantiates a new unary proposition.
     *
     * @param p        the operand
     * @param operator the operator
     */
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

    /**
     * @return the proposition operand
     */
    public Proposition getProposition() {
        return p;
    }
}
