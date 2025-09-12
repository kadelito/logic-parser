package interpreting.parsing;

import interpreting.common.InterpretingResult;
import interpreting.tokenization.Lexer;
import propositions.AtomicProposition;
import propositions.Proposition;

import java.util.*;

public class LogicInterpreter {
    private Lexer lexer;
    private Parser parser;
    private InterpretingResult<Proposition> prevTreeOutput;
    private List<Proposition> propositions;
    private Set<AtomicProposition> atomics;

    public LogicInterpreter(String input) {
        atomics = new HashSet<>();
        propositions = new ArrayList<>();

        lexer = new Lexer(input);
        parser = new Parser(lexer);
    }

    public LogicInterpreter() {
        this("");
    }

    public void generateProposition() {
        prevTreeOutput = parser.buildPropositionTree();
        if (prevTreeOutput.value() != null) {
            propositions.add(prevTreeOutput.value());
            atomics.addAll(parser.getAtomics());
        }
    }

    public Proposition getLastProposition() {
        return prevTreeOutput.value();
    }

    public String getErrorMessage() {
        return prevTreeOutput.message();
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public Set<AtomicProposition> getAtomics() {
        return atomics;
    }

    public void setInput(String input) {
        lexer.setInput(input);
    }
}
