package interpreting;

import propositions.AtomicProposition;
import propositions.BinaryProposition;
import propositions.Proposition;
import propositions.UnaryProposition;

import java.util.*;

public class LogicInterpreter {
    private final Lexer lexer;
    private InterpretingResult<Proposition> treeOutput;
    private List<AtomicProposition> atomics;
    private Map<String, AtomicProposition> atomicMap;

    public LogicInterpreter(String input) {
        lexer = new Lexer(input);
        atomicMap = new HashMap<>();
    }

    public LogicInterpreter() {
        this("");
    }

    public void generateProposition() {
        lexer.tokenize();
        InterpretingResult<List<Token>> tokensResult = lexer.getTokens();
        if (tokensResult.value() == null) {
            treeOutput = new InterpretingResult<>(null, "Token creation error: " + tokensResult.message());
            return;
        }

        InterpretingResult<Proposition> treeResult = parse(tokensResult.value());
        if (treeResult.value() == null) {
            treeOutput = new InterpretingResult<>(null, "Parsing error: " + treeResult.message());
            return;
        }
        treeOutput = treeResult;
    }

    /**
     * Parses the given list of tokens into a propositional tree.
     * If the parsing failed, the 'value' member of the result will be null.
     *
     * @param tokens the tokens
     *
     * @return the head node of the treeOutput
     */
    public InterpretingResult<Proposition> parse(List<Token> tokens) {

        InterpretingResult<Queue<Token>> queueResult = tokensToRPN(tokens);
        if (queueResult.value() == null)
            return new InterpretingResult<>(null, "Token processing error: " + queueResult.message());

        InterpretingResult<Proposition> treeResult = buildPropositionTree(queueResult.value());
        if (treeResult.value() == null)
            return new InterpretingResult<>(null, "Tree construction error: " + treeResult.message());

        this.atomics = new ArrayList<>(atomicMap.values());
        return treeResult;
    }

    // Convert to reverse polish notation using Shunting yard algorithm
    // https://en.wikipedia.org/wiki/Shunting_yard_algorithm
    private InterpretingResult<Queue<Token>> tokensToRPN(List<Token> tokens) {
        Queue<Token> outputQueue = new LinkedList<>();
        Deque<Token> operatorStack = new ArrayDeque<>();
        boolean propositionExpected = true;
        int activeOperands = 0;

        for (Token curToken : tokens) {
            if (curToken.isProposition()) {
                if (!propositionExpected)
                    return new InterpretingResult<>(null, "Unexpected proposition");
                if (activeOperands > 0 && operatorStack.isEmpty())
                    return new InterpretingResult<>(null, "Identifier used without associated operator");
                outputQueue.add(curToken);
                activeOperands++;
                propositionExpected = false;
            }
            else if (curToken.isParen()) {
                if (curToken.type == TokenType.OPEN_PAREN) {
                    operatorStack.push(curToken);
                }
                else { // curToken.type == TokenType.CLOSE_PAREN
                    while (!operatorStack.isEmpty() && operatorStack.peek().type != TokenType.OPEN_PAREN) {
                        outputQueue.add(operatorStack.pop());
                        activeOperands--;
                    }
                    if (operatorStack.isEmpty() || operatorStack.peek().type != TokenType.OPEN_PAREN)
                        return new InterpretingResult<>(null, "Incorrect use of closing parentheses");
                    operatorStack.pop();
                    propositionExpected = false;
                    // TODO: add support for function tokens?
                }
            }
            else if (curToken.isBinaryOperation()) {
                if (outputQueue.isEmpty() || propositionExpected)
                    return new InterpretingResult<>(null, String.format("Binary operator '%s' found when proposition was expected", curToken));
                while (!operatorStack.isEmpty() && operatorStack.peek().type != TokenType.OPEN_PAREN) {
                    if (operatorStack.peek().numOperands() == 2)
                        activeOperands--;
                    outputQueue.add(operatorStack.pop());
                }
                // TODO: add support for differing precedence & right associativity?
                operatorStack.push(curToken);
                propositionExpected = true;
            }
            else if (curToken.isUnaryOperation()) {
                if (!propositionExpected)
                    return new InterpretingResult<>(null, "Unexpected unary operation");
                operatorStack.push(curToken);
//              propositionExpected remains true
            }
        }
        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek().type == TokenType.OPEN_PAREN)
                return new InterpretingResult<>(null, "Open parenthesis was not closed");
            if (operatorStack.peek().numOperands() == 2)
                activeOperands--;
            outputQueue.add(operatorStack.pop());
        }
        return new InterpretingResult<>(outputQueue, null);
    }

    // Generate propositional treeOutput from RPN queue of tokens
    private InterpretingResult<Proposition> buildPropositionTree(Queue<Token> tokenQueue) {
        Stack<Proposition> propositionStack = new Stack<>();

        for (Token curToken: tokenQueue) {
            if (curToken.isConstant())
                propositionStack.add(switch(curToken.type) {
                    case TRUE -> Proposition.TRUE;
                    case FALSE -> Proposition.FALSE;
                    default -> throw new RuntimeException("Non-constant token was considered constant");
                });
            else if (curToken.isIdentifier())
                propositionStack.add(atomicMap.computeIfAbsent(curToken.data, str -> new AtomicProposition(curToken.data)));
            else if (curToken.isBinaryOperation()) {
                // Last two propositions are reversed to retain original order
                if (propositionStack.size() < 2)
                    return new InterpretingResult<>(null, "Binary operator does not have two propositions");
                Proposition p2 = propositionStack.pop();
                Proposition p1 = propositionStack.pop();
                Proposition compound = new BinaryProposition(p1, p2, curToken.getBinaryOperator());
                propositionStack.add(compound);
            }
            else if (curToken.isUnaryOperation()) {
                if (propositionStack.isEmpty())
                    return new InterpretingResult<>(null, "Unary operator does not have a proposition");
                Proposition p = propositionStack.pop();
                Proposition unary = new UnaryProposition(p, curToken.getUnaryOperator());
                propositionStack.add(unary);
            }
            else return new InterpretingResult<>(null, "Unexpected token found");
        }
        if (propositionStack.size() != 1)
            return new InterpretingResult<>(null, "More than 1 proposition found");
        return new InterpretingResult<>(propositionStack.pop(), null);
    }

    public Proposition getProposition() {
        return treeOutput.value();
    }

    public String getErrorMessage() {
        return treeOutput.message();
    }

    public List<AtomicProposition> getAtomics() {
        return atomics;
    }

    public void setInput(String input) {
        lexer.setInput(input);
    }
}
