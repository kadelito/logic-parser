package logic;

import interpreting.parsing.PropositionProcessor;
import propositions.AtomicProposition;
import propositions.Proposition;

import java.util.*;

public class LogicContext implements Collection<Proposition> {
    private List<Proposition> propositions;
    private Set<AtomicProposition> atomics;

    public LogicContext() {
        atomics = new HashSet<>();
        propositions = new ArrayList<>();
    }

    public void updateSelf(PropositionProcessor processor) {
        if (processor.generateSucceeded()) {
            propositions.add(processor.getLastProposition());
            atomics.addAll(processor.getLastAtomicSet());
        }
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public Set<AtomicProposition> getAtomics() {
        return atomics;
    }

    public void printTruthTable() {
        printTruthTable(propositions.size() - 1);
    }

    public void printTruthTable(int index) {

        Proposition proposition = propositions.get(index);
        List<AtomicProposition> atomicList = new ArrayList<>(atomics);

        String treeRepr = proposition.repr();
        int reprLen = treeRepr.length();
        int lineLen = 2 + reprLen;
        List<Integer> atomicLengths = new ArrayList<>(atomicList.size());

        // Print atomics and entire proposition
        StringBuilder topRow = new StringBuilder(" ");
        for (AtomicProposition a: atomicList) {
            String aRepr = a.repr();
            topRow.append(aRepr).append(" | ");
            atomicLengths.add(aRepr.length());
            lineLen += aRepr.length() + 3;
        }
        topRow.append(treeRepr);
        System.out.println(topRow);

        // Divider
        for (int i = 0; i < lineLen; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Truth Table
        long numCombinations = 1L << atomicList.size();
        // Check for overflow
        if (numCombinations < atomicList.size()) {
            System.out.println("Too many atomic propositions! (" + atomicList.size() + ")");
            return;
        }
        for (long comb = numCombinations - 1; comb >= 0; comb--) {
            for (int i = 0; i < atomicList.size(); i++) {
                boolean val = (comb >> i) % 2 == 1;
                atomicList.get(atomicList.size() - i - 1).setValue(val);
                System.out.printf(" %" + atomicLengths.get(i) + "s |", val ? "T" : "F");
            }
            System.out.println(justifyCenter(proposition.evaluate() ? "T" : "F", reprLen + 1));
        }
    }

    // Helper method for printTruthTable
    private String justifyCenter(String str, int width) {
        if (str.length() >= width) return str;
        int padding = (width - str.length()) / 2;
        return String.format("%" + (padding + str.length()) + "s", str);
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
    public Iterator<Proposition> iterator() {
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
    public boolean add(Proposition proposition) {
        return propositions.add(proposition);
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
    public boolean addAll(Collection<? extends Proposition> c) {
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
