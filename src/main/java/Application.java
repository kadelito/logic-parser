import interpreting.parsing.PropositionProcessor;
import interpreting.common.RepresentationTable;
import logic.LogicContext;
import propositions.Proposition;

import java.util.Scanner;

public class Application {
    private Scanner in;
    private PropositionProcessor processor;
    private LogicContext context;

    public Application() {
        in = new Scanner(System.in);
        processor = new PropositionProcessor();
        context = new LogicContext();
    }

    public void run() {
        RepresentationTable table = RepresentationTable.getInstance();
        table.displayAsDefault();

        System.out.println("Propositional Logic thing idk"); // TODO name this thing
        System.out.println("Type the number of an option, or press Enter to quit.");

//        processor.generateProposition("p -> q");
//        context.updateSelf(processor);
//        context.printTruthTable(0);

        while (context != null) {
            printMenu();
            String choice = in.nextLine().trim();
            if (choice.isEmpty()) {
                System.out.println("Goodbye!");
                break;
            }

            switch (choice) {
                case "1":
                    addProposition();
                    break;
                case "2":
                    listPropositions();
                    waitToContinue();
                    break;
                case "3":
                    showTruthTable();
                    waitToContinue();
                    break;
                default:
                    System.out.println("Unknown option: " + choice);
                    break;
            }
        }
    }

    private void waitToContinue() {
        System.out.print("\n(press enter to continue)");
        in.nextLine();
    }

    private void printMenu() {
        System.out.println("\nOptions:");
        System.out.println("  1: Add a proposition to the context");
        System.out.println("  2: Show propositions in the context");
        System.out.println("  3: Show a truth table for a proposition");
        System.out.print(  "> ");
    }

    private void addProposition() {
        System.out.print("Enter proposition: ");
        String input = in.nextLine().trim();
        if (input.isEmpty()) return;

        processor.generateProposition(input);
        Proposition tree = processor.getLastProposition();

        if (processor.generateSucceeded()) {
            context.updateSelf(processor);
            System.out.printf("Added to context: " + tree.repr());
        } else {
            System.out.println("Couldn't add proposition.");
            System.out.println(processor.getErrorMessage());
        }
    }

    private void listPropositions() {
        if (context.isEmpty()) {
            System.out.println("No propositions in context.");
            return;
        }
        System.out.println("Propositions in context:");
        int i = 0;
        for (Proposition p : context) {
            System.out.printf("  %d: %s%n", i++, p.repr());
        }
    }

    private void showTruthTable() {
        if (context.isEmpty()) {
            System.out.println("No propositions in context.");
            return;
        }

        listPropositions();
        System.out.print("Choose index: ");
        String input = in.nextLine().trim();

        try {
            int index = Integer.parseInt(input);
            context.printTruthTable(index);
        } catch (NumberFormatException e) {
            System.out.println("Not a valid number.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No proposition at that index.");
        }
    }
}