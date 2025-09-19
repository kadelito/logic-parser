import common.PropositionEntry;
import interpreting.parsing.PropositionProcessor;
import interpreting.common.RepresentationTable;
import logic.BruteForceReasoner;
import logic.PropositionRegistry;
import logic.Reasoner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {
    private Scanner input;
    private RepresentationTable table;
    private PropositionProcessor processor;
    private PropositionRegistry context;
    private Reasoner reasoner;

    public Application() {
        input = new Scanner(System.in);
        table = RepresentationTable.getInstance();
        processor = new PropositionProcessor();
        context = new PropositionRegistry();
        reasoner = new BruteForceReasoner();
    }

    public void run() {
        RepresentationTable.getInstance().displayAsDefault();

        System.out.println("Propositional Logic Playground"); // TODO name this thing better
        System.out.println("Type the number of an option, or press Enter to quit.");

        while (context != null) {
            printMenu();
            String choice = input.nextLine().trim();

            switch (choice) {
                case "":
                    System.out.println("Goodbye!");
                    return;
                case "0":
                    displaySettings();
                    break;
                case "1":
                    listPropositions();
                    break;
                case "2":
                    addProposition();
                    break;
                case "3":
                    showTruthTable();
                    break;
                case "4":
                    equalityTest();
                    break;
                case "5":
                    validityTest();
                    break;
                default:
                    System.out.println("Unknown option: " + choice);
                    break;
            }
            waitToContinue();
        }
    }

    private void waitToContinue() {
        System.out.print("\n(press enter to continue)");
        input.nextLine();
    }

    private void printMenu() {
        System.out.println("\nOptions:");
        System.out.println("  0: Change format settings");
        System.out.println("  1: Show propositions in the current context");
        System.out.println("  2: Add a proposition");
        System.out.println("  3: Show a truth table for a proposition");
        System.out.println("  4: Check for equality between propositions");
        System.out.println("  5: Evaluate validity of an argument");
        System.out.print(  "> ");
    }

    private void displaySettings() {
        RepresentationTable table = RepresentationTable.getInstance();
        System.out.println("\nOptions:");
        System.out.println("  1: Default");
        System.out.println("  2: LaTeX markup");
        System.out.println("  3: Typable characters");
        System.out.println("  4: Words");
        System.out.print(  "> ");

        String choice = input.nextLine();
        switch (choice) {
            case "1": table.displayAsDefault();
                break;
            case "2": table.displayAsLaTeX();
                break;
            case "3": table.displayAsTypeable();
                break;
            case "4": table.displayAsWords();
                break;
            default:
                System.out.println("No changes made.");
        }
    }

    private void addProposition() {
        System.out.print("Enter proposition: ");
        String input = this.input.nextLine().trim();
        if (input.isEmpty()) return;

        processor.generateProposition(input);
        if (processor.generateSucceeded()) {
            PropositionEntry entry = processor.getLastProposition();
            context.add(entry);
            System.out.printf("Added to context: " + entry.proposition().toString());
        } else {
            System.out.println("Couldn't add proposition.");
            System.out.println(processor.getErrorMessage());
        }
    }

    /**
     * @return whether any propositions were printed
     */
    private boolean listPropositions() {
        if (context.isEmpty()) {
            System.out.println("No propositions in context.");
            return false;
        }
        System.out.println("Propositions in context:");
        int i = 0;
        for (PropositionEntry p : context) {
            System.out.printf("  %d: %s\n", i++, p.proposition().toString());
        }
        return true;
    }

    private void showTruthTable() {
        listPropositions();
        PropositionEntry entry = askEntry("");
        if (entry != null)
            table.printTruthTable(entry);
    }

    private void equalityTest() {
        if (context.isEmpty()) {
            System.out.println("Not enough propositions to show equality.");
            return;
        }

        PropositionEntry entry1 = askEntry("Select the first proposition.");
        if (entry1 == null)
            return;

        PropositionEntry entry2 = askEntry("Select the second proposition.");
        if (entry2 == null)
            return;

        System.out.println("\nEvaluating equality between the two selections...");
        Boolean result = reasoner.areEqual(entry1, entry2);
        if (result == null)
            System.out.println("Something went wrong.");
        else
            System.out.println("The two propositions are " + (result ? "" : "not ") + "equivalent.");
    }

    private void validityTest() {
        if (context.isEmpty()) {
            System.out.println("Not enough propositions to evaluate argument validity.");
            return;
        }

        PropositionEntry conclusion = askEntry("Select a conclusion (the proposition you want to prove)");
        if (conclusion == null) return;

        List<PropositionEntry> premises = new ArrayList<>();
        while (true) {
            PropositionEntry entry = askEntry("Select a premise to add (or nothing to quit)");
            if (entry == null)
                break;
            else
                premises.add(entry);
        }
        if (premises.isEmpty()) {
            System.out.println("No premises entered.");
            return;
        }
        System.out.println("\nArgument:");
        for (PropositionEntry entry: premises)
            System.out.println(entry.proposition());
        for (int dash = 0; dash < 10; dash++)
            System.out.print('-');
        System.out.println("\n∴ " + conclusion.proposition());

        System.out.println("Evaluating validity...");
        Boolean result = reasoner.isArgumentValid(conclusion, premises);
        if (result == null)
            System.out.println("Something went wrong.");
        else
            System.out.println("The argument is " + (result ? "" : "not ") + "valid.");
    }

    /**
     * Lists propositions, and asks for an index
     * @return <code>null</code> if something went wrong, otherwise the entry at the proposition entered
     */
    private PropositionEntry askEntry(String message) {
        if (!listPropositions()) return null;
        System.out.println(message);
        int index = askIndex();
        if (index < 0) {
            System.out.println("Exiting early.");
            return null;
        }
        PropositionEntry entry = context.getEntry(index);
        System.out.println("Selected: " + entry.proposition());
        return entry;
    }

    /**
     * Helper method to ask for an index to a proposition in the current context.
     * <p>
     * This method assumes {@link #listPropositions()} was called.
     * <p>
     * @return -1 for an invalid index, -2 if nothing was entered,
     * otherwise returns the index entered.
     */
    private int askIndex() {
        if (context.isEmpty()) return -1;

        System.out.print("Enter index: ");
        String input = this.input.nextLine().trim();

        if (input.isEmpty()) {
            return -2;
        }

        int index;
        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Not a valid number.");
            return -1;
        }
        if (index < 0 || context.size() <= index) {
            System.out.println("No proposition at that index.");
            return -1;
        }
        else return index;
    }
}