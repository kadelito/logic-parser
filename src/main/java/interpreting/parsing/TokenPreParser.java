package interpreting.parsing;

import interpreting.common.InterpretingResult;
import interpreting.tokenization.*;

import java.util.*;

public class TokenPreParser implements Iterable<InterpretingResult<Token>> {

    private Iterable<InterpretingResult<Token>> tokenStream;

    public TokenPreParser(Iterable<InterpretingResult<Token>> lexer) {
        tokenStream = lexer;
    }

    @Override
    public Iterator<InterpretingResult<Token>> iterator() {
        return new TokenRPNIterator(tokenStream.iterator());
    }

    private class TokenRPNIterator implements Iterator<InterpretingResult<Token>> {

        private Iterator<InterpretingResult<Token>> tokenSource;
        private Queue<Token> outputQueue;
        private Deque<Token> operatorStack;

        private boolean propositionExpected;
        private int activeOperands;

        private boolean errorEncountered;

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
                if (operatorStack.peek().type == TokenType.OPEN_PAREN)
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
                if (token.type == TokenType.OPEN_PAREN) {
                    if (!propositionExpected)
                        return new InterpretingResult<>(null, "Unexpected open parenthesis");
                    operatorStack.push(token);
                }
                else { // curToken.type == TokenType.CLOSE_PAREN
                    while (!operatorStack.isEmpty() && operatorStack.peek().type != TokenType.OPEN_PAREN) {
                        activeOperands--;
                        outputQueue.add(operatorStack.pop());
                    }
                    if (operatorStack.isEmpty() || operatorStack.peek().type != TokenType.OPEN_PAREN)
                        return new InterpretingResult<>(null, "Incorrect use of closing parenthesis");
                    operatorStack.pop();
                    propositionExpected = false;
                    // TODO?: add support for function tokens
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
                    if (!(top.type != TokenType.OPEN_PAREN && (top.precedence() > token.precedence() || (
                            top.precedence() == token.precedence() && token.leftAssociative()))))
                        break;

                    if (top.numOperands() == 2)
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
