package testing;

import interpreting.common.InterpretingResult;
import interpreting.tokenization.Token;

import java.util.Iterator;

public class SmartTokenGenerator implements Iterable<InterpretingResult<Token>> {

    private int lengthGoal;
    String[] atomics;

    public SmartTokenGenerator(int lengthGoal, String... atomics) {
        this.lengthGoal = lengthGoal;
        this.atomics = atomics;
    }

    @Override
    public Iterator<InterpretingResult<Token>> iterator() {
        return new SmartTokenIterator(lengthGoal, atomics);
    }
}
