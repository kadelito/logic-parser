package interpreting.tokenization;

import interpreting.common.InterpretingResult;
import interpreting.common.RepresentationTable;

import java.util.*;

public class Lexer implements Iterable<InterpretingResult<Token>> {

    private static RepresentationTable repTable = RepresentationTable.getInstance();
    private String input;

    public Lexer(String input) {
        this.input = input;
    }

    @Override
    public Iterator<InterpretingResult<Token>> iterator() {
        return new LexerIterator();
    }

    public void setInput(String input) {
        this.input = input;
    }

    private class LexerIterator implements Iterator<InterpretingResult<Token>> {
        
        private int i;

        private boolean errorEncountered;

        LexerIterator() {
            i = -1; // Offset initial increment
            errorEncountered = false;
        }
    
        @Override
        public boolean hasNext() {
            if (errorEncountered)
                return false;
            for (int j = i + 1; j < input.length(); j++) {
                if (!Character.isWhitespace(input.charAt(j)))
                    return true;
            }
            return false;
        }
    
        @Override
        public InterpretingResult<Token> next() {
            InterpretingResult<Token> result = nextOutput();
            if (result.value() == null)
                errorEncountered = true;
            return result;
        }

        private InterpretingResult<Token> nextOutput() {

            char c;
            do {
                i++;
                c = input.charAt(i);
            } while (Character.isWhitespace(c));

            if (!repTable.isAllowed(c)) {
                return inputErrorResult(i, "Invalid character: " + c);
            }
    
            // Handle parentheses
            else if (c == '(')
                return new InterpretingResult<>(new Token(TokenType.OPEN_PAREN), null);
            else if (c == ')')
                return new InterpretingResult<>(new Token(TokenType.CLOSE_PAREN), null);
    
            // Add identifier
            else if (repTable.validIdentifierChar(c)) {
                StringBuilder ident = new StringBuilder();
                while (i < input.length() && repTable.validIdentifierChar(c = input.charAt(i))) {
                    ident.append(c);
                    i++;
                }
                i--;
                String name = ident.toString();
    
                // If already a token, add that instead of an identifier
                // For example 'T' would become a TRUE token instead of an identifier
                TokenType otherTokenType = repTable.getTokenType(name);
                if (otherTokenType != null)
                    return new InterpretingResult<>(new Token(otherTokenType), null);
                else
                    return new InterpretingResult<>(new Token(TokenType.IDENTIFIER, name), null);
            }
            // Non-identifier characters must be a miscellaneous token
            else  { // Character.isLetterOrDigit(c) is false
                String buffer = "";
                Set<TokenType> possible = new HashSet<>();
                while (i < input.length()) {
                    c = input.charAt(i);

                    possible = repTable.getPossibleTokenTypes(buffer + c);

                    if (possible.isEmpty()) {
                        if (buffer.isEmpty())
                            return inputErrorResult(i, "Invalid character: '" + c + '\'');

                        i--;
                        TokenType correspondingType = repTable.getTokenType(buffer);
                        if (correspondingType != null)
                            return new InterpretingResult<>(new Token(correspondingType), null);
                        else
                            return inputErrorResult(i, "Invalid sequence: \"" + buffer + c + '\"');
                    }
                    buffer += c;
                    i++;
                }
                // Don't forget final token
                if (possible.isEmpty()) {
                    return inputErrorResult(i, "Invalid sequence: \"" + buffer + '\"');
                } else {
                    TokenType correspondingType = repTable.getTokenType(buffer);
                    if (correspondingType != null)
                        return new InterpretingResult<>(new Token(correspondingType), null);
                    else {
                        return inputErrorResult(i, "Invalid sequence: \"" + buffer + c + '\"');
                    }
                }
            }
        }
        
        private InterpretingResult<Token> inputErrorResult(int index, String message) {
            return new InterpretingResult<>(null, String.format("\n%s\n%" + (index + 1) + "s\n%s", input, "^", message));
        }
    }
}
