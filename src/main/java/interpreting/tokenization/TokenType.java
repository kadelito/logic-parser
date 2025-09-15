package interpreting.tokenization;

public enum TokenType {
    OPEN_PAREN,
    CLOSE_PAREN,
    IDENTIFIER,
    AND(3),
    OR(2),
    IMPLY(1),
    BICONDITIONAL(0),
    NOT(4, false),
    TRUE,
    FALSE;

    final Integer precedence;
    final Boolean leftAssociative;

    TokenType(Integer precedence, Boolean leftAssociative) {
        this.precedence = precedence;
        this.leftAssociative = leftAssociative;
    }

    TokenType(Integer precedence) {this(precedence, true);}

    TokenType() {
        this(null, null);
    }

    int getCategory() {
        return switch (this) {
            case IDENTIFIER -> 0;
            case TRUE, FALSE -> 1;                  // Constants
            case OPEN_PAREN, CLOSE_PAREN -> 2;      // Parentheses
            case AND, OR, IMPLY, BICONDITIONAL -> 3;// Binary operators
            case NOT -> 4;                          // Unary operator
            default -> -1;
        };
    }
}