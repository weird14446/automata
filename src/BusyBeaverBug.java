import java.io.File;

public class BusyBeaverBug {
    public static void main(String[] args) {
        String filename = "42-busyBeaver.txt";
        File f = new File(filename);
        if (f.exists()) f.delete();

        System.out.println("Testing BusyBeaver constructor with non-existent file...");
        try {
            new BusyBeaver(42);
            System.out.println("SUCCESS: Constructor finished (Unexpected)");
        } catch (RuntimeException e) {
            System.out.println("CAUGHT EXPECTED EXCEPTION: " + e.getCause());
        }
    }
}
