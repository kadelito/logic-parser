package interpreting.parsing;

import common.PropositionEntry;
import interpreting.common.PropositionConstructionResult;
import interpreting.tokenization.Lexer;
import common.propositions.AtomicProposition;
import logic.LogicContext;

import java.util.*;

public class PropositionProcessor {
    private Lexer lexer;
    private Parser parser;
    private Set<AtomicProposition> atomicContext;
    private PropositionConstructionResult prevTreeOutput;

    public PropositionProcessor(String input) {
        this.atomicContext = new HashSet<>();
        lexer = new Lexer(input);
        parser = new Parser(lexer);
    }

    public PropositionProcessor(String input, Set<AtomicProposition> atomicContext) {
        this.atomicContext = atomicContext;
        lexer = new Lexer(input);
        parser = new Parser(lexer, atomicContext);
    }

    public PropositionProcessor() {
        this("");
    }

    public void generateProposition() {
        prevTreeOutput = parser.buildPropositionTree();
        if (generateSucceeded())
            atomicContext.addAll(prevTreeOutput.value().atomics());
    }

    public void generateProposition(String newStr) {
        setInput(newStr);
        generateProposition();
    }

    public boolean generateSucceeded() {return prevTreeOutput.value() != null;}

    public PropositionEntry getLastProposition() {
        return prevTreeOutput.value();
    }

    public Set<AtomicProposition> getLastAtomicSet() {return prevTreeOutput.value().atomics();}

    public String getErrorMessage() {
        return prevTreeOutput.message();
    }

    public void setInput(String input) {
        lexer.setInput(input);
    }

    public void setContext(Set<AtomicProposition> atomicContext) {parser.setAtomicContext(atomicContext);}

    public void setContext(LogicContext context) {parser.setAtomicContext(context.getAtomics());}

    public void clearContext() {parser.clearContext();}
}
