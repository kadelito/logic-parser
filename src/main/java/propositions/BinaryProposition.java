package propositions;

import operators.BinaryOperator;

public class BinaryProposition implements Proposition {
    private Proposition p;
    private Proposition q;
    private BinaryOperator operator;

    public BinaryProposition(Proposition p, Proposition q, BinaryOperator operator) {
        this.p = p;
        this.q = q;
        this.operator = operator;
    }

    public boolean evaluate() {
        return operator.apply(p, q);
    }

    public String toString() {
        String pStr = p.toString();
        if (p instanceof BinaryProposition)
            pStr = "(" + pStr + ")";

        String qStr = q.toString();
        if (q instanceof BinaryProposition)
            qStr = "(" + qStr + ")";

        return String.format("%s %s %s", pStr, operator, qStr);
    }

    public Proposition getProposition1() {
        return p;
    }

    public Proposition getProposition2() {
        return q;
    }

}
