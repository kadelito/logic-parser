package interpreting.common;

import common.PropositionEntry;
import common.propositions.AtomicProposition;
import interpreting.parsing.Parser;
import interpreting.tokenization.Lexer;
import common.LogicContext;

/**
 *
 * After successfully generating a tree, any {@link AtomicProposition AtomicPropositions} are saved to ensure shared instances.
 * <p>
 * For example, if a parser generates a tree represented by <code>"p ^ q"</code> then generates <code>"q -> r"</code>
 * without modifying context externally or through {@link #setContext}
 * the <code>AtomicProposition</code> with representation <code>"q"</code> will be the same instance among both propositions.
 * @see LogicContext
 * @see AtomicProposition
 */
public class PropositionProcessor {
    private Lexer lexer;
    private Parser parser;
    private LogicContext context;
    private InterpretingResult<PropositionEntry> prevTreeResult;

    public PropositionProcessor(String input, LogicContext context) {
        this.context = context;
        this.lexer = new Lexer(input);
        this.parser = new Parser(lexer, context);
    }

    public PropositionProcessor(String input) {
        this(input, new LogicContext());
    }

    public PropositionProcessor(LogicContext context) {
        this("", context);
    }

    public PropositionProcessor() {
        this("", new LogicContext());
    }

    public void generateProposition() {
        prevTreeResult = parser.buildPropositionTree();
    }

    public void generateProposition(String newStr) {
        setInput(newStr);
        generateProposition();
    }

    public boolean generateSucceeded() {return prevTreeResult.value() != null;}

    public PropositionEntry getLastProposition() {
        return prevTreeResult.value();
    }

    public String getErrorMessage() {
        return prevTreeResult.message();
    }

    public void setInput(String input) {
        lexer.setInput(input);
    }

    public void clearContext() {context.clear();}

    public void setContext(LogicContext context) {parser.setContext(context);}
}
