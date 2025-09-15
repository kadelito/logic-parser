package testing;

import interpreting.common.InterpretingResult;
import interpreting.tokenization.Token;
import interpreting.tokenization.TokenType;

import java.util.Iterator;

class SmartTokenIterator implements Iterator<InterpretingResult<Token>> {
    String[] availableNames;

    private boolean expectingProposition;
    private int curDepth;

    private int tokensLeft;
    private boolean shouldEndItAlready;

    public SmartTokenIterator(int tokensLeft, String[] availableNames) {
        this.tokensLeft = tokensLeft;
        this.availableNames = availableNames;
        this.expectingProposition = true;
        this.curDepth = 0;
        this.shouldEndItAlready = false;
    }

    @Override
    public boolean hasNext() {
        return tokensLeft > 0 || expectingProposition || curDepth > 0;
    }

    @Override
    public InterpretingResult<Token> next() {
        if (--tokensLeft < 0) shouldEndItAlready = true;
        if (expectingProposition)
            return doRandomProposition();
        else {
            return doRandomOperation();
        }
    }

    private InterpretingResult<Token> doRandomProposition() {
        Token output;
        switch ((int)(Math.random() * (shouldEndItAlready ? 4 : 5))) {
            case 0: // Add atomic or constant
            case 1:
            case 2:
                output = switch ((int)(Math.random() * 5)) {
                    case 0 -> new Token(TokenType.TRUE);
                    case 1 -> new Token(TokenType.FALSE);
                    case 2,3,4 -> new Token(TokenType.IDENTIFIER, randomAtomic());
                    default -> throw new IllegalStateException("Unexpected value: " + (int)(Math.random() * 3));
                };
                expectingProposition = false;
                break;
            case 3:
                output = new Token(TokenType.NOT);
                break;
            case 4:
                output = new Token(TokenType.OPEN_PAREN);
                curDepth++;
                break;

            default: throw new RuntimeException();
        }
        return new InterpretingResult<>(output, null);
    }

    private String randomAtomic() {
        return availableNames[(int)(Math.random() * availableNames.length)];
    }

    private InterpretingResult<Token> doRandomOperation() {
        Token output;
        if (shouldEndItAlready && curDepth > 0) {
            curDepth--;
            return new InterpretingResult<>(new Token(TokenType.CLOSE_PAREN), null);
        }
        switch ((int)(Math.random() * (curDepth > 0 ? 2 : 1))) {
            case 0:
                output = new Token(switch ((int)(Math.random() * 4)) {
                    case 0 -> TokenType.AND;
                    case 1 -> TokenType.OR;
                    case 2 -> TokenType.IMPLY;
                    case 3 -> TokenType.BICONDITIONAL;
                    default -> throw new IllegalStateException("Unexpected value");
                });
                expectingProposition = true;
                break;
            case 1:
                output = new Token(TokenType.CLOSE_PAREN);
                curDepth--;
                break;
            default: throw new IllegalStateException("Unexpected value");
        }
        return new InterpretingResult<>(output, null);
    }
}
