package interpreting;

import operators.BinaryOperator;
import operators.UnaryOperator;

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

    public String getRepresentation(TokenType type) {
        for (TableRow row: table) {
            if (row.tokenType == type)
                return row.representations[reprType];
        }
        return null;
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

    public void displayAsDefault() {reprType = 0;}
    public void displayAsLatex() {reprType = 1;}
    public void displayAsTypeable() {reprType = 2;}

    public static RepresentationTable getInstance() {
        if (instance == null)
            instance = new RepresentationTable();
        return instance;
    }
}