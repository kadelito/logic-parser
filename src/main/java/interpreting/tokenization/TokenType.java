package interpreting.tokenization;

public enum TokenType {
    IDENTIFIER(0),
    TRUE(1),
    FALSE(1),
    OPEN_PAREN(2),
    CLOSE_PAREN(2),
    AND(3, 3, true),
    OR(3, 2, true),
    IMPLY(3, 1, true),
    BICONDITIONAL(3, 0, true),
    NOT(4, 4, null);

    final int category;
    final Integer precedence;
    final Boolean leftAssociative;

    TokenType(int category, Integer precedence, Boolean leftAssociative) {
        this.category = category;
        this.precedence = precedence;
        this.leftAssociative = leftAssociative;
    }

    TokenType(int category) {this(category, null, null);}

    int getCategory() {
        return category;
    }
}