package propositions;

import interpreting.common.RepresentationTable;
import interpreting.tokenization.TokenType;

public class AtomicProposition extends Proposition {

    private boolean immut;
    private boolean value;
    private String repr;

    AtomicProposition(String repr, boolean value, boolean immut) {
        this.repr = repr;
        this.value = value;
        this.immut = immut;
    }

    public AtomicProposition(String repr, boolean value) {
        this(repr, value, false);
    }

    public AtomicProposition(String repr) {
        this(repr, true);
    }

    @Override
    public boolean evaluate() {
        return value;
    }

    public void setValue(boolean value) {
        if (!immut)
            this.value = value;
    }

    @Override
    public String repr() {
        if (this == Proposition.TRUE)       return RepresentationTable.getInstance().getRepresentation(TokenType.TRUE);
        else if (this == Proposition.FALSE) return RepresentationTable.getInstance().getRepresentation(TokenType.FALSE);
        return '\'' + repr + '\'';
    }
}
