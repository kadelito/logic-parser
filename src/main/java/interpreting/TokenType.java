package interpreting;

public enum TokenType {
    OPEN_PAREN,
    CLOSE_PAREN,
    IDENTIFIER,
    AND(3),
    OR(2),
    IMPLY(1),
    BICONDITIONAL(0),
    NOT(4),
    TRUE,
    FALSE;

    public final Integer precedence;

    TokenType(Integer precedence) {
        this.precedence = precedence;
    }

    TokenType() {
        this(null);
    }

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