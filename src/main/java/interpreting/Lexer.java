package interpreting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Lexer {
    private static final RepresentationTable repTable = RepresentationTable.getInstance();

    private String input;
    private InterpretingResult<List<Token>> tokens;
                                                                       
    public Lexer(String input) {
        this.input = input;
    }

    public void tokenize() {
        tokens = new InterpretingResult<>(new ArrayList<>(), null);

        // Iterate through "starting" characters of each substring
        // that corresponds to a single token
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            if (!repTable.isAllowed(c)) {
                setInputError(i, "Invalid character: " + c);
                return;
            }

            // Skip whitespaces
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // Handle parentheses
            else if (c == '(')
                tokens.value().add(new Token(TokenType.OPEN_PAREN));
            else if (c == ')')
                tokens.value().add(new Token(TokenType.CLOSE_PAREN));

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
                    tokens.value().add(new Token(otherTokenType));
                else
                    tokens.value().add(new Token(TokenType.IDENTIFIER, name));
            }
            // Invalid identifier characters mark miscellaneous tokens (or beginnings of them)
            else  { // Character.isLetterOrDigit(c) is false
                String buffer = "";
                Set<TokenType> possible = new HashSet<>();
                while (i < input.length()) {
                    c = input.charAt(i);

                    possible = repTable.getPossibleTokenTypes(buffer + c);

                    if (possible.isEmpty()) {
                        if (buffer.isEmpty()) {
                            setInputError(i, "Invalid character: '" + c + '\'');
                            return;
                        }

                        TokenType correspondingType = repTable.getTokenType(buffer);
                        if (correspondingType != null)
                            tokens.value().add(new Token(correspondingType));
                        else {
                            setInputError(i, "Invalid sequence: \"" + buffer + c + '\"');
                            return;
                        }
                        i--;
                        break;
                    }
                    buffer += c;
                    i++;
                }
                // Don't forget final token
                if (i >= input.length()) {
                    if (possible.isEmpty()) {
                        setInputError(i, "Invalid sequence: \"" + buffer + '\"');
                        return;
                    }
                    else {
                        TokenType correspondingType = repTable.getTokenType(buffer);
                        if (correspondingType != null)
                            tokens.value().add(new Token(correspondingType));
                        else {
                            setInputError(i, "Invalid sequence: \"" + buffer + c + '\"');
                            return;
                        }
                    }
                }
            }
            i++;
        }
    }


    private void setInputError(int index, String message) {
        tokens = new InterpretingResult<>(null, String.format("\n%s\n%" + (index + 1) + "s\n%s", input, "^", message));
    }

    public InterpretingResult<List<Token>> getTokens() {
        return tokens;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
