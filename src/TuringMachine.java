import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TuringMachine {
    protected HashSet<String> states;
    protected HashSet<Character> symbols;
    protected HashSet<Character> inputSymbols;
    protected String initialState;
    protected HashSet<String> finalStates;
    protected char blankSymbol;
    
    // Optimized Transition Storage: State -> (ReadChar -> Transition)
    protected Map<String, Map<Character, Transition>> transitionTable;
    
    protected StringBuilder tape;
    protected int headPosition;

    static class Transition {
        String state1;
        char symbol1;
        String state2;
        char symbol2;
        boolean direction; // true: right, false: left

        public Transition(String state1, char symbol1, String state2, char symbol2, boolean direction) {
            this.state1 = state1;
            this.symbol1 = symbol1;
            this.state2 = state2;
            this.symbol2 = symbol2;
            this.direction = direction;
        }
    }

    public TuringMachine() {
        inputSymbols = new HashSet<>();
        states = new HashSet<>();
        initialState = null;
        finalStates = new HashSet<>();
        symbols = new HashSet<>();
        transitionTable = new HashMap<>();
    }

    public void setInitialState(String state) {
        initialState = state;
    }

    public void setBlankSymbol(char s) {
        blankSymbol = s;
    }

    public void addFinalState(String state) {
        finalStates.add(state);
    }

    public void addState(String state) {
        states.add(state);
    }

    public void addSymbol(char s) {
        symbols.add(s);
    }

    public void addInputSymbol(char s) {
        inputSymbols.add(s);
    }

    public void addTransition(String readState, char readChar, String setState, char writeChar, boolean direction) {
        transitionTable.putIfAbsent(readState, new HashMap<>());
        transitionTable.get(readState).put(readChar, new Transition(readState, readChar, setState, writeChar, direction));
    }

    public long countChar(char c) {
        if (tape == null) return 0;
        return tape.chars().filter(a -> a == c).count();
    }

    // Template Method Pattern
    public void run(String inputTape) {
        this.tape = new StringBuilder(inputTape);
        this.headPosition = 0;
        String currentState = initialState;

        onStart(currentState);

        while (!finalStates.contains(currentState)) {
            char readChar = getCharUnderHead();
            
            // O(1) Lookup
            Map<Character, Transition> charMap = transitionTable.get(currentState);
            Transition t = (charMap != null) ? charMap.get(readChar) : null;

            if (t == null) {
                // Halt on undefined transition (Logic Fix)
                System.err.println("Halted: No transition defined for state '" + currentState + "' and symbol '" + readChar + "'");
                break;
            }

            // Execute Step
            currentState = t.state2;
            setCharUnderHead(t.symbol2);
            moveHead(t.direction);

            onStep(currentState);
        }

        onFinish();
    }

    private char getCharUnderHead() {
        if (headPosition < 0 || headPosition >= tape.length()) {
            // Should be unreachable with correct moveHead logic, but defensive:
            return blankSymbol; 
        }
        return tape.charAt(headPosition);
    }

    private void setCharUnderHead(char c) {
        tape.setCharAt(headPosition, c);
    }

    private void moveHead(boolean direction) {
        if (direction) { // Right
            headPosition++;
            if (headPosition >= tape.length()) {
                tape.append(blankSymbol);
            }
        } else { // Left
            if (headPosition == 0) {
                tape.insert(0, blankSymbol);
                // Head stays at 0 because we inserted at 0. 
                // The previous symbol moved to 1.
            } else {
                headPosition--;
            }
        }
    }

    // Hooks for subclasses (BusyBeaver) or default behavior
    protected void onStart(String state) {
        printState(state);
    }

    protected void onStep(String state) {
        printState(state);
    }

    protected void onFinish() {
        System.out.println("[accept]");
    }

    protected void printState(String state) {
        System.out.println(tape.toString());
        StringBuilder head = new StringBuilder();
        for (int i = 0; i < headPosition; i++) head.append(" ");
        System.out.println(head + "|" + state);
    }
}