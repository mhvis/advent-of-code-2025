import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Note! This implementation may scale rows of the matrix when necessary for a division.
 */
public class SimpleBoundsAlgorithm implements BoundsAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(SimpleBoundsAlgorithm.class.getName());

    @Override
    public boolean execute(Matrix M, int[] lower, int[] upper) {
        for (int i = 0; i < M.rows(); i++) {
            for (int j = 0; j < M.cols(); j++) {
                // The element must be non-zero, because we cannot divide by zero
                if (M.get(i, j) != 0) {
                    // Make sure that we can safely divide, when moving terms from one side of the equation to the other
                    M.scale(i, scalingFactor(M.getY(i), M.get(i, j)));
                    int newUpper = safeDiv(M.getY(i), M.get(i, j));
                    int newLower = newUpper;

                    for (int k = 0; k < M.cols(); k++) {
                        if (k != j) {
                            // Make sure that we can safely divide
                            M.scale(i, scalingFactor(M.get(i, k), M.get(i, j)));
                            // Bring the element to the other side of the equation
                            int x = -1 * safeDiv(M.get(i, k), M.get(i, j));

                            // When we are maximizing a coefficient:
                            //
                            // - If another element (x) on the other side is negative, we use the lower bound of
                            // the coefficient of that element, to 'help' the maximized coefficient in the least way possible.
                            // - Vice versa when another variable is positive.
                            //
                            // When we are minimizing a coefficient, this is the other way around.

                            newUpper = addBounded(newUpper, x * ((x < 0) ? lower[k] : upper[k]));
                            newLower = addBounded(newLower, x * ((x < 0) ? upper[k] : lower[k]));
                        }
                    }

                    // Diagnostics
                    if (newUpper < upper[j]) {
                        LOGGER.finer("New upper bound found for coefficient " + j + " using row " + i + ": " + newUpper);
                    }
                    if (newLower > lower[j]) {
                        LOGGER.finer("New lower bound found for coefficient " + j + " using row " + i + ": " + newLower);
                    }

                    // Check if a bound is more restricted
                    if (newUpper < upper[j] || newLower > lower[j]) {
                        upper[j] = Math.min(upper[j], newUpper);
                        lower[j] = Math.max(lower[j], newLower);

                        if (upper[j] < lower[j]) {
                            return false;  // Contradiction
                        }

                        // Start over because the new bounds may affect the earlier bounds
                        i = 0;
                        break;
                    }
                }
            }
        }
        LOGGER.finer("Newly computed bounds: " + Arrays.toString(lower) + ", " + Arrays.toString(upper));
        return true;
    }

    /**
     * Returns Integer.MAX_VALUE or Integer.MIN_VALUE when the result would overflow.
     */
    private int addBounded(int a, int b) {
        try {
            return Math.addExact(a, b);
        } catch (ArithmeticException e) {
            return (b >= 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
    }

    /**
     * Throws an exception if we cannot divide without remainder.
     */
    private int safeDiv(int a, int b) {
        if (a % b != 0) {
            throw new ArithmeticException(a + " is not a multiple of " + b);
        }
        return a / b;
    }

    /**
     * Determines a scaling factor that we can use to make two numbers in the same row of a matrix divisible.
     */
    private int scalingFactor(int a, int b) {
        int f = 1;
        LOGGER.finest("scalingFactor(" + a + ", " + b + ")");
        while ((f * a) % (f * b) != 0) {
            f++;
        }
        return f;
    }
}
