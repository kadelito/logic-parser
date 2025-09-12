package interpreting.common;

import interpreting.parsing.PropositionProcessor;
import interpreting.tokenization.TokenType;
import operators.BinaryOperator;
import operators.UnaryOperator;
import propositions.AtomicProposition;
import propositions.Proposition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepresentationTable {

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
     *        3 | Words
     */
    private int reprType = -1;

    private RepresentationTable() {

        table.add(new TableRow(
                TokenType.OPEN_PAREN, new String[]{"("},
                null, null));
        table.add(new TableRow(
                TokenType.CLOSE_PAREN, new String[]{")"},
                null, null));

        table.add(new TableRow(
                TokenType.AND, new String[]{"∧", "\\land", "^", "AND"},
                BinaryOperator.AND, null));
        table.add(new TableRow(
                TokenType.OR, new String[]{"∨", "\\lor", "v", "OR"},
                BinaryOperator.OR, null));
        table.add(new TableRow(
                TokenType.IMPLY, new String[]{"→", "\\rightarrow", "->", "IMPLIES", "⇒"},
                BinaryOperator.IMPLY, null));
        table.add(new TableRow(
                TokenType.BICONDITIONAL,new String[]{"↔", "\\leftrightarrow", "<->", "EQUALS", "⇔", "\\equiv"}, //TODO better word for biconditional
                BinaryOperator.BICONDITIONAL, null));
        table.add(new TableRow(
                TokenType.NOT, new String[]{"¬", "\\neg ", "-", "NOT"},
                null, UnaryOperator.NOT));

        table.add(new TableRow(
                TokenType.TRUE, new String[]{"T", "TRUE", "1"},
                null, null));
        table.add(new TableRow(
                TokenType.TRUE, new String[]{"F", "FALSE", "0"},
                null, null));

        for (TableRow row: table) {
            for (String repr: row.representations()) {
                for (char c: repr.toCharArray()) {
                    allowedCharacters.add(c);
                }
            }
        }
    }

    public void printTruthTable(PropositionProcessor interpreter) {

        Proposition proposition = interpreter.getLastProposition();
        List<AtomicProposition> atomics = new ArrayList<>(interpreter.getAtomics());

        String treeRepr = proposition.repr();
        int reprLen = treeRepr.length();
        int lineLen = 2 + reprLen;
        List<Integer> atomicLengths = new ArrayList<>(atomics.size());

        // Print atomics and entire proposition
        StringBuilder topRow = new StringBuilder(" ");
        for (AtomicProposition a: atomics) {
            String aRepr = a.repr();
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
        if (numCombinations < atomics.size()) {
            System.out.println("Too many atomic propositions! (" + atomics.size() + ")");
            return;
        }
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
            for (String repr: row.representations()) {
                if (repr.equals(input))
                    return row.tokenType();
            }
        }
        return null;
    }

    public Set<TokenType> getPossibleTokenTypes(String input) {
        Set<TokenType> possible = new HashSet<>();
        for (TableRow row: table) {
            for (String repr: row.representations()) {
                if (input.length() <= repr.length() && repr.startsWith(input)) {
                    possible.add(row.tokenType());
                    break;
                }
            }
        }
        return possible;
    }

    public String getRepresentation(BinaryOperator binOp) {
        for (TableRow row: table) {
            if (row.binaryOperator() == binOp)
                return row.representations()[reprType];
        }
        return null;
    }

    public String getRepresentation(UnaryOperator unOp) {
        for (TableRow row: table) {
            if (row.unaryOperator() == unOp)
                return row.representations()[reprType] + extraSpace();
        }
        return null;
    }

    public boolean isAllowed(char c) {
        return Character.isWhitespace(c) || validIdentifierChar(c) || allowedCharacters.contains(c);
    }

    public boolean validIdentifierChar(char c) {
        return c == '_' || Character.isLetterOrDigit(c);
    }

    private String extraSpace() {return reprType == 1 || reprType == 3 ? " " : "";}

    /**
     * Display propositional operations as default symbols, ex: and operator -> "∧"
     */
    public void displayAsDefault() {reprType = 0;}

    /**
     * Display propositional operations as LaTeX commands, ex: and operator -> "\land"
     */
    public void displayAsLaTeX() {reprType = 1;}

    /**
     * Display propositional operations as typeable characters, ex: and operator -> "^"
     */
    public void displayAsTypeable() {reprType = 2;}

    /**
     * Display propositional operations as English words, ex: and operator -> "AND"
     */
    public void displayAsWords() {reprType = 3;}

    public static RepresentationTable getInstance() {
        if (instance == null)
            instance = new RepresentationTable();
        return instance;
    }
}