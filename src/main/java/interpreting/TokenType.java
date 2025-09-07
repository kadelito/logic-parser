package interpreting;

public enum TokenType {
    OPEN_PAREN,
    CLOSE_PAREN,
    IDENTIFIER,
    AND,
    OR,
    IMPLY,
    BICONDITIONAL,
    NOT,
    TRUE,
    FALSE;

    int getCategory() {
        return switch (this) {
            case IDENTIFIER -> 0;
            case TRUE, FALSE -> 1;                  // Constants
            case OPEN_PAREN, CLOSE_PAREN -> 2;      // Parentheses
            case AND, OR, IMPLY, BICONDITIONAL -> 3;// Binary operators
            case NOT -> 4;                          // Unary operator
        };
    }
}