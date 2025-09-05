package interpreting;

import propositions.AtomicProposition;
import propositions.BinaryProposition;
import propositions.Proposition;
import propositions.UnaryProposition;

import java.util.*;

public class LogicInterpreter {
    private static final RepresentationTable repTable = RepresentationTable.getInstance();
    private String input;

    private Lexer lexer;
    private Proposition tree;
    private List<AtomicProposition> atomics;
    private Map<String, AtomicProposition> atomicMap;

    public LogicInterpreter(String input) {
        this.input = input;
        lexer = new Lexer(input);
        atomicMap = new HashMap<>();
    }

    public LogicInterpreter() {
        this("");
    }

    public void process() {
        lexer.tokenize();
        List<Token> tokens = lexer.getTokens();
        this.tree = parse(tokens);
    }

    /**
     * Parses the given list of tokens
     *
     * @param tokens the tokens
     */
    public Proposition parse(List<Token> tokens) {
        atomicMap.put("T", Proposition.TRUE);
        atomicMap.put("F", Proposition.FALSE);

        Queue<Token> tokenQueue = tokensToRPN(tokens);

        Proposition tree = buildPropositionTree(tokenQueue);

        atomicMap.remove("T");
        atomicMap.remove("F");
        this.atomics = new ArrayList<>(atomicMap.values());
        return tree;
    }

    // Convert to reverse polish notation using Shunting yard algorithm
    // https://en.wikipedia.org/wiki/Shunting_yard_algorithm
    private Queue<Token> tokensToRPN(List<Token> tokens) {
        Queue<Token> outputQueue = new LinkedList<>();
        Deque<Token> operatorStack = new ArrayDeque<>();
        for (int i = 0; i < tokens.size(); i++) {
            Token curToken = tokens.get(i);
            if (curToken.isConstant() || curToken.isIdentifier()) {
                atomicMap.computeIfAbsent(curToken.data, str -> new AtomicProposition(curToken.data));
                outputQueue.add(curToken);
            }
            else if (curToken.isParen()) {
                if (curToken.type == TokenType.OPEN_PAREN) {
                    operatorStack.push(curToken);
                }
                else { // curToken.type == TokenType.CLOSE_PAREN
                    while (!operatorStack.isEmpty() && operatorStack.peek().type != TokenType.OPEN_PAREN) {
                        outputQueue.add(operatorStack.pop());
                    }
                    assert Objects.requireNonNull(operatorStack.peek()).type == TokenType.OPEN_PAREN;
                    operatorStack.pop();
                    // TODO: add support for function tokens?
                }
            }
            else if (curToken.isBinaryOperation()) {
                while (!operatorStack.isEmpty() &&
                        operatorStack.peek().type != TokenType.OPEN_PAREN) {
                    outputQueue.add(operatorStack.pop());
                }
                // TODO: add support for differing precedence & right associativity?
                operatorStack.push(curToken);
            }
            else if (curToken.isUnaryOperation()) {
                operatorStack.push(curToken);
            }
            else if (curToken.type == TokenType.EOL) break;
        }
        while (!operatorStack.isEmpty()) {
            assert operatorStack.peek().type != TokenType.OPEN_PAREN;
            outputQueue.add(operatorStack.pop());
        }
        return outputQueue;
    }

    // Generate propositional tree from RPN queue of tokens
    private Proposition buildPropositionTree(Queue<Token> tokenQueue) {
        Stack<Proposition> propositionStack = new Stack<>();

        for (Token curToken: tokenQueue) {
            if (curToken.isConstant() || curToken.isIdentifier()) {
                propositionStack.add(atomicMap.get(curToken.data));
            }
            else if (curToken.isBinaryOperation()) {
                // Last two propositions are reversed to retain original order
                Proposition p2 = propositionStack.pop();
                Proposition p1 = propositionStack.pop();
                Proposition compound = new BinaryProposition(p1, p2, curToken.getBinaryOperator());
                propositionStack.add(compound);
            }
            else if (curToken.isUnaryOperation()) {
                Proposition p = propositionStack.pop();
                Proposition unary = new UnaryProposition(p, curToken.getUnaryOperator());
                propositionStack.add(unary);
            }
        }
        assert propositionStack.size() == 1;
        return propositionStack.pop();
    }

    public void printTruthTable() {
        String treeRepr = tree.toString();
        int reprLen = treeRepr.length();
        int lineLen = 1 + reprLen;
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
        for (int i = 0; i < reprLen; i++) {
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
            System.out.println(" " + tree.evaluate());
        }
    }

    private void inputError(int index, String message) {
        throw new RuntimeException(
                String.format("\n%s\n%" + (index + 1) + "s\n%s", input, "^", message)
        );
    }

    public void setInput(String input) {
        this.input = input;
        lexer.setInput(input);
    }
}
