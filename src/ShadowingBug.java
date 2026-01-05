public class ShadowingBug {
    public static void main(String[] args) {
        TuringMachine tm = new TuringMachine();
        tm.setInitialState("start");
        tm.setBlankSymbol('0');
        tm.addFinalState("halt");
        
        // Step 1: Read '1', write '1', move Right. (i becomes 1, tape becomes "10")
        tm.addTransition("start", '1', "step2", '1', true);
        // Step 2: Read '0', write '1', move Right. (i becomes 2, tape becomes "11")
        // But wait! if 'tape.length()' is shadowed as 1, 
        // then at Step 2, i (1) is NOT equal to tape.length() - 1 (0).
        // So it just increments i to 2 WITHOUT expanding the tape.
        tm.addTransition("step2", '0', "step3", '1', true);
        // Step 3: Read anything. i is 2, tape is "11". tape.charAt(2) will CRASH.
        tm.addTransition("step3", '0', "halt", '1', true);
        tm.addTransition("step3", '1', "halt", '1', true);

        System.out.println("Running Turing Machine to test tape expansion shadowing bug...");
        try {
            tm.run("1");
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println("CAUGHT EXPECTED BUG: " + e);
        }
    }
}