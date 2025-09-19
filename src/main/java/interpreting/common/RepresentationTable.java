package interpreting.common;

import common.PropositionEntry;
import common.propositions.AtomicProposition;
import common.propositions.Proposition;
import interpreting.tokenization.TokenType;
import common.operators.BinaryOperator;
import common.operators.UnaryOperator;

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
    private int reprType = 0;

    private RepresentationTable() {

        table.add(new TableRow(
                TokenType.OPEN_PAREN, new String[]{"("},
                null, null));
        table.add(new TableRow(
                TokenType.CLOSE_PAREN, new String[]{")"},
                null, null));

        table.add(new TableRow(
                TokenType.AND, new String[]{"∧", "\\land", "^", "AND", "&", "&&", "*"},
                BinaryOperator.AND, null));
        table.add(new TableRow(
                TokenType.OR, new String[]{"∨", "\\lor", "v", "OR", "|", "||", "+"},
                BinaryOperator.OR, null));
        table.add(new TableRow(
                TokenType.IMPLY, new String[]{"→", "\\rightarrow", "->", "IMPLIES", "⇒"},
                BinaryOperator.IMPLY, null));
        table.add(new TableRow(
                TokenType.BICONDITIONAL,new String[]{"↔", "\\leftrightarrow", "<->", "EQUALS", "⇔", "\\equiv"},
                BinaryOperator.BICONDITIONAL, null));
        table.add(new TableRow(
                TokenType.NOT, new String[]{"¬", "\\neg ", "-", "NOT", "!", "~"},
                null, UnaryOperator.NOT));

        table.add(new TableRow(
                TokenType.TRUE, new String[]{"T", "T", "T", "TRUE", "1"},
                null, null));
        table.add(new TableRow(
                TokenType.FALSE, new String[]{"F", "F", "F", "FALSE", "0"},
                null, null));

        for (TableRow row: table) {
            for (String repr: row.representations()) {
                for (char c: repr.toCharArray()) {
                    allowedCharacters.add(c);
                }
            }
        }
    }

    public void printTruthTable(PropositionEntry entry) {

        Proposition proposition = entry.proposition();
        List<AtomicProposition> atomicList = new ArrayList<>(entry.atomics());

        String treeRepr = proposition.repr();
        int reprLen = treeRepr.length();
        int atomicsLen = 2;
        List<Integer> atomicLengths = new ArrayList<>(atomicList.size());

        // Print atomics and entire proposition
        StringBuilder topRow = new StringBuilder(" ");
        for (int i = atomicList.size() - 1; i >= 0; i--) {
            String aRepr = atomicList.get(i).repr();
            topRow.append(aRepr).append(" | ");
            atomicLengths.add(aRepr.length());
            atomicsLen += aRepr.length() + 3;
        }
        System.out.print(justifyCenter("Atomics", atomicsLen - 3));
        System.out.println("| " + justifyCenter("Proposition", reprLen + 1));
        topRow.append(treeRepr);
        System.out.println(topRow);

        // Divider
        for (int i = 0; i < atomicsLen + reprLen; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Truth Table
        long numCombinations = 1L << atomicList.size();
        // Check for overflow
        if (numCombinations < atomicList.size()) {
            System.out.println("Too many atomic propositions! (" + atomicList.size() + ")");
            return;
        }
        for (long comb = numCombinations - 1; comb >= 0; comb--) {
            for (int i = atomicList.size() - 1; i >= 0; i--) {
                boolean val = (comb >> i) % 2 == 1;
                atomicList.get(i).setValue(val);
                System.out.printf(justifyCenter(val ? "T" : "F", atomicLengths.get(i) + 2) + "|");
            }
            System.out.println(justifyCenter(proposition.evaluate() ? "T" : "F", reprLen + 1));
        }
    }

    // Helper method for printTruthTable
    private String justifyCenter(String str, int width) {
        if (str.length() >= width) return str;
        int padding = (width - str.length()) / 2;
        return String.format("%" + (padding + str.length()) + "s%" + (width - padding - str.length()) + "s", str, "");
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

    public String getRepresentation(TokenType type) {
        switch (type) {
            case OPEN_PAREN:
                return "(";
            case CLOSE_PAREN:
                return ")";
        }
        for (TableRow row: table) {
            if (row.tokenType() == type)
                return row.representations()[reprType];
        }
        throw new RuntimeException("Tried finding representation for token with type " + type);
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