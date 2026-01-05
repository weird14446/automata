import java.io.*;
import java.nio.charset.StandardCharsets;

public class BusyBeaver extends TuringMachine implements AutoCloseable {
    private FileOutputStream fileOutputStream;
    private final int num;
    private final boolean isWrite;

    public BusyBeaver(int n) {
        this(n, true);
    }

    public BusyBeaver(int n, boolean b) {
        super();
        this.num = n;
        this.isWrite = b;
        
        // Logical Fix: Only open file if writing is enabled. 
        // Removed useless FileInputStream.
        if (isWrite) {
            File file = new File(n + "-busyBeaver.txt");
            try {
                this.fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Failed to create log file", e);
            }
        }
    }

    @Override
    protected void onStart(String state) {
        if (isWrite) logState(state);
        // Default console output is suppressed in BusyBeaver logic? 
        // Original code seemed to mix them or only write to file.
        // Assuming we want to suppress console if writing to file to avoid noise,
        // or keep both. Original code: "try { write... } catch..." and no System.out.
        // So we do NOT call super.onStart(state) if we want to replace it.
    }

    @Override
    protected void onStep(String state) {
        if (isWrite) logState(state);
    }

    @Override
    protected void onFinish() {
        if (isWrite) {
            try {
                String result = "\nBB(" + num + ")=" + countChar('1') + "\n";
                fileOutputStream.write(result.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException("Failed to write result", e);
            }
        }
        // We can still print to console if desired
        // super.onFinish(); 
    }

    private void logState(String state) {
        try {
            fileOutputStream.write((tape.toString() + "\n").getBytes(StandardCharsets.UTF_8));
            
            StringBuilder head = new StringBuilder();
            for (int i = 0; i < headPosition; i++) head.append(" ");
            head.append("|").append(state).append("\n");
            
            fileOutputStream.write(head.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write state", e);
        }
    }

    @Override
    public void close() {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Failed to close stream", e);
            }
        }
    }
}