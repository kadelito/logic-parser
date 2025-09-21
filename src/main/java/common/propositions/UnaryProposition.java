package common.propositions;

import common.operators.BinaryOperator;
import common.operators.UnaryOperator;
import interpreting.common.RepresentationTable;

/**
 * A class representing a proposition with a unary operator applied to a single child proposition.
 * @see Proposition
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

    /**
     * Creates and returns a string representation of this instance.
     * <p>
     * This is done first by obtaining a representation of the child proposition,
     * then by concatenating it with the associated {@link UnaryOperator},
     * whose representation is determined by {@link RepresentationTable}.
     * in prefix format (<code>[operator] p</code> or <code>[operator]p</code>)
     * @return a complete string representation of this instance
     */
    @Override
    protected String repr() {
        return table.getRepresentation(operator) + Proposition.formatChild(p);
    }

    /**
     * @return the proposition operand
     */
    public Proposition getProposition() {
        return p;
    }
}
