package interpreting.parsing;

import common.PropositionEntry;
import common.operators.BinaryOperator;
import common.operators.UnaryOperator;
import common.propositions.*;
import interpreting.common.InterpretingResult;
import interpreting.tokenization.Token;
import common.LogicContext;
import interpreting.tokenization.TokenType;

import java.util.*;

/**
 * A class that transforms a sequence of tokens into a propositional tree.
 * <p>
 * Input is assumed to follow the expected syntactic format.
 * If the input is invalid, this is handled as a normal part of control flow,
 * as an {@link InterpretingResult} will be returned containing an error message
 * rather than throwing an exception.
 * <p>
 * Internal errors (e.g. null tokens, logic bugs) are the only cases that may cause exceptions.
 */
public class Parser {

    private Iterable<InterpretingResult<Token>> RPNTokenSequence;
    private LogicContext context;
    private LogicContext tempContext;

    /**
     * Instantiates a new Parser using a pre-existing context.
     *
     * @param infixTokenSequence a sequence of tokens assumed to be in an infix format.
     * @param context      the context to use (or create)
     */
    public Parser(Iterable<InterpretingResult<Token>> infixTokenSequence, LogicContext context) {
        this.RPNTokenSequence = new TokenPreParser(infixTokenSequence);
        this.context = context;
        this.tempContext = new LogicContext();
    }

    /**
     * Attempts to build a propositional tree. If it succeeds, updates the context
     * <p>
     * To prevent directly modifying context,
     * a separate, temporary {@link LogicContext} is used for {@link AtomicProposition AtomicPropositions}
     * not already in the context to ensure
     * @return the result of the attempt
     * @see AtomicProposition
     */
    public InterpretingResult<PropositionEntry> buildPropositionTree() {
        InterpretingResult<PropositionEntry> result = attemptGeneration();
        tempContext.clear();
        return result;
    }

    /**
     * Actual code for buildPropositionTree.
     * Extracted to allow separate "cleanup" operations at the end
     * regardless on success or failure
     */
    private InterpretingResult<PropositionEntry> attemptGeneration() {
        Set<AtomicProposition> newAtomics = new HashSet<>();
        Stack<Proposition> propositionStack = new Stack<>();

        for (InterpretingResult<Token> inToken: RPNTokenSequence) {
            Token token = inToken.value();

            if (token == null)
                return new InterpretingResult<>(null, "Token error: " + inToken.message());

            if (token.isConstant())
                propositionStack.add(switch(token.getType()) {
                    case TRUE -> Proposition.getTrue();
                    case FALSE -> Proposition.getFalse();
                    default -> throw new IllegalStateException();
                });
            else if (token.isIdentifier()) {
                AtomicProposition atomic = getAtomic(token.getText());
                propositionStack.add(atomic);
                newAtomics.add(atomic);
            }
            else if (token.isBinaryOperation()) {
                // Last two propositions are reversed to retain original order
                if (propositionStack.size() < 2)
                    return new InterpretingResult<>(null,  "Binary operator does not have two propositions");
                Proposition p2 = propositionStack.pop();
                Proposition p1 = propositionStack.pop();
                Proposition binary = new BinaryProposition(p1, p2, switch (token.getType()) {
                    case AND -> BinaryOperator.AND;
                    case OR -> BinaryOperator.OR;
                    case IMPLY -> BinaryOperator.IMPLY;
                    case BICONDITIONAL -> BinaryOperator.BICONDITIONAL;
                    default -> throw new IllegalStateException("Unexpected value: " + token.getType());
                });
                propositionStack.add(binary);
            }
            else if (token.isUnaryOperation()) {
                if (propositionStack.isEmpty())
                    return new InterpretingResult<>(null,  "Unary operator does not have a proposition");
                Proposition p = propositionStack.pop();
                Proposition unary = new UnaryProposition(p, switch (token.getType()) {
                    case NOT -> UnaryOperator.NOT;
                    default -> throw new IllegalStateException("Unexpected value: " + token.getType());
                });
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

    private AtomicProposition getAtomic(String repr) {
        if (context.contains(repr))
            return context.getOrCreateAtomic(repr);
        else
            return tempContext.getOrCreateAtomic(repr);
    }

    /**
     * Sets the context.
     *
     * @param context the new context to be used
     */
    public void setContext(LogicContext context) {
        this.context = context;
    }
}
