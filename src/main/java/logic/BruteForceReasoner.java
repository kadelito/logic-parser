package logic;

import common.PropositionEntry;
import common.operators.BinaryOperator;
import common.propositions.AtomicProposition;
import common.propositions.BinaryProposition;
import common.propositions.Proposition;

import java.util.*;

/**
 * An implementation of the {@link Reasoner} interface that, to draw conclusions,
 * considers every combination of truth values for all atomic propositions.
 */
public class BruteForceReasoner implements Reasoner {

    @Override
    public Boolean areEqual(PropositionEntry p1, PropositionEntry p2) {
        // Get list of all atomics with no duplicates
        Set<AtomicProposition> allAtomics = p1.atomics();
        allAtomics.addAll(p2.atomics());
        List<AtomicProposition> atomicsList = new ArrayList<>(allAtomics);

        Proposition p = p1.proposition();
        Proposition q = p2.proposition();

        // Check every combination to see if they are equal
        long numCombinations = 1L << atomicsList.size();
        if (numCombinations < atomicsList.size()) {
            return null;
        }
        for (long comb = numCombinations - 1; comb >= 0; comb--) {
            for (int i = atomicsList.size() - 1; i >= 0; i--) {
                boolean val = (comb >> i) % 2 == 1;
                atomicsList.get(i).setValue(val);
            }
            if (p.evaluate() != q.evaluate())
                return false;
        }
        return true;
    }

    @Override
    public Boolean isArgumentValid(PropositionEntry conclusion, Collection<PropositionEntry> premises) {
        if (premises.isEmpty())
            return null;

        // Get list of all relevant atomics with no duplicates
        Set<AtomicProposition> allAtomics = conclusion.atomics();
        for (PropositionEntry entry: premises)
            allAtomics.addAll(entry.atomics());
        List<AtomicProposition> atomicsList = new ArrayList<>(allAtomics);

        // Get conjunction of all premises
        Iterator<PropositionEntry> premiseIterator = premises.iterator();
        Proposition conjunction = premiseIterator.next().proposition();
        while (premiseIterator.hasNext())
            conjunction = new BinaryProposition(conjunction, premiseIterator.next().proposition(), BinaryOperator.AND);

        Proposition conclusionProp = conclusion.proposition();

        // Check every combination to see if they are equal
        long numCombinations = 1L << atomicsList.size();
        if (numCombinations < atomicsList.size()) {
            return null;
        }
        for (long comb = numCombinations - 1; comb >= 0; comb--) {
            for (int i = atomicsList.size() - 1; i >= 0; i--) {
                boolean val = (comb >> i) % 2 == 1;
                atomicsList.get(i).setValue(val);
            }
            // If (conjunction -> conclusion) evaluates to false
            if (!conclusionProp.evaluate() && conjunction.evaluate())
                return false;
        }
        return true;
    }
}
