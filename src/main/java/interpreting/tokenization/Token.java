package interpreting.tokenization;

import interpreting.common.RepresentationTable;
import common.operators.BinaryOperator;
import common.operators.UnaryOperator;

/**
 * A class representing a single unit of data after lexical analysis.
 * @see TokenType
 * @see Lexer
 */
public class Token {
    private static RepresentationTable repTable = RepresentationTable.getInstance();

    private final TokenType type;
    private final String text;

    /**
     * Instantiates a new Token with the specified type and text.
     * <p>
     * This constructor is typically only used with <code>IDENTIFIER</code> tokens.
     *
     * @param type the type
     * @param text the text
     */
    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    /**
     * Instantiates a new Token with the specified type.
     *
     * @param type the type
     */
    public Token(TokenType type) {
        this(type, null);
    }

    /**
     * Returns a numerical representation of the operator's precedence.
     *
     * @throws NullPointerException if called on a token that isn't an operator
     */
    public int precedence() throws NullPointerException {return type.precedence;}

    /**
     * Returns <code>true</code> if the operator is left-associative,
     * <code>false</code> if right-associaive.
     *
     * @throws NullPointerException if called on a token that isn't a binary operator
     */
    public boolean leftAssociative() throws NullPointerException {return type.leftAssociative;}

    /**
     * Returns whether the token represents some proposition (Typically atomic).
     */
    public boolean isProposition() {
        return isIdentifier() || isConstant();
    }

    /**
     * Returns whether the token represents an identifier.
     */
    public boolean isIdentifier() {
        return type.getCategory() == 0;
    }

    /**
     * Returns whether the token represents a constant.
     */
    public boolean isConstant() {
        return type.getCategory() == 1;
    }

    /**
     * Returns whether the token represents a parenthesis.
     */
    public boolean isParen() {
        return type.getCategory() == 2;
    }

    /**
     * Returns whether the token represents a binary operation.
     */
    public boolean isBinaryOperation() {
        return type.getCategory() == 3;
    }

    /**
     * Returns whether the token represents a unary operation.
     */
    public boolean isUnaryOperation() {
        return type.getCategory() == 4;
    }

    /**
     * Returns the current type.
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Returns the current text,
     * typically corresponding to an identifier token.
     */
    public String getText() {
        return text;
    }

    /**
     * If the token is an identifier, returns the associated text.
     * Otherwise, returns a representation as determined by {@link RepresentationTable}.
     */
    @Override
    public String toString() {
        return type == TokenType.IDENTIFIER ? text : repTable.getRepresentation(type);
    }
}
