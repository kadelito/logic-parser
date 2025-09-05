package propositions;

public class AtomicProposition implements Proposition {

    private boolean immut = false;
    private boolean value;
    private String repr;

    AtomicProposition(String repr, boolean value, boolean immut) {
        if (repr.isBlank())
            throw new RuntimeException("Atomic proposition cannot be blank.");
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

    public String toString() {
        return repr;
    }
}
