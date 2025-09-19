package common.propositions;

import common.operators.BinaryOperator;
import interpreting.common.RepresentationTable;

/**
 * A class representing a proposition with some binary operator.
 */
public class BinaryProposition extends Proposition {
    private static RepresentationTable table = RepresentationTable.getInstance();

    private Proposition p;
    private Proposition q;
    private BinaryOperator operator;

    /**
     * Instantiates a new binary proposition.
     *
     * @param p        the first operand
     * @param q        the second operand
     * @param operator the operator
     */
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

    /**
     * @return the "left" proposition operand
     */
    public Proposition getLeftProposition() {
        return p;
    }

    /**
     * @return the "right" proposition operand
     */
    public Proposition getRightProposition() {
        return q;
    }
}
