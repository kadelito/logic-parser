import interpreting.LogicInterpreter;
import interpreting.RepresentationTable;

public class Main {
    public static void main(String[] args) {
        RepresentationTable table = RepresentationTable.getInstance();
        table.displayAsTypeable();

        LogicInterpreter logicInterpreter = new LogicInterpreter();
        logicInterpreter.setInput("(-t ^ s) -> -r");
//        logicInterpreter.setInput("(p) -> -(((q \\lor (T ^ p)) ^ q) ) <-> ((p \\lor q) ^ F)");
        logicInterpreter.process();
        logicInterpreter.printTruthTable();
    }
}
