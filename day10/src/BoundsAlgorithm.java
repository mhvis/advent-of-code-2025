public interface BoundsAlgorithm {
    /**
     * Uses the given upper and lower bounds and restricts them further using the matrix.
     *
     * @return true when restriction was possible, false when we encountered a contradiction
     */
    boolean execute(Matrix M, int[] lower, int[] upper);
}
