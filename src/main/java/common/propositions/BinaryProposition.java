package common.propositions;

import common.operators.BinaryOperator;
import interpreting.common.RepresentationTable;

/**
 * A class representing a proposition with some binary operator and two child proposition operands.
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

    /**
     * Creates and returns a string representation of this instance.
     * <p>
     * This is done first by obtaining a representation of each child proposition,
     * then by concatenating the two with the associated {@link BinaryOperator},
     * whose representation is determined by {@link RepresentationTable}.
     * in infix format (<code>p1 [operator] p2</code>)
     * @return a complete string representation of this instance
     */
    @Override
    protected String repr() {
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
