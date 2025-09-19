package common.operators;

import common.propositions.Proposition;

/**
 * An enum class representing a binary operator in propositional logic.
 */
public enum BinaryOperator {

    AND,
    OR,
    IMPLY,
    BICONDITIONAL;

    /**
     * Applies this operator to two propositions.
     *
     * @param p the first operand
     * @param q the second operand
     * @return the resulting truth value
     */
    public boolean apply(Proposition p, Proposition q) {
        return switch (this) {
            case AND -> p.evaluate() && q.evaluate();
            case OR -> p.evaluate() || q.evaluate();
            case IMPLY -> !p.evaluate() || q.evaluate();
            case BICONDITIONAL -> p.evaluate() == q.evaluate();
        };
    }
}
