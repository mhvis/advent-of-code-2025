import java.util.logging.Logger;

public class Util {
    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    /**
     * Least common multiple.
     */
    public static int lcm(int a, int b) {
        int x = Math.abs(a);
        int y = Math.abs(b);
        if (x == 0 || y == 0) {
            LOGGER.finest("lcm(" + a + ", " + b + ") = 0");
            return 0;
        }
        while (x != y) {
            if (x < y) {
                x += Math.abs(a);
            } else {
                y += Math.abs(b);
            }
        }
        LOGGER.finest("lcm(" + a + ", " + b + ") = " + x);
        return x;
    }

    /**
     * @throws ArithmeticException when we cannot divide without remainder
     */
    public static int safeDiv(int a, int b) {
        if (a % b != 0) {
            throw new ArithmeticException(a + " is not a multiple of " + b);
        }
        return a / b;
    }

    /**
     * @throws ArithmeticException when we cannot divide without remainder
     */
    public static long safeDiv(long a, long b) {
        if (a % b != 0) {
            throw new ArithmeticException(a + " is not a multiple of " + b);
        }
        return a / b;
    }
}
