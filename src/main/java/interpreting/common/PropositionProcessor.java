package interpreting.common;

import common.PropositionEntry;
import common.propositions.AtomicProposition;
import interpreting.parsing.Parser;
import interpreting.tokenization.Lexer;
import common.LogicContext;

/**
 * A class that can transform a {@link String} into a {@link PropositionEntry Proposition}.
 * <p>
 * After a successful generation, any {@link AtomicProposition AtomicPropositions} are saved to ensure shared instances.
 * <p>
 * For example, if a parser generates a tree represented by <code>"p ^ q"</code> then generates <code>"q -> r"</code>
 * without modifying the {@link LogicContext} externally or through {@link #setContext},
 * the <code>AtomicProposition</code> with representation <code>"q"</code> will be the same instance among both propositions.
 *
 * @see LogicContext
 * @see AtomicProposition
 */
public class PropositionProcessor {
    private Lexer lexer;
    private Parser parser;
    private LogicContext context;
    private InterpretingResult<PropositionEntry> prevTreeResult;

    /**
     * Instantiates a new Proposition processor.
     *
     * @param input   the input to be used
     * @param context the context
     */
    public PropositionProcessor(String input, LogicContext context) {
        this.context = context;
        this.lexer = new Lexer(input);
        this.parser = new Parser(lexer, context);
    }

    /**
     * Instantiates a new Proposition processor.
     *
     * @param context the context
     */
    public PropositionProcessor(LogicContext context) {
        this("", context);
    }

    /**
     * Attempts to generate a {@link PropositionEntry}
     * from the current input string.
     * <p>
     * If generation succeeds, the resulting <code>PropositionEntry</code> is added to the context.
     */
    public void generateProposition() {
        prevTreeResult = parser.buildPropositionTree();
        if (generateSucceeded())
            context.add(prevTreeResult.value());
    }

    /**
     * Sets the input string, then calls {@link #generateProposition()}.
     */
    public void generateProposition(String newStr) {
        setInput(newStr);
        generateProposition();
    }

    /**
     * Returns whether the last attempt to generate a proposition succeeded.
     *
     * @see #generateProposition()
     */
    public boolean generateSucceeded() {return prevTreeResult.value() != null;}

    /**
     * Returns the {@link PropositionEntry} generated most recently.
     * <p>
     * If the last attempt failed, returns <code>null</code>.
     * @see #generateProposition()
     */
    public PropositionEntry getLastProposition() {
        return prevTreeResult.value();
    }

    /**
     * Returns the error message associated with the most recent generation attempt.
     * <p>
     * If the last attempt succeeded, returns <code>null</code>.
     * @see #generateProposition()
     */
    public String getErrorMessage() {
        return prevTreeResult.message();
    }

    /**
     * Sets input.
     *
     * @param input the input
     */
    public void setInput(String input) {
        lexer.setInput(input);
    }

    /**
     * Clears the current {@link LogicContext}, allowing for new {@link PropositionEntry PropositionEntries} generated
     * to use new {@link AtomicProposition} instances.
     */
    public void clearContext() {context.clear();}

    /**
     * Changes the current {@link LogicContext}.
     */
    public void setContext(LogicContext context) {parser.setContext(context);}
}
