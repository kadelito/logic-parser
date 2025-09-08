package propositions;

import operators.BinaryOperator;

public class BinaryProposition extends Proposition {
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
        String pStr = p.repr();
        if (p instanceof AtomicProposition)
            pStr = '\'' + pStr + '\'';
        else
            pStr = '(' + pStr + ')';

        String qStr = q.repr();
        if (q instanceof AtomicProposition)
            qStr = '\'' + qStr + '\'';
        else
            qStr = '(' + qStr + ')';

        return String.format("%s %s %s", pStr, operator, qStr);
    }
}
