package interpreting.parsing;

import interpreting.common.InterpretingResult;
import interpreting.tokenization.Lexer;
import interpreting.tokenization.Token;
import propositions.AtomicProposition;
import propositions.Proposition;

import java.util.*;

public class LogicInterpreter {
    private Lexer lexer;
    private Parser parser;
    private InterpretingResult<Proposition> treeOutput;
    private List<AtomicProposition> atomics;
    private Map<String, AtomicProposition> atomicMap;

    public LogicInterpreter(String input) {
        lexer = new Lexer(input);
        parser = new Parser(lexer);
    }

    public LogicInterpreter() {
        this("");
    }

    public void generateProposition() {
        treeOutput = parser.buildPropositionTree();
//        if (treeOutput)
        atomics = new ArrayList<>(parser.getAtomics());
    }

    public Proposition getProposition() {
        return treeOutput.value();
    }

    public String getErrorMessage() {
        return treeOutput.message();
    }

    public List<AtomicProposition> getAtomics() {
        return atomics;
    }

    public void setInput(String input) {
        lexer.setInput(input);
    }
}
