package common.propositions;

import interpreting.common.RepresentationTable;
import interpreting.tokenization.TokenType;
import common.LogicContext;

/**
 * A class representing atomic propositions in propositional logic.
 * <p>
 * Within a {@link LogicContext}, multiple <code>AtomicProposition</code> occurrences
 * should share an instance if they have the same {@link String} representation.
 * See the {@link #AtomicProposition(String, boolean) constructor} for more information.
 * @see Proposition
 */
public class AtomicProposition extends Proposition {

    private final boolean mutable;
    private boolean value;
    private final String repr;

    /**
     * "Default" constructor for AtomicProposition.
     * <p>
     * All atomics should be mutable EXCEPT for TRUE and FALSE in the Proposition class.
     * <p>
     * This is necessary for evaluating propositions under different truth value combinations.
     */
    AtomicProposition(String repr, boolean value, boolean mutable) {
        this.repr = repr;
        this.value = value;
        this.mutable = mutable;
    }

    /**
     * Instantiates a new Atomic proposition with a specified truth value.
     *
     * <p>Atomic propositions are normally created and managed through a
     * {@link LogicContext}. Within a context, all atomics with the same
     * name are guaranteed to refer to the same shared instance. This ensures
     * consistency when evaluating or comparing propositions.
     *
     * <p>Although this class provides a public constructor for creating
     * standalone propositions (e.g., for testing or self-contained examples),
     * such instances are <em>not</em> tracked by any <code>LogicContext</code>.
     * Mixing standalone atomics with context-managed ones may result in
     * incorrect behavior, such as duplicated variables in truth tables,
     * or two propositions incorrectly stated to be unequal.
     * <p>
     * <b>Usage guideline:</b> Prefer creating atomics via {@link LogicContext#getOrCreateAtomic(String)}
     * unless you explicitly want a self-contained, context-free proposition.
     */
    public AtomicProposition(String repr, boolean value) {
        this(repr, value, true);
    }

    /**
     * Instantiates a new Atomic proposition with a default truth value of <code>false</code>.
     * <p>
     * <b>Usage guideline:</b> Prefer creating atomics via {@link LogicContext#getOrCreateAtomic(String)}
     * unless you explicitly want a self-contained, context-free proposition.
     */
    public AtomicProposition(String repr) {this(repr, false, true);}

    /**
     * Sets the truth value.
     *
     * @param value the new truth value
     */
    public void setValue(boolean value) {
        if (mutable)
            this.value = value;
    }

    /**
     * @return the current truth value, <code>true</code> or <code>false</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Principle_of_bivalence">Principle of bivalence</a>
     */
    @Override
    public boolean evaluate() {
        return value;
    }

    /**
     * @return a corresponding String representation from {@link RepresentationTable}
     * if called on the constants TRUE or FALSE,
     * otherwise this instance's defined String representation.
     */
    @Override
    protected String repr() {
        if (this == Proposition.getTrue())
            return RepresentationTable.getInstance().getRepresentation(TokenType.TRUE);
        else if (this == Proposition.getFalse())
            return RepresentationTable.getInstance().getRepresentation(TokenType.FALSE);
        else
            return repr;
    }
}
