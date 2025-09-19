package interpreting.parsing;

import common.PropositionEntry;
import common.propositions.*;
import interpreting.common.InterpretingResult;
import interpreting.tokenization.Token;

import java.util.*;

public class Parser {

    private Iterable<InterpretingResult<Token>> RPNTokenSequence;
    private Map<String, AtomicProposition> atomicMap;

    public Parser(Iterable<InterpretingResult<Token>> infixTokenSequence) {
        RPNTokenSequence = new TokenPreParser(infixTokenSequence);
        atomicMap = new HashMap<>();
    }

    public Parser(Iterable<InterpretingResult<Token>> infixTokenSequence, Set<AtomicProposition> atomicContext) {
        this(infixTokenSequence);
        for (AtomicProposition a: atomicContext)
            atomicMap.put(a.toString(), a);
    }

    // Generate propositional treeOutput from RPN sequence of tokens
    public InterpretingResult<PropositionEntry> buildPropositionTree() {
        Set<AtomicProposition> newAtomics = new HashSet<>();
        Stack<Proposition> propositionStack = new Stack<>();

        for (InterpretingResult<Token> inToken: RPNTokenSequence) {
            Token token = inToken.value();

            if (token == null)
                return new InterpretingResult<>(null, "Token error: " + inToken.message());

            if (token.isConstant())
                propositionStack.add(switch(token.type) {
                    case TRUE -> Proposition.getTrue();
                    case FALSE -> Proposition.getFalse();
                    default -> throw new RuntimeException("Non-constant token was considered constant");
                });
            else if (token.isIdentifier()) {
                propositionStack.add(atomicMap.computeIfAbsent(token.data, str -> new AtomicProposition(token.data)));
                newAtomics.add(atomicMap.get(token.data));
            }
            else if (token.isBinaryOperation()) {
                // Last two propositions are reversed to retain original order
                if (propositionStack.size() < 2)
                    return new InterpretingResult<>(null,  "Binary operator does not have two propositions");
                Proposition p2 = propositionStack.pop();
                Proposition p1 = propositionStack.pop();
                Proposition compound = new BinaryProposition(p1, p2, token.getBinaryOperator());
                propositionStack.add(compound);
            }
            else if (token.isUnaryOperation()) {
                if (propositionStack.isEmpty())
                    return new InterpretingResult<>(null,  "Unary operator does not have a proposition");
                Proposition p = propositionStack.pop();
                Proposition unary = new UnaryProposition(p, token.getUnaryOperator());
                propositionStack.add(unary);
            }
            else return new InterpretingResult<>(null, "Unexpected token found");
        }
        if (propositionStack.empty())
            return new InterpretingResult<>(null, "No propositions found");
        if (propositionStack.size() > 1)
            return new InterpretingResult<>(null, "More than 1 proposition found");

        return new InterpretingResult<>(
                new PropositionEntry(propositionStack.pop(), newAtomics), null);
    }

    public void setAtomicContext(Set<AtomicProposition> atomicContext) {
        atomicMap.clear();
        for (AtomicProposition a: atomicContext)
            atomicMap.put(a.toString(), a);
    }

    public void clearContext() {
        atomicMap.clear();
    }
}
