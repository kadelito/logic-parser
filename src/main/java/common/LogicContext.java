package common;

import common.propositions.AtomicProposition;

import java.util.*;

/**
 * A container for {@link PropositionEntry PropositionEntries} that also manages the set of
 * {@link common.propositions.AtomicProposition AtomicPropositions} used across them.
 * <p>
 * The context ensures that each atomic symbol is represented by a single shared
 * {@code AtomicProposition} instance. This allows propositions added to the same
 * context to remain consistent with one another.
 * <p>
 * Adding a proposition entry to this context will register any new atomics it contains,
 * and removing an entry will also remove its atomics if they are not used elsewhere
 * in the context. Other {@link Collection} methods follow this pattern as well.
 * <p>
 * <code>AtomicPropositions</code> are held in a {@link Map}
 * to, using a corresponding {@link String}, allow access to existing ones or create new ones
 * through {@link #getOrCreateAtomic(String)}.
 *
 * @see PropositionEntry
 * @see common.propositions.AtomicProposition
 */
public class LogicContext implements Collection<PropositionEntry> {
    private final List<PropositionEntry> propositions;
    private final Map<String, AtomicProposition> atomicsMap;

    /**
     * Instantiates an empty LogicContext.
     */
    public LogicContext() {
        atomicsMap = new HashMap<>();
        propositions = new ArrayList<>();
    }

    /**
     * Instantiates a new LogicContext from an existing collection.
     */
    public LogicContext(Collection<? extends PropositionEntry> c) {
        this();
        addAll(c);
    }

    /**
     * Returns the {@link AtomicProposition} corresponding to a string.
     * If no such instance exists, one is created using the given string.
     *
     * @param repr the repr
     * @return an <code>AtomicProposition</code> with the name of repr
     * @see AtomicProposition#AtomicProposition(String, boolean) AtomicProposition#AtomicProposition(String, boolean)Constructor for AtomicProposition
     */
    public AtomicProposition getOrCreateAtomic(String repr) {
        return atomicsMap.getOrDefault(repr, new AtomicProposition(repr));
    }

    /**
     * Returns the {@link PropositionEntry} at a specific index.
     */
    public PropositionEntry getEntry(int index) {return propositions.get(index);}

    /**
     * Checks whether a {@link String} corresponds
     * with an {@link AtomicProposition} that already exists.
     *
     * @param repr the string representation to be searched
     * @return true if a <code>AtomicProposition</code> has already been defined with {@code repr}
     */
    public boolean contains(String repr) {
        return atomicsMap.containsKey(repr);
    }

    /**
     * Checks whether a {@link AtomicProposition} has already been defined in this context.
     *
     * @param a the <code>AtomicProposition</code> to be checked
     * @return true if <code>a</code> is present
     */
    public boolean contains(AtomicProposition a) {
        return atomicsMap.containsKey(a.toString());
    }

    @Override
    public int size() {
        return propositions.size();
    }

    @Override
    public boolean isEmpty() {
        return propositions.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return propositions.contains(o);
    }

    @Override
    public Iterator<PropositionEntry> iterator() {
        return propositions.iterator();
    }

    @Override
    public Object[] toArray() {
        return propositions.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return propositions.toArray(a);
    }

    @Override
    public boolean add(PropositionEntry entry) {
        for (AtomicProposition a: entry.atomics())
            atomicsMap.put(a.toString(), a);
        return propositions.add(entry);
    }

    /**
     * {@inheritDoc}
     * <p>
     * In the case that the element <code>e</code> (a {@link PropositionEntry}) was present,
     * if any {@link AtomicProposition AtomicPropositions} were present in exclusively element <code>e</code>,
     * the associated <code>AtomicPropositions</code> are removed from context.
     */
    @Override
    public boolean remove(Object o) {
        if (o instanceof PropositionEntry p) {
            if (!propositions.remove(p)) return false;
            Set<AtomicProposition> removeMe = new HashSet<>(p.atomics());
            for (PropositionEntry entry: propositions) {
                removeMe.removeAll(entry.atomics());
                if (removeMe.isEmpty()) return true;
            }
            for (AtomicProposition a: removeMe)
                atomicsMap.remove(a.toString());
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(propositions).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends PropositionEntry> c) {
        boolean modified = false;
        for (PropositionEntry entry: c)
            modified |= add(entry);
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o: c) {
            modified |= remove(o);
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (PropositionEntry entry: propositions) {
            if (!c.contains(entry))
                modified |= remove(entry);
        }
        return modified;
    }

    @Override
    public void clear() {
        atomicsMap.clear();
        propositions.clear();
    }
}
