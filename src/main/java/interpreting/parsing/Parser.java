package interpreting.parsing;

import common.propositions.AtomicProposition;
import common.propositions.BinaryProposition;
import common.propositions.Proposition;
import common.propositions.UnaryProposition;
import interpreting.common.InterpretingResult;
import interpreting.common.PropositionConstructionResult;
import interpreting.tokenization.Token;

import java.util.*;

public class Parser {

    private Iterable<InterpretingResult<Token>> RPNTokenSequence;
    private Map<String, AtomicProposition> atomicMap;

    public Parser(Iterable<InterpretingResult<Token>> infixTokenSequence) {
        RPNTokenSequence = new TokenPreParser(infixTokenSequence);
    }

    public Parser(Iterable<InterpretingResult<Token>> infixTokenSequence, Set<AtomicProposition> atomicContext) {
        this(infixTokenSequence);
        for (AtomicProposition a: atomicContext)
            atomicMap.put(a.repr(), a);
    }

    // Generate propositional treeOutput from RPN queue of tokens
    public PropositionConstructionResult buildPropositionTree() {
        atomicMap = new HashMap<>();
        Set<AtomicProposition> newAtomics = new HashSet<>();
        Stack<Proposition> propositionStack = new Stack<>();

        for (InterpretingResult<Token> inToken: RPNTokenSequence) {
            Token token = inToken.value();

            if (token == null)
                return new PropositionConstructionResult(null, null, "Token error: " + inToken.message());

            if (token.isConstant())
                propositionStack.add(switch(token.type) {
                    case TRUE -> Proposition.TRUE;
                    case FALSE -> Proposition.FALSE;
                    default -> throw new RuntimeException("Non-constant token was considered constant");
                });
            else if (token.isIdentifier()) {
                propositionStack.add(atomicMap.computeIfAbsent(token.data, str -> new AtomicProposition(token.data)));
                newAtomics.add(atomicMap.get(token.data));
            }
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
            return new PropositionConstructionResult(null, null,
                    propositionStack.isEmpty() ? "No propositions found" : "More than 1 proposition found");

        return new PropositionConstructionResult(propositionStack.pop(), newAtomics, null);
    }
}
