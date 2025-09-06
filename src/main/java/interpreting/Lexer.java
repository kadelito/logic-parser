package interpreting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Lexer {
    private static final RepresentationTable repTable = RepresentationTable.getInstance();

    private String input;
    private List<Token> tokens;

    public Lexer(String input) {
        this.input = input;
    }

    public void tokenize() {
        tokens = new ArrayList<>();

        // Iterate through "starting" characters of each substring
        // that corresponds to a single token
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            if (!repTable.isAllowed(c))
                inputError(i, "Invalid character: " + c);

            // Skip whitespaces
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // Handle parentheses
            else if (c == '(')
                tokens.add(new Token(TokenType.OPEN_PAREN));
            else if (c == ')')
                tokens.add(new Token(TokenType.CLOSE_PAREN));

            // Non-alphanumeric characters mark miscellaneous tokens (or beginnings of them)
            else if (!Character.isLetterOrDigit(c)) {
                String buffer = "";
                Set<TokenType> possible = new HashSet<>();
                while (i < input.length()) {
                    c = input.charAt(i);

                    possible = repTable.getPossibleTokenTypes(buffer + c);

                    if (possible.isEmpty()) {
                        if (buffer.isEmpty())
                            inputError(i, "Invalid character: '" + c + '\'');

                        TokenType correspondingType = repTable.getTokenType(buffer);
                        if (correspondingType != null)
                            tokens.add(new Token(correspondingType));
                        else
                            inputError(i, "Invalid sequence: \"" + buffer + c + '\"');
                        i--;
                        break;
                    }
                    buffer += c;
                    i++;
                }
                // Don't forget final token
                if (i >= input.length())
                    if (possible.isEmpty())
                        inputError(i, "Invalid sequence: \"" + buffer + '\"');
                    else {
                        TokenType firstPossibleType = possible.iterator().next();
                        tokens.add(new Token(firstPossibleType));
                    }
            }

            // Add identifiers for contiguous alphanumeric substrings
            else  { // Character.isLetterOrDigit(c) is true
                StringBuilder ident = new StringBuilder();
                while (i < input.length() && Character.isLetterOrDigit(c = input.charAt(i))) {
                    ident.append(c);
                    i++;
                }
                String name = ident.toString();
                if (name.equals("T") || name.equals("1"))
                    tokens.add(new Token(TokenType.TRUE, "T"));
                else if (name.equals("F")|| name.equals("0"))
                    tokens.add(new Token(TokenType.FALSE, "F"));
                else
                    tokens.add(new Token(TokenType.IDENTIFIER, name));
                i--;
            }
            i++;
        }
        tokens.add(new Token(TokenType.EOL));
    }

    public void printTokens() {
        for (Token token: tokens) {
            if (token.type == TokenType.IDENTIFIER)
                System.out.print(token.data);
            else if (token.type != TokenType.EOL)
                System.out.print(repTable.getRepresentation(token.type));
//            System.out.print(" ");
        }
//        System.out.println(" \\\\");
    }

    private void inputError(int index, String message) {
        throw new RuntimeException(
                String.format("\n%s\n%" + (index + 1) + "s\n%s", input, "^", message)
        );
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
