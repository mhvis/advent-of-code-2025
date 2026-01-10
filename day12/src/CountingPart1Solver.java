import present.Present;

/**
 * Hint source: <a href="https://www.reddit.com/r/adventofcode/comments/1q0f55h/comment/nwygwm7/">Reddit</a>
 */
public class CountingPart1Solver implements Part1Solver {
    private final Problem problem;
    private final Present[] presents;

    public CountingPart1Solver(Problem problem, Present[] presents) {
        this.problem = problem;
        this.presents = presents;
    }

    @Override
    public boolean solve() {
        int squareCount = 0;
        for (int i = 0; i < presents.length; i++) {
            squareCount += countSquares(presents[i]) * problem.shapeQuantities[i];
        }
        return squareCount <= problem.width * problem.length;
    }

    private int countSquares(Present present) {
        int count = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (present.getCell(x, y)) {
                    count++;
                }
            }
        }
        return count;
    }
}
