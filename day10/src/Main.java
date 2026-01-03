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

        // Guess

        // Prepare lower and upper bounds of all coefficients, initially set to 0 and Infinity respectively
        int[] lower = new int[M.cols()];
        int[] upper = new int[M.cols()];
        Arrays.fill(upper, Integer.MAX_VALUE);

        new SimpleBoundsAlgorithm().execute(M, lower, upper);

        return 0;
    }

    /**
     * Finds pivots and free variables in a matrix in row reduced echelon form.
     */
    private static boolean[] frees(Matrix M) {
        boolean[] free = new boolean[M.cols()];
        int pivR = 0;
        for (int i = 0; i < M.cols(); i++) {
            for (int j = 0; j < M.rows(); j++) {
                if (M.get(j, i) != ((pivR == j) ? 1 : 0)) {
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
