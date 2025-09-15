package interpreting.tokenization;

import interpreting.common.RepresentationTable;
import operators.BinaryOperator;
import operators.UnaryOperator;

public class Token {
    private static RepresentationTable repTable = RepresentationTable.getInstance();
    public TokenType type;
    // field 'data' only for identifiers
    public String data;

    public Token(TokenType type, String data) {
        this.type = type;
        this.data = data;
    }

    public Token(TokenType type) {
        this(type, null);
    }

    public BinaryOperator getBinaryOperator() {
        return switch (type) {
            case TokenType.AND -> BinaryOperator.AND;
            case TokenType.OR -> BinaryOperator.OR;
            case TokenType.IMPLY -> BinaryOperator.IMPLY;
            case TokenType.BICONDITIONAL -> BinaryOperator.BICONDITIONAL;
            default -> null;
        };
    }

    public UnaryOperator getUnaryOperator() {
        return type == TokenType.NOT ? UnaryOperator.NOT : null;
    }

    public int numOperands() {
        return switch (type.getCategory()) {
            case 3 -> 2;
            case 4 -> 1;
            default -> 0;
        };
    }

    public int precedence() {return type.precedence;}

    public boolean leftAssociative() {return type.leftAssociative;}

    public boolean isProposition() {
        return isIdentifier() || isConstant();
    }

    public boolean isIdentifier() {
        return type.getCategory() == 0;
    }

    public boolean isConstant() {
        return type.getCategory() == 1;
    }

    public boolean isParen() {
        return type.getCategory() == 2;
    }

    public boolean isBinaryOperation() {
        return type.getCategory() == 3;
    }

    public boolean isUnaryOperation() {
        return type.getCategory() == 4;
    }

    public String toString() {
        return String.format("%s%s", repTable.getRepresentation(type), type == TokenType.IDENTIFIER ? (": '" + data + '\'') : "");
    }
}
