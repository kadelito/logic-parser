package interpreting.parsing;

import interpreting.common.PropositionConstructionResult;
import interpreting.tokenization.Lexer;
import propositions.AtomicProposition;
import propositions.Proposition;

import java.util.*;

public class PropositionProcessor {
    private Lexer lexer;
    private Parser parser;
    private PropositionConstructionResult prevTreeOutput;

    public PropositionProcessor(String input) {
        lexer = new Lexer(input);
        parser = new Parser(lexer);
    }

    public PropositionProcessor() {
        this("");
    }

    public void generateProposition() {
        prevTreeOutput = parser.buildPropositionTree();
    }

    public void generateProposition(String newStr) {
        setInput(newStr);
        generateProposition();
    }

    public boolean generateSucceeded() {return prevTreeOutput.value() != null;}

    public Proposition getLastProposition() {
        return prevTreeOutput.value();
    }

    public Set<AtomicProposition> getLastAtomicSet() {return prevTreeOutput.atomics();}

    public String getErrorMessage() {
        return prevTreeOutput.message();
    }

    public void setInput(String input) {
        lexer.setInput(input);
    }
}
