package testing;

import interpreting.common.InterpretingResult;
import interpreting.tokenization.Token;
import interpreting.tokenization.TokenType;

import java.util.Iterator;

class RandomTokenIterator implements Iterator<InterpretingResult<Token>> {

    private int numLeft;
    private String[] atomics;

    public RandomTokenIterator(int numLeft, String[] atomics) {
        this.numLeft = numLeft;
        this.atomics = atomics;
    }

    @Override
    public boolean hasNext() {
        return numLeft > 0;
    }

    @Override
    public InterpretingResult<Token> next() {
        numLeft--;
        return getRandomToken();
    }

    private InterpretingResult<Token> getRandomToken() {
        TokenType type = switch ((int) (Math.random() * 10)) {
            case 0 -> TokenType.OPEN_PAREN;
            case 1 -> TokenType.CLOSE_PAREN;
            case 2, 8, 9 -> TokenType.IDENTIFIER;
            case 3 -> TokenType.AND;
            case 4 -> TokenType.OR;
            case 5 -> TokenType.IMPLY;
            case 6 -> TokenType.BICONDITIONAL;
            case 7 -> TokenType.NOT;
//                    case 8 -> TokenType.TRUE;
//                    case 9 -> TokenType.FALSE;
            default -> throw new RuntimeException();
        };
        String data = (type == TokenType.IDENTIFIER) ? atomics[(int) (Math.random() * atomics.length)] : null;
        return new InterpretingResult<>(new Token(type, data), null);
    }
}
