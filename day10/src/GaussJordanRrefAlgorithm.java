import java.util.logging.Logger;

/**
 * Gauss-Jordan elimination to achieve a row reduced echelon form.
 */
public class GaussJordanRrefAlgorithm implements RrefAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(GaussJordanRrefAlgorithm.class.getName());

    @Override
    public void execute(Matrix M) {
        int r = 0;

        // For each column
        for (int i = 0; i < M.cols(); i++) {
            // Find the first non-zero row in the column, at or after the current row
            int j = r;
            while (j < M.rows() && M.get(j, i) == 0) {
                j++;
            }
            // If there is none, try the next column with the same row
            if (j >= M.rows()) {
                continue;
            }

            M.swap(j, r);

            // // Make sure that the pivot is one by dividing by itself
            // M.divide(r, M.get(r, i));

            // Cancel out every other non-zero value in the same column
            for (int k = 0; k < M.rows(); k++) {
                if (k != r) {
                    // Make both values divisible by scaling both rows using the least common multiple
                    int num = M.get(k, i);
                    if (num != 0) {
                        int denom = M.get(r, i);
                        M.scale(k, Util.lcm(num, denom) / Math.abs(num));
                        M.scale(r, Util.lcm(num, denom) / Math.abs(denom));
                    }

                    M.add(k, r, -1 * Util.safeDiv(M.get(k, i), M.get(r, i)));
                }
            }
            r++;
        }
    }
}
