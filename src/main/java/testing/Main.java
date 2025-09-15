package testing;

import interpreting.common.InterpretingResult;
import interpreting.parsing.PropositionProcessor;
import interpreting.common.RepresentationTable;
import interpreting.tokenization.Token;
import logic.LogicContext;
import propositions.Proposition;

import java.util.Scanner;

public class Main {
    private static PropositionProcessor processor;
    private static LogicContext context;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        RepresentationTable table = RepresentationTable.getInstance();
        table.displayAsDefault();
        processor = new PropositionProcessor();
        context = new LogicContext();

        String inStr;
        while (processor == null) {
            System.out.println("Enter some proposition:");
            inStr = in.nextLine();
            if (inStr.isEmpty()) break;
            testInput(inStr);
        }

        testInput("((-((p))))");

        Iterable<InterpretingResult<Token>> source = new SmartTokenGenerator(5, "p", "q", "r");
//        for (InterpretingResult<Token> token: source)
//            System.out.print(token.value());
//        System.out.println();

        StringBuilder sb = new StringBuilder();
        String result;
        for (int t = 0; t < 3; t++) {
            do {
                sb.setLength(0);
                table.displayAsTypeable();
                for (InterpretingResult<Token> token: source)
                    sb.append(token.value()).append(" ");
                result = sb.toString();
                table.displayAsWords();
            } while (!testInput(result));
        }
    }

    public static boolean testInput(String str) {
        processor.generateProposition(str);
        Proposition tree = processor.getLastProposition();
        System.out.printf("Input: \"%s\"\n", str);
        System.out.println(tree != null ? "Tree: " + tree.repr() : processor.getErrorMessage());
//        if (tree != null) {
//            context.updateSelf(processor);
//            System.out.printf("Input:  %s\n", str);
//            System.out.printf("  Tree: %s\n", tree.repr());
////            context.printTruthTable();
//        }
        return tree != null;
    }
}
