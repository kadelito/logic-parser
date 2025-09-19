package logic;

import common.PropositionEntry;
import common.propositions.AtomicProposition;

import java.util.*;

public class PropositionRegistry implements Collection<PropositionEntry> {
    private List<PropositionEntry> propositions;
    private Set<AtomicProposition> atomics;

    public PropositionRegistry() {
        atomics = new HashSet<>();
        propositions = new ArrayList<>();
    }

    public PropositionEntry getEntry(int index) {return propositions.get(index);}

    public Set<AtomicProposition> getAtomics() {
        return atomics;
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
        atomics.addAll(entry.atomics());
        return propositions.add(entry);
    }

    @Override
    public boolean remove(Object o) {
        return propositions.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return propositions.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends PropositionEntry> c) {
        return propositions.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return propositions.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return propositions.retainAll(c);
    }

    @Override
    public void clear() {
        propositions.clear();
    }
}
