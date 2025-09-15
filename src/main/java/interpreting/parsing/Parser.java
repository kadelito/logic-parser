package interpreting.parsing;

import interpreting.common.InterpretingResult;
import interpreting.common.PropositionConstructionResult;
import interpreting.tokenization.Lexer;
import interpreting.tokenization.Token;
import propositions.*;

import java.util.*;

public class Parser {

    private Iterable<InterpretingResult<Token>> tokenStreamRPN;

    public Parser(Lexer lexer) {
        tokenStreamRPN = new TokenPreParser(lexer);
    }

    // Generate propositional treeOutput from RPN queue of tokens
    public PropositionConstructionResult buildPropositionTree() {
        Map<String, AtomicProposition> atomicMap = new HashMap<>();
        Stack<Proposition> propositionStack = new Stack<>();

        for (InterpretingResult<Token> inToken: tokenStreamRPN) {
            Token token = inToken.value();

            if (token == null)
                return new PropositionConstructionResult(null, null, "Token error: " + inToken.message());

            if (token.isConstant())
                propositionStack.add(switch(token.type) {
                    case TRUE -> Proposition.TRUE;
                    case FALSE -> Proposition.FALSE;
                    default -> throw new RuntimeException("Non-constant token was considered constant");
                });
            else if (token.isIdentifier())
                propositionStack.add(atomicMap.computeIfAbsent(token.data, str -> new AtomicProposition(token.data)));
            else if (token.isBinaryOperation()) {
                // Last two propositions are reversed to retain original order
                if (propositionStack.size() < 2)
                    return new PropositionConstructionResult(null, null, "Binary operator does not have two propositions");
                Proposition p2 = propositionStack.pop();
                Proposition p1 = propositionStack.pop();
                Proposition compound = new BinaryProposition(p1, p2, token.getBinaryOperator());
                propositionStack.add(compound);
            }
            else if (token.isUnaryOperation()) {
                if (propositionStack.isEmpty())
                    return new PropositionConstructionResult(null, null, "Unary operator does not have a proposition");
                Proposition p = propositionStack.pop();
                Proposition unary = new UnaryProposition(p, token.getUnaryOperator());
                propositionStack.add(unary);
            }
            else return new PropositionConstructionResult(null, null, "Unexpected token found");
        }
        if (propositionStack.size() != 1)
            return new PropositionConstructionResult(null, null, "More than 1 proposition found");

        return new PropositionConstructionResult(propositionStack.pop(), new HashSet<>(atomicMap.values()), null);
    }
}
