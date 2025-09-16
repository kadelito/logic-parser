package logic;

import interpreting.parsing.PropositionProcessor;
import common.propositions.AtomicProposition;
import common.propositions.BinaryProposition;
import common.propositions.Proposition;
import common.propositions.UnaryProposition;

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

    public void printTruthTable(int index) {

        Proposition proposition = propositions.get(index);
        List<AtomicProposition> atomicList = new ArrayList<>(getAtomics(index));

        String treeRepr = proposition.repr();
        int reprLen = treeRepr.length();
        int atomicsLen = 2;
        List<Integer> atomicLengths = new ArrayList<>(atomicList.size());

        // Print atomics and entire proposition
        StringBuilder topRow = new StringBuilder(" ");
        for (int i = atomicList.size() - 1; i >= 0; i--) {
            String aRepr = atomicList.get(i).repr();
            topRow.append(aRepr).append(" | ");
            atomicLengths.add(aRepr.length());
            atomicsLen += aRepr.length() + 3;
        }
        System.out.print(justifyCenter("Atomics", atomicsLen - 3));
        System.out.println("| " + justifyCenter("Proposition", reprLen + 1));
        topRow.append(treeRepr);
        System.out.println(topRow);

        // Divider
        for (int i = 0; i < atomicsLen + reprLen; i++) {
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
            for (int i = atomicList.size() - 1; i >= 0; i--) {
                boolean val = (comb >> i) % 2 == 1;
                atomicList.get(i).setValue(val);
                System.out.printf(justifyCenter(val ? "T" : "F", atomicLengths.get(i) + 2) + "|");
            }
            System.out.println(justifyCenter(proposition.evaluate() ? "T" : "F", reprLen + 1));
        }
    }

    // Helper method for printTruthTable
    private String justifyCenter(String str, int width) {
        if (str.length() >= width) return str;
        int padding = (width - str.length()) / 2;
        return String.format("%" + (padding + str.length()) + "s%" + (width - padding - str.length()) + "s", str, "");
    }

    // Returns the atomic propositions used in the proposition at given index
    private Set<AtomicProposition> getAtomics(int index) {
        Set<AtomicProposition> atomics = new HashSet<>();
        Deque<Proposition> stack = new ArrayDeque<>();
        Proposition current = propositions.get(index);
        while (true) {
            switch (current) {
                case BinaryProposition b:
                    stack.add(b.getRightProposition());
                    current = b.getLeftProposition();
                    break;

                case UnaryProposition u:
                    current = u.getProposition();
                    break;

                case AtomicProposition a:
                    if (a != Proposition.TRUE && a != Proposition.FALSE)
                        atomics.add(a);
                    if (stack.isEmpty())
                        return atomics;
                    else
                        current = stack.pop();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + current);
            }
        }
    }

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
