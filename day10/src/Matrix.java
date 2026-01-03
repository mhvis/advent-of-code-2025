import java.util.logging.Logger;

/**
 * Matrix class with elementary matrix operations.
 */
public class Matrix {
    private static final Logger LOGGER = Logger.getLogger(Matrix.class.getName());
    private final int[][] M;
    private final int[] y;  // Right hand side

    /**
     * Constructs the matrix with initially all zeros.
     */
    public Matrix(int rows, int cols) {
        this.M = new int[rows][cols];
        this.y = new int[rows];
    }

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

        if (rowA != rowB) {
            LOGGER.finer("Swapped row " + rowA + " and " + rowB);
            LOGGER.finest("\n" + this);
        }
    }

    /**
     * Adds scalar*otherRow to row
     */
    public void add(int row, int otherRow, int scalar) {
        for (int i = 0; i < cols(); i++) {
            M[row][i] += scalar * M[otherRow][i];
        }
        y[row] += scalar * y[otherRow];

        if (scalar != 0) {
            LOGGER.finer("Added " + scalar + " times row " + otherRow + " to row " + row);
            LOGGER.finest("\n" + this);
        }
    }

    public void scale(int row, int scalar) {
        for (int i = 0; i < cols(); i++) {
            M[row][i] *= scalar;
        }
        y[row] *= scalar;

        if (scalar != 1) {
            LOGGER.finer("Scaled row " + row + " by " + scalar);
            LOGGER.finest("\n" + this);
        }
    }

    /**
     * Integer division.
     *
     * <p>We have a separate division method next to scale() for integer division
     * specifically.
     */
    public void divide(int row, int scalar) {
        for (int i = 0; i < cols(); i++) {
            M[row][i] = Util.safeDiv(M[row][i], scalar);
        }
        y[row] = Util.safeDiv(y[row], scalar);

        if (scalar != 1) {
            LOGGER.finer("Divided row " + row + " by " + scalar);
            LOGGER.finest("\n" + this);
        }
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
     * Sets the right-hand side value of a row.
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
            if (i != 0) {
                builder.append('\n');
            }
            for (int j = 0; j < cols(); j++) {
                builder.append(String.format("%3d", M[i][j]));
            }
            builder.append(" |");
            builder.append(String.format("%3d", y[i]));
        }
        return builder.toString();
    }
}
