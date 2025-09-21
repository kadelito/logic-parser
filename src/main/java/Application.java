import common.PropositionEntry;
import interpreting.parsing.PropositionProcessor;
import interpreting.common.RepresentationTable;
import logic.BruteForceReasoner;
import logic.PropositionRegistry;
import logic.Reasoner;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {
    private Scanner input;
    private PrintStream output;
    private RepresentationTable table;
    private PropositionProcessor processor;
    private Reasoner reasoner;

    public Application(InputStream inputStream, PrintStream printStream) {
        input = new Scanner(inputStream);
        output = printStream;
        table = RepresentationTable.getInstance();
        processor = new PropositionProcessor();
        context = new PropositionRegistry();
        reasoner = new BruteForceReasoner();
    }

    public void run() {
        RepresentationTable.getInstance().displayAsDefault();

        output.println("Propositional Logic Playground"); // TODO name this thing better
        output.println("Type the number of an option, or press Enter to quit.");

        while (context != null) {
            printMenu();
            String choice = input.nextLine().trim();

            switch (choice) {
                case "":
                    output.println("Goodbye!");
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
                    output.println("Unknown option: " + choice);
                    break;
            }
            waitToContinue();
        }
    }

    private void waitToContinue() {
        output.print("\n(press enter to continue)");
        input.nextLine();
    }

    private void printMenu() {
        output.println("\nOptions:");
        output.println("  0: Change format settings");
        output.println("  1: Show propositions in the current context");
        output.println("  2: Add a proposition");
        output.println("  3: Show a truth table for a proposition");
        output.println("  4: Check for equality between propositions");
        output.println("  5: Evaluate validity of an argument");
        output.print(  "> ");
    }

    private void displaySettings() {
        RepresentationTable table = RepresentationTable.getInstance();
        output.println("\nOptions:");
        output.println("  1: Default");
        output.println("  2: LaTeX markup");
        output.println("  3: Typable characters");
        output.println("  4: Words");
        output.print(  "> ");

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
                output.println("No changes made.");
        }
    }

    private void addProposition() {
        output.print("Enter proposition: ");
        String input = this.input.nextLine().trim();
        if (input.isEmpty()) return;

        processor.generateProposition(input);
        if (processor.generateSucceeded()) {
            PropositionEntry entry = processor.getLastProposition();
            context.add(entry);
            output.printf("Added to context: " + entry.proposition().toString());
        } else {
            output.println("Couldn't add proposition.");
            output.println(processor.getErrorMessage());
        }
    }

    /**
     * @return whether any propositions were printed
     */
    private boolean listPropositions() {
        if (context.isEmpty()) {
            output.println("No propositions in context.");
            return false;
        }
        output.println("Propositions in context:");
        int i = 0;
        for (PropositionEntry p : context) {
            output.printf("  %d: %s\n", i++, p.proposition().toString());
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
            output.println("Not enough propositions to show equality.");
            return;
        }

        PropositionEntry entry1 = askEntry("Select the first proposition.");
        if (entry1 == null)
            return;

        PropositionEntry entry2 = askEntry("Select the second proposition.");
        if (entry2 == null)
            return;

        output.println("\nEvaluating equality between the two selections...");
        Boolean result = reasoner.areEqual(entry1, entry2);
        if (result == null)
            output.println("Something went wrong.");
        else
            output.println("The two propositions are " + (result ? "" : "not ") + "equivalent.");
    }

    private void validityTest() {
        if (context.isEmpty()) {
            output.println("Not enough propositions to evaluate argument validity.");
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
            output.println("No premises entered.");
            return;
        }
        output.println("\nArgument:");
        for (PropositionEntry entry: premises)
            output.println(entry.proposition());
        for (int dash = 0; dash < 10; dash++)
            output.print('-');
        output.println("\n∴ " + conclusion.proposition());

        output.println("Evaluating validity...");
        Boolean result = reasoner.isArgumentValid(conclusion, premises);
        if (result == null)
            output.println("Something went wrong.");
        else
            output.println("The argument is " + (result ? "" : "not ") + "valid.");
    }

    /**
     * Lists propositions, and asks for an index
     * @return <code>null</code> if something went wrong, otherwise the entry at the proposition entered
     */
    private PropositionEntry askEntry(String message) {
        if (!listPropositions()) return null;
        output.println(message);
        int index = askIndex();
        if (index < 0) {
            output.println("Exiting early.");
            return null;
        }
        PropositionEntry entry = context.getEntry(index);
        output.println("Selected: " + entry.proposition());
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

        output.print("Enter index: ");
        String input = this.input.nextLine().trim();

        if (input.isEmpty()) {
            return -2;
        }

        int index;
        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            output.println("Not a valid number.");
            return -1;
        }
        if (index < 0 || context.size() <= index) {
            output.println("No proposition at that index.");
            return -1;
        }
        else return index;
    }
}