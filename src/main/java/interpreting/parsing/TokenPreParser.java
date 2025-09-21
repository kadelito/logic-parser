package interpreting.parsing;

import interpreting.common.InterpretingResult;
import interpreting.tokenization.*;

import java.util.*;

/**
 * A class that "intercepts" a sequence of {@link Token Tokens},
 * converting a sequence with infixed operators to one in Reverse Polish Notation.
 * <p>
 * This class is used in {@link Parser} to facilitate building {@link common.propositions.Proposition Propositions}.
 * <p>
 * Input is assumed to follow the expected syntactic format.
 * If the input is invalid, this is handled as a normal part of control flow,
 * as an {@link InterpretingResult} will be returned containing an error message
 * rather than throwing an exception.
 * <p>
 * Internal errors (e.g. null tokens, logic bugs) are the only cases that may cause exceptions.
 * @see Parser
 */
public class TokenPreParser implements Iterable<InterpretingResult<Token>> {

    private Iterable<InterpretingResult<Token>> infixTokenSequence;

    /**
     * Instantiates a new Token pre parser.
     *
     * @param infixTokenSequence the infix token sequence to be used
     */
    public TokenPreParser(Iterable<InterpretingResult<Token>> infixTokenSequence) {
        this.infixTokenSequence = infixTokenSequence;
    }

    @Override
    public Iterator<InterpretingResult<Token>> iterator() {
        return new TokenRPNIterator(infixTokenSequence.iterator());
    }

    private static class TokenRPNIterator implements Iterator<InterpretingResult<Token>> {

        private Iterator<InterpretingResult<Token>> tokenSource;
        private Queue<Token> outputQueue;
        private Deque<Token> operatorStack;

        private boolean propositionExpected;
        private int activeOperands;

        private boolean errorEncountered;

        /**
         * Instantiates a new Token rpn iterator.
         *
         * @param tokens the tokens
         */
        public TokenRPNIterator(Iterator<InterpretingResult<Token>> tokens) {
            tokenSource = tokens;
            outputQueue = new LinkedList<>();
            operatorStack = new ArrayDeque<>();
            propositionExpected = true;
            activeOperands = 0;
            errorEncountered = false;
        }

        @Override
        public boolean hasNext() {
            if (errorEncountered)
                return false;
            return tokenSource.hasNext() || !operatorStack.isEmpty() || !outputQueue.isEmpty();
        }

        @Override
        public InterpretingResult<Token> next() {
            InterpretingResult<Token> result = nextOutput();
            if (result.value() == null)
                errorEncountered = true;
            return result;
        }

        private InterpretingResult<Token> nextOutput() {
            // Empty output queue if items remain
            if (!outputQueue.isEmpty())
                return new InterpretingResult<>(outputQueue.poll(), null);

            // Empty operator stack if items remain and are valid
            if (!tokenSource.hasNext()) {
                if (operatorStack.isEmpty())
                    throw new IllegalStateException("Method 'next()' called when no tokens are available");
                if (operatorStack.peek().getType() == TokenType.OPEN_PAREN)
                    return new InterpretingResult<>(null, "Open parenthesis was not closed");
                else
                    return new InterpretingResult<>(operatorStack.pop(), null);
            }

            // End early if error
            InterpretingResult<Token> inToken = tokenSource.next();
            if (inToken.value() == null)
                return new InterpretingResult<>(null, "Tokenization error: " + inToken.message());

            Token token = inToken.value();
            if (token.isProposition()) {
                if (!propositionExpected)
                    return new InterpretingResult<>(null, "Unexpected proposition");
                if (activeOperands > 0 && operatorStack.isEmpty())
                    return new InterpretingResult<>(null, "Identifier used without associated operator");
                activeOperands++;
                propositionExpected = false;
                return new InterpretingResult<>(token, null);
            }
            else if (token.isParen()) {
                if (token.getType() == TokenType.OPEN_PAREN) {
                    if (!propositionExpected)
                        return new InterpretingResult<>(null, "Unexpected open parenthesis");
                    operatorStack.push(token);
                }
                else { // curToken.type == TokenType.CLOSE_PAREN
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() != TokenType.OPEN_PAREN) {
                        activeOperands--;
                        outputQueue.add(operatorStack.pop());
                    }
                    if (operatorStack.isEmpty() || operatorStack.peek().getType() != TokenType.OPEN_PAREN)
                        return new InterpretingResult<>(null, "Incorrect use of closing parenthesis");
                    operatorStack.pop();
                    propositionExpected = false;
                }
                if (hasNext())
                    return next();
                else
                    return new InterpretingResult<>(null, "Token expected after closing parenthesis");
            }
            else if (token.isBinaryOperation()) {
                if (propositionExpected)
                    return new InterpretingResult<>(null, String.format("Binary operator '%s' found when proposition was expected", token));
                while (!operatorStack.isEmpty()) {
                    Token top = operatorStack.peek();
                    if (!(top.getType() != TokenType.OPEN_PAREN && (top.precedence() > token.precedence() || (
                            top.precedence() == token.precedence() && token.leftAssociative()))))
                        break;

                    if (top.isBinaryOperation())
                        activeOperands--;
                    outputQueue.add(operatorStack.pop());
                }
                propositionExpected = true;
                operatorStack.push(token);
                return next();
            }
            else if (token.isUnaryOperation()) {
                if (!propositionExpected)
                    return new InterpretingResult<>(null, "Unexpected unary operation");
                operatorStack.push(token);
                return next();
            }
            else throw new IllegalStateException("Unexpected token: " + token);
        }
    }
}
