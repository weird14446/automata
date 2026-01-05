public class BugReproduction {
    public static void main(String[] args) {
        testUndefinedTransition();
    }

    private static void testUndefinedTransition() {
        System.out.println("Testing Undefined Transition (Infinite Loop)...");
        TuringMachine tm = new TuringMachine();
        tm.setInitialState("start");
        tm.setBlankSymbol('0');
        tm.addFinalState("halt");
        
        // Only one transition defined
        tm.addTransition("start", '1', "next", '1', true);
        
        // At "next", if it reads '0', there is no transition.
        // It should halt with error or status, but it will loop forever.
        
        Thread t = new Thread(() -> tm.run("10"));
        t.start();
        
        try {
            Thread.sleep(2000);
            if (t.isAlive()) {
                System.err.println("FAIL: TuringMachine is still running (Infinite Loop) on undefined transition!");
                System.exit(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
