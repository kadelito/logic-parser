package propositions;

import operators.UnaryOperator;

import java.util.HashSet;
import java.util.Set;

public class UnaryProposition implements Proposition {
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
    public String toString() {
        return operator.toString() + p.toString();
    }
}
