import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static void main() {
        // Parse input
        ArrayList<Problem> problems = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            problems.add(Problem.fromLine(scanner.nextLine()));
        }

        int sum = 0;
        for (int p = 0; p < problems.size(); p++) {
            int answer = solvePart2(problems.get(p));
            LOGGER.info("Answer " + p + ": " + answer);
            sum += answer;
        }
        System.out.println(sum);
    }

    private static int solvePart2(Problem problem) {
        // Construct matrix
        Matrix M = new Matrix(problem.joltage.length, problem.wirings.length);
        for (int i = 0; i < problem.wirings.length; i++) {
            for (int button : problem.wirings[i]) {
                M.set(button, i, 1);
            }
        }
        // Construct RHS of the matrix
        for (int i = 0; i < problem.joltage.length; i++) {
            M.setY(i, problem.joltage[i]);
        }
        LOGGER.fine("Initial matrix:\n" + M);

        // Bring to row reduced echelon form
        new GaussJordanRrefAlgorithm().execute(M);
        LOGGER.fine("RREF:\n" + M);

        // Find pivots and free variables
        boolean[] free = frees(M);

        // Prepare lower and upper bounds of all coefficients, initially set to 0 and Infinity respectively
        int[] lower = new int[M.cols()];
        int[] upper = new int[M.cols()];
        Arrays.fill(upper, Integer.MAX_VALUE);

        return minimizeDfs(M, lower, upper, free, new SimpleBoundsAlgorithm());
    }

    private static int minimizeDfs(Matrix M, int[] lower, int[] upper, boolean[] free, BoundsAlgorithm boundsAlgo) {
        // Update lower and upper bounds
        lower = lower.clone();
        upper = upper.clone();

        if (!boundsAlgo.execute(M, lower, upper)) {
            // Contradiction, return infinity
            return Integer.MAX_VALUE;
        }

        // Base case: return the sum (total number of button presses) when there are no unknown coefficients left
        if (Arrays.equals(lower, upper)) {
            return Arrays.stream(lower).sum();
        }

        // Find an unknown free coefficient with the smallest [lower, upper] range
        int c = -1;
        for (int i = 0; i < M.cols(); i++) {
            if (free[i] && lower[i] != upper[i]) {
                if (c == -1 || upper[i] - lower[i] < upper[c] - lower[c]) {
                    c = i;
                }
            }
        }

        // Guess all values for the chosen coefficient, and pick the minimal result
        int lowerC = lower[c];
        int upperC = upper[c];
        if (upperC - lowerC > 100000) {
            throw new RuntimeException("Numbers are too large, something is wrong");
        }
        int min = Integer.MAX_VALUE;
        for (int v = lowerC; v <= upperC; v++) {
            lower[c] = v;
            upper[c] = v;
            int res = minimizeDfs(M, lower, upper, free, boundsAlgo);
            LOGGER.finer("minimizeDfs: c=" + c + " v=" + v + " res=" + res);
            min = Math.min(min, res);
        }

        return min;
    }

    /**
     * Finds pivots and free variables in a matrix in row reduced echelon form.
     *
     * <p>We don't require the pivots to be '1'.</p>
     */
    private static boolean[] frees(Matrix M) {
        boolean[] free = new boolean[M.cols()];
        int pivR = 0;
        for (int i = 0; i < M.cols(); i++) {
            for (int j = 0; j < M.rows(); j++) {
                int v = M.get(j, i);
                if ((j == pivR && v == 0) || (j != pivR && v != 0)) {
                    free[i] = true;
                    break;
                }
            }
            if (!free[i]) {
                pivR++;
            }
        }
        LOGGER.finer("Free variables: " + Arrays.toString(free));
        return free;
    }
}
