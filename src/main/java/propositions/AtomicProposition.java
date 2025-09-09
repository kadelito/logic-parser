package propositions;

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
        if (immut)
            throw new RuntimeException("Cannot modify immutable atomic proposition.");
        else
            this.value = value;
    }

    // Intended as a.setValue(0) or a.setValue(1)
    public void setValue(long value) {
        setValue(value != 0);
    }

    @Override
    public String repr() {
        return repr;
    }
}
