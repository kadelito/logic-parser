package interpreting;

import operators.BinaryOperator;
import operators.UnaryOperator;
import propositions.AtomicProposition;
import propositions.Proposition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepresentationTable {

    record TableRow(
            TokenType tokenType,
            String[] representations,
            // exactly one of binaryOperator and unaryOperator will be null
             BinaryOperator binaryOperator,
            UnaryOperator unaryOperator
    ) {}

    private static RepresentationTable instance;
    private final List<TableRow> table = new ArrayList<>();
    private final Set<Character> allowedCharacters = new HashSet<>();

    /*
     * reprType | definition
     * ---------+-----------
     *       -1 | Debug mode
     *        0 | Default
     *        1 | LaTeX
     *        2 | Typeable
     */
    private int reprType = -1;

    private RepresentationTable() {

        table.add(new TableRow(
                TokenType.OPEN_PAREN, new String[]{"(","(","("},
                null, null));
        table.add(new TableRow(
                TokenType.CLOSE_PAREN, new String[]{")",")",")"},
                null, null));

        table.add(new TableRow(
                TokenType.AND, new String[]{"∧", "\\land ", "^"},
                BinaryOperator.AND, null));
        table.add(new TableRow(
                TokenType.OR, new String[]{"∨", "\\lor ", "v"},
                BinaryOperator.OR, null));
        table.add(new TableRow(
                TokenType.IMPLY, new String[]{"→", "\\rightarrow ", "->", "⇒"},
                BinaryOperator.IMPLY, null));
        table.add(new TableRow(
                TokenType.BICONDITIONAL,new String[]{"↔", "\\leftrightarrow ", "<->", "⇔"},
                BinaryOperator.BICONDITIONAL, null));
        table.add(new TableRow(
                TokenType.NOT, new String[]{"¬", "\\neg ", "-"},
                null, UnaryOperator.NOT));

        for (TableRow row: table) {
            for (String repr: row.representations) {
                for (char c: repr.toCharArray()) {
                    allowedCharacters.add(c);
                }
            }
        }
    }



    public void printTruthTable(LogicInterpreter interpreter) {

        Proposition proposition = interpreter.getProposition();
        List<AtomicProposition> atomics = interpreter.getAtomics();

        String treeRepr = proposition.toString();
        int reprLen = treeRepr.length();
        int lineLen = 2 + reprLen;
        List<Integer> atomicLengths = new ArrayList<>();

        // Print atomics and entire proposition
        StringBuilder topRow = new StringBuilder(" ");
        for (AtomicProposition a: atomics) {
            String aRepr = a.toString();
            topRow.append(aRepr).append(" | ");
            atomicLengths.add(aRepr.length());
            lineLen += aRepr.length() + 3;
        }
        topRow.append(treeRepr);
        System.out.println(topRow);

        // Divider
        for (int i = 0; i < lineLen; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Truth Table
        long numCombinations = 1L << atomics.size();
        // Check for overflow
        if (numCombinations < atomics.size())
            throw new RuntimeException("Too many atomic propositions! (" + atomics.size() + ")");
        for (long comb = numCombinations - 1; comb >= 0; comb--) {
            for (int aIndex = 0; aIndex < atomics.size(); aIndex++)
                atomics.get(atomics.size() - aIndex - 1).setValue((comb >> aIndex) % 2);
            for (int i = 0; i < atomics.size(); i++) {
                System.out.printf(" %" + atomicLengths.get(i) + "s |", atomics.get(i).evaluate() ? "T" : "F");
            }
            System.out.println(justifyCenter(proposition.evaluate() ? "T" : "F", reprLen + 1));
        }
    }

    private String justifyCenter(String str, int width) {
        if (str.length() >= width) return str;
        int padding = (width - str.length()) / 2;
        return String.format("%" + (padding + str.length()) + "s", str);
    }

    public TokenType getTokenType(String input) {
        for (TableRow row: table) {
            for (String repr: row.representations) {
                if (repr.equals(input))
                    return row.tokenType;
            }
        }
        return null;
    }

    public Set<TokenType> getPossibleTokenTypes(String input) {
        Set<TokenType> possible = new HashSet<>();
        for (TableRow row: table) {
            for (String repr: row.representations) {
                if (input.length() <= repr.length() && repr.startsWith(input)) {
                    possible.add(row.tokenType);
                    break;
                }
            }
        }
        return possible;
    }

    public String getRepresentation(BinaryOperator binOp) {
        for (TableRow row: table) {
            if (row.binaryOperator == binOp)
                return row.representations[reprType];
        }
        return null;
    }

    public String getRepresentation(UnaryOperator unOp) {
        for (TableRow row: table) {
            if (row.unaryOperator == unOp)
                return row.representations[reprType];
        }
        return null;
    }

    public boolean isAllowed(char c) {
        return Character.isWhitespace(c) || Character.isLetterOrDigit(c) || allowedCharacters.contains(c);
    }

    public void displayAsDefault() {reprType = 0;}
    public void displayAsLatex() {reprType = 1;}
    public void displayAsTypeable() {reprType = 2;}

    public static RepresentationTable getInstance() {
        if (instance == null)
            instance = new RepresentationTable();
        return instance;
    }
}