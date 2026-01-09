import java.io.PrintStream;

/**
 * Because the built-in Java logger is too complex.
 */
public class SimpleLogger {
    private final PrintStream out;
    private final int LEVEL_OFF = 0;
    private final int LEVEL_FINE = 1;
    private final int LEVEL_FINER = 2;
    private final int LEVEL_FINEST = 3;
    private int level = 0;

    public SimpleLogger(PrintStream out) {
        this.out = out;
    }

    /**
     * For 1-line messages not deeply nested.
     */
    void fine(String msg) {
        log(LEVEL_FINE, msg);
    }

    /**
     * For 1-line messages inside nested loops.
     */
    void finer(String msg) {
        log(LEVEL_FINER, msg);
    }

    /**
     * For multiline messages inside nested loops.
     */
    void finest(String msg) {
        log(LEVEL_FINEST, msg);
    }


    void setOff() {
        level = LEVEL_OFF;
    }

    void setFine() {
        level = LEVEL_FINE;
    }

    void setFiner() {
        level = LEVEL_FINER;
    }

    void setFinest() {
        level = LEVEL_FINEST;
    }

    private void log(int msgLevel, String msg) {
        if (level >= msgLevel) {
            out.println(msg);
        }
    }

}
