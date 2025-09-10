import interpreting.parsing.LogicInterpreter;
import interpreting.common.RepresentationTable;
import propositions.Proposition;

import java.util.Scanner;

public class Main {
    private static RepresentationTable repTable;
    private static LogicInterpreter logicInterpreter;
    private static Scanner in;

    public static void main(String[] args) {
        in = new Scanner(System.in);
        repTable = RepresentationTable.getInstance();
        repTable.displayAsWords();
        logicInterpreter = new LogicInterpreter();

        String inStr;
        while (true) {
            System.out.println("Enter some proposition:");
            inStr = in.nextLine();
            if (inStr.isEmpty()) break;
            testInput(inStr);
        }

        char[] allowed = "ABC()∧∨→↔¬TF    ".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < 0; t++) {
            do {
                sb.setLength(0);
                for (int i = 0; i < Math.ceil(5 + Math.random() * 50); i++)
                    sb.append(allowed[(int) (Math.random() * allowed.length)]);
                System.out.println();
            } while (!testInput(sb.toString()));
        }
    }

    public static boolean testInput(String str) {
        logicInterpreter.setInput(str);
        logicInterpreter.generateProposition();
        Proposition tree = logicInterpreter.getProposition();
        System.out.printf("Input:  %s\n", str);
        System.out.println(tree != null ? "Result: " + tree.repr() : logicInterpreter.getErrorMessage());
        if (tree != null) {
            repTable.printTruthTable(logicInterpreter);
//            System.out.printf("Input:  %s\n", str);
//            System.out.printf("Result: %s\n", tree.repr());
        }
        return tree != null;
    }
}
