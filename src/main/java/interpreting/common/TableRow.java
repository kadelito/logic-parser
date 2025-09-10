package interpreting.common;

import interpreting.tokenization.TokenType;
import operators.BinaryOperator;
import operators.UnaryOperator;

record TableRow(
        TokenType tokenType,
        String[] representations,
        // exactly one of binaryOperator and unaryOperator will be null
        BinaryOperator binaryOperator,
        UnaryOperator unaryOperator
) {
}
