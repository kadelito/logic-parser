package interpreting.common;

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
    private int reprType = -1;

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
//        return null;
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