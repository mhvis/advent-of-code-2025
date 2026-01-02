import java.util.ArrayList;
import java.util.Scanner;

public class RREF {
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
            Matrix M = new Matrix(problem.wirings.length, problem.joltage.length);
            for (int i = 0; i < problem.wirings.length; i++) {
                for (int button : problem.wirings[i]) {
                    M.set(i, button, 1);
                }
            }
            // Construct RHS of the matrix
            for (int i = 0; i < problem.joltage.length; i++) {
                M.setY(i, problem.joltage[i]);
            }

            // Bring to row reduced echelon form
            new RrefComputer().execute(M);

            // Print result
            System.out.println(M);
            System.out.print(".");
        }
        System.out.println();
        System.out.println(sum);
    }

    /**
     * Row reduced echelon form solver.
     */
    static interface RrefComputerInterface {
        void execute(Matrix M);
    }

    /**
     * Basic implementation.
     */
    static class RrefComputer implements RrefComputerInterface {
        public void execute(Matrix M) {
            // For each column
            for (int i = 0; i < M.cols(); i++) {
                // Find the first non-zero row in the column, at or after i
                int r = i;
                while (M.get(r, i) == 0) {
                    r++;
                }

                // Put this row at i
                M.swap(r, i);

                // Make every non-zero values below row i 0, by adding/removing row i
                for (int j = i + 1; j < M.cols(); j++) {
                    if (M.get(j, i) != 0) {
                        M.add(i, 1, j);  // TODO scalar?
                    }
                }

            }
        }
    }

    /**
     * Matrix class supporting elementary matrix operations.
     */
    static class Matrix {
        private final int[][] M;
        private final int[] y;  // Right hand side

        /**
         * Contructs the matrix with initially all zeros.
         */
        public Matrix(int rows, int cols) {
            this.M = new int[rows][cols];
            this.y = new int[rows];
        }


        /**
         * Swaps two rows.
         */
        public void swap(int rowA, int rowB) {
            int carry;
            for (int i = 0; i < cols(); i++) {
                carry = M[rowA][i];
                M[rowA][i] = M[rowB][i];
                M[rowB][i] = carry;
            }

            // RHS
            carry = y[rowA];
            y[rowA] = y[rowB];
            y[rowB] = carry;
        }


        /**
         * Adds row*scalar to otherRow.
         */
        public void add(int row, int scalar, int otherRow) {
            for (int i = 0; i < cols(); i++) {
                M[otherRow][i] += M[row][i] * scalar;
            }
            y[otherRow] += y[row] * scalar;
        }

        public void scale(int row, int scalar) {

        }

        public int get(int row, int col) {
            return M[row][col];
        }

        public void set(int row, int col, int val) {
            M[row][col] = val;
        }

        public int getY(int row) {
            return y[row];
        }

        /**
         * Sets the right-hand side of a row.
         */
        public void setY(int row, int val) {
            y[row] = val;
        }

        public int rows() {
            return M.length;
        }

        public int cols() {
            return M[0].length;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < rows(); i++) {
                for (int j = 0; j < cols(); j++) {
                    builder.append(String.format("%03d", M[i][j]));
                }
                builder.append(" |");
                builder.append(String.format("%03d", y[i]));
            }
            return builder.toString();
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