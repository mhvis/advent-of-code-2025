import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Day10ILP {
    public static void main(String[] args) {
        // Parse input
        ArrayList<Problem> problems = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            problems.add(Problem.fromLine(scanner.nextLine()));
        }

        int sum = 0;
        for (Problem problem : problems) {
            // Construct matrix
            int[][] M = new int[problem.wirings.length][problem.joltage.length];
            for (int i = 0; i < problem.wirings.length; i++) {
                for (int button : problem.wirings[i]) {
                    M[i][button] = 1;
                }
            }

            // Column vector for the right side of the equation
            int[] y = problem.joltage;

            // Initialize the lower and upper bounds to 0 and infinity respectively
            int[] lower = new int[M.length];
            int[] upper = new int[M.length];
            Arrays.fill(upper, Integer.MAX_VALUE);

            sum += Day10ILP.minimizeIlp(M, y, lower, upper, Integer.MAX_VALUE, 0);

            System.out.print(".");
        }
        System.out.println();
        System.out.println(sum);
    }

    private static int minimizeIlp(int[][] M, int[] y, int[] lower, int[] upper, int min, int depth) {
        // Update lower and upper bounds
        lower = lower.clone();
        upper = upper.clone();
        Day10ILP.computeBounds(M, y, lower, upper);

        // Pruning: we can prune this tree when the lower bound sum is already greater than or equal to the global min
        if (Arrays.stream(lower).sum() >= min) {
            return min;
        }

        // Base case: return infinity when there is a contradiction (lower bound is above an upper bound)
        for (int i = 0; i < M.length; i++) {
            if (lower[i] > upper[i]) {
                return Integer.MAX_VALUE;
            }
        }

        // Base case: return the sum (total number of button presses) when there are no unknown coefficients left
        if (Arrays.equals(lower, upper)) {
            return Arrays.stream(lower).sum();
        }

        // Find an unknown coefficient with the smallest [lower, upper] range
        int c = -1;
        for (int i = 0; i < M.length; i++) {
            if (lower[i] != upper[i]) {
                if (c == -1 || upper[i] - lower[i] < upper[c] - lower[c]) {
                    c = i;
                }
            }
        }

        // Guess all values for the chosen coefficient, and pick the minimal result
        int lowerC = lower[c];
        int upperC = upper[c];
        int res;
        for (int v = lowerC; v <= upperC; v++) {
            lower[c] = v;
            upper[c] = v;
            res = minimizeIlp(M, y, lower, upper, min, depth + 1);
            System.out.println("depth=" + depth + " c=" + c + " v=" + v + " min=" + min + " res=" + res + " lower=" + Arrays.toString(lower) + " upper=" + Arrays.toString(upper));
            min = Math.min(min, res);
        }

        return min;
    }

    private static void computeBounds(int[][] M, int[] y, int[] lower, int[] upper) {
        boolean changed = true;
        while (changed) {
            changed = false;

//            System.out.println("Pre-compute-bound");
//            System.out.println(Arrays.toString(upper));
//            System.out.println(Arrays.toString(lower));

            // We find the new upper bounds by minimizing the other coefficients, i.e., using the lower bounds
            for (int i = 0; i < M.length; i++) {
                for (int j = 0; j < y.length; j++) {
                    if (M[i][j] == 1) {
                        int v = y[j];  // Find the upper bound for this equation
                        for (int k = 0; k < M.length; k++) {
                            if (k != i) {
                                v -= M[k][j] * lower[k];
                            }
                        }
                        upper[i] = Math.min(upper[i], v);
                        if (upper[i] < lower[i]) {
                            return;  // Contradiction
                        }
                        assert upper[i] >= 0;  // This should never become negative
                    }
                }
            }

            // Inverse for the new lower bounds
            for (int i = 0; i < M.length; i++) {
                for (int j = 0; j < y.length; j++) {
                    if (M[i][j] == 1) {
                        int v = y[j];
                        for (int k = 0; k < M.length; k++) {
                            if (k != i) {
                                v -= M[k][j] * upper[k];
                            }
                        }
                        if (v > lower[i]) {
                            lower[i] = v;
                            changed = true;  // Update the upper bounds again when the lower bounds changed
                        }
                        if (lower[i] > upper[i]) {
                            // Contradiction; stop
                            return;
                        }
                        assert lower[i] <= upper[i];
                    }
                }
            }
        }
    }

    static class Problem {
        public boolean[] lights;
        public int[][] wirings;
        public int[] joltage;

        public Problem(boolean[] lights, int[][] wirings, int[] joltage) {
            this.lights = lights;
            this.wirings = wirings;
            this.joltage = joltage;
        }

        public static Problem fromLine(String line) {
            String[] components = line.split(" ");

            String diagram = components[0];
            boolean[] lights = new boolean[diagram.length() - 2];
            for (int i = 0; i < lights.length; i++) {
                lights[i] = diagram.charAt(i + 1) == '#';
            }

            int[][] buttons = new int[components.length - 2][];
            for (int i = 0; i < buttons.length; i++) {
                String button = components[i + 1].substring(1, components[i + 1].length() - 1);
                String[] wiringIndexes = button.split(",");
                buttons[i] = new int[wiringIndexes.length];
                for (int j = 0; j < wiringIndexes.length; j++) {
                    buttons[i][j] = Integer.parseInt(wiringIndexes[j]);
                }
            }

            String joltageComponent = components[components.length - 1];
            joltageComponent = joltageComponent.substring(1, joltageComponent.length() - 1);
            String[] joltages = joltageComponent.split(",");
            int[] joltage = new int[joltages.length];
            for (int i = 0; i < joltages.length; i++) {
                joltage[i] = Integer.parseInt(joltages[i]);
            }

            return new Problem(lights, buttons, joltage);
        }
    }
}