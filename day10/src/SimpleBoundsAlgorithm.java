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
                    // Using a long here to prevent overflows
                    long newUpper = M.getY(i);
                    long newLower = newUpper;

                    for (int k = 0; k < M.cols(); k++) {
                        if (k != j) {
                            // Bring the element to the other side of the equation
                            long x = -M.get(i, k);

                            // We pick the lower or upper bound based on whether we should maximize or minimize
                            // the current coefficient.

                            boolean inverse = (x < 0) ^ (M.get(i, j) < 0);

                            newUpper += x * (inverse ? lower[k] : upper[k]);
                            newLower += x * (inverse ? upper[k] : lower[k]);
                        }
                    }

                    // !!!!!!
                    // This is a very ugly integer division where we throw away a sometimes non-zero
                    // remainder. Somehow it seems to work but this is going to cause issues.
                    newUpper /= M.get(i, j);
                    newLower /= M.get(i, j);

                    // Better:
                    // newUpper = Util.safeDiv(newUpper, M.get(i, j));
                    // newLower = Util.safeDiv(newLower, M.get(i, j));

                    // Diagnostics
                    if (newUpper < upper[j]) {
                        LOGGER.finest("New upper bound found for coefficient " + j + " using row " + i + ": " + newUpper + "\nLower: " + Arrays.toString(lower) + "\nUpper: " + Arrays.toString(upper));
                    }
                    if (newLower > lower[j]) {
                        LOGGER.finest("New lower bound found for coefficient " + j + " using row " + i + ": " + newLower + "\nLower: " + Arrays.toString(lower) + "\nUpper: " + Arrays.toString(upper));
                    }

                    // Check if a bound is more restricted
                    if (newUpper < upper[j] || newLower > lower[j]) {
                        upper[j] = Math.toIntExact(Math.min(upper[j], newUpper));
                        lower[j] = Math.toIntExact(Math.max(lower[j], newLower));

                        if (upper[j] < lower[j]) {
                            LOGGER.finest("Contradiction");
                            return false;
                        }

                        // Start over because the new bounds may affect the earlier bounds
                        i = -1;  // We have to use -1 because the for loop does ++
                        break;
                    }
                }
            }
        }
        LOGGER.finer("Newly computed bounds: " + Arrays.toString(lower) + ", " + Arrays.toString(upper));
        return true;
    }
}
