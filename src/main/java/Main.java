import interpreting.LogicInterpreter;
import interpreting.RepresentationTable;
import propositions.AtomicProposition;
import propositions.BinaryProposition;
import propositions.Proposition;

public class Main {
    private static RepresentationTable repTable;
    private static LogicInterpreter logicInterpreter;

    public static void main(String[] args) {
        repTable = RepresentationTable.getInstance();
        repTable.displayAsWords();
        logicInterpreter = new LogicInterpreter();
        testInput("T");

        char[] allowed = "abc()∧∨→↔¬TF".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < 0; t++) {
            do {
                sb.setLength(0);
                for (int i = 0; i < Math.ceil(5 + Math.random() * 50); i++)
                    sb.append(allowed[(int) (Math.random() * allowed.length)]);
            } while (!testInput(sb.toString()));
        }
    }

    public static boolean testInput(String str) {
        logicInterpreter.setInput(str);
        logicInterpreter.generateProposition();
        Proposition tree = logicInterpreter.getProposition();
//        if (tree != null) {
//            System.out.printf("Input:  %s\n", str);
//            System.out.printf("Result: %s\n", tree.repr());
//        }
        System.out.printf("Input:  %s\n", str);
        System.out.printf("Result: %s\n", tree != null ? tree.repr() : logicInterpreter.getErrorMessage());
        return tree != null;
    }
}
