import interpreting.LogicInterpreter;
import interpreting.RepresentationTable;
import propositions.AtomicProposition;
import propositions.Proposition;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        RepresentationTable repTable = RepresentationTable.getInstance();
        repTable.displayAsTypeable();

        LogicInterpreter logicInterpreter = new LogicInterpreter();
        logicInterpreter.setInput("(-t ^ s) -> -r");
//        logicInterpreter.setInput("(p) -> -(((q \\lor (T ^ p)) ^ q) ) <-> ((p \\lor q) ^ F)");
        logicInterpreter.process();
        repTable.printTruthTable(logicInterpreter);
    }
}
