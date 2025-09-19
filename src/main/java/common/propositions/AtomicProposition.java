package common.propositions;

import interpreting.common.RepresentationTable;
import interpreting.tokenization.TokenType;

/**
 * A class representing atomic propositions in propositional logic.
 */
public class AtomicProposition extends Proposition {

    private boolean immut;
    private boolean value;
    private String repr;

    /**
     * "Default" constructor for AtomicProposition.
     * <p>
     * All atomics should be mutable EXCEPT for TRUE and FALSE in the Proposition class.
     * <p>
     * This is because, in arguments & proofs, atomic propositions may be true or false at any time.
     */
    AtomicProposition(String repr, boolean value, boolean immut) {
        this.repr = repr;
        this.value = value;
        this.immut = immut;
    }

    /**
     * Instantiates a new Atomic proposition with a specified truth value.
     *
     * @param repr  the string representation
     * @param value the truth value
     */
    public AtomicProposition(String repr, boolean value) {
        this(repr, value, false);
    }

    /**
     * Instantiates a new Atomic proposition.
     *
     * @param repr  the string representation
     */
    public AtomicProposition(String repr) {
        this(repr, true);
    }

    /**
     * @return the current truth value
     */
    @Override
    public boolean evaluate() {
        return value;
    }

    /**
     * Sets the truth value.
     *
     * @param value the new truth value
     */
    public void setValue(boolean value) {
        if (!immut)
            this.value = value;
    }

    /**
     * @return a corresponding String representation from {@link RepresentationTable}
     * if called on the constants TRUE or FALSE,
     * otherwise this instance's defined String representation.
     */
    @Override
    protected String repr() {
        if (this == Proposition.getTrue()) return RepresentationTable.getInstance().getRepresentation(TokenType.TRUE);
        else if (this == Proposition.getFalse()) return RepresentationTable.getInstance().getRepresentation(TokenType.FALSE);
        return repr;
    }
}
