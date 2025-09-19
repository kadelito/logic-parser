package common.operators;

import common.propositions.Proposition;

/**
 * An enum class representing a unary operator in propositional logic.
 * <p>
 * Currently, this is only the NOT operator, but the class is structured to allow for more operators.
 */
public enum UnaryOperator {
    NOT;

    /**
     * Applies this operator to a single proposition.
     *
     * @param p the operand
     * @return the resulting truth value
     */
    public boolean apply(Proposition p) {
        switch (this) {
            case NOT:
                return !p.evaluate();
            default:
                throw new RuntimeException();
        }
    }
}
