package testing;

import interpreting.common.InterpretingResult;
import interpreting.tokenization.Token;

import java.util.Iterator;

class RandomTokenGenerator implements Iterable<InterpretingResult<Token>> {

    private boolean isRandomNum;
    private int numTokens;
    private int minTokens;
    private int maxTokens;

    private String[] atomics;

    public RandomTokenGenerator(int numTokens, String... atomics) {
        this.numTokens = numTokens;
        this.isRandomNum = false;
        this.atomics = atomics;
    }

    public RandomTokenGenerator(int minTokens, int maxTokens, String... atomics) {
        this.isRandomNum = true;
        this.minTokens = minTokens;
        this.maxTokens = maxTokens;
        this.atomics = atomics;
    }

    @Override
    public Iterator<InterpretingResult<Token>> iterator() {
        return new RandomTokenIterator(isRandomNum ? (int)(minTokens + Math.random() * (maxTokens - minTokens)) : numTokens, atomics);
    }

}
