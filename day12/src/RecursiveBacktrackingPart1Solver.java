import present.BasePresent;
import present.FlipPresent;
import present.Present;
import present.Rotate90Present;

import java.awt.*;
import java.util.Arrays;

public class RecursiveBacktrackingPart1Solver implements Part1Solver {
    // There must be a smarter way...
    private final Point[][] FANNINGS = new Point[][]{
            // Top side
            new Point[]{new Point(-2, -1), new Point(-2, -2), new Point(-2, -3)}, new Point[]{new Point(-1, -1), new Point(-1, -2), new Point(-1, -3)}, new Point[]{new Point(0, -1), new Point(0, -2), new Point(0, -3)}, new Point[]{new Point(1, -1), new Point(1, -2), new Point(1, -3)}, new Point[]{new Point(2, -1), new Point(2, -2), new Point(2, -3)},
            // Right side
            new Point[]{new Point(1, -2), new Point(2, -2), new Point(3, -2)}, new Point[]{new Point(1, -1), new Point(2, -1), new Point(3, -1)}, new Point[]{new Point(1, 0), new Point(2, 0), new Point(3, 0)}, new Point[]{new Point(1, 1), new Point(2, 1), new Point(3, 1)}, new Point[]{new Point(1, 2), new Point(2, 2), new Point(3, 2)},
            // Bottom side
            new Point[]{new Point(-2, 1), new Point(-2, 2), new Point(-2, 3)}, new Point[]{new Point(-1, 1), new Point(-1, 2), new Point(-1, 3)}, new Point[]{new Point(0, 1), new Point(0, 2), new Point(0, 3)}, new Point[]{new Point(1, 1), new Point(1, 2), new Point(1, 3)}, new Point[]{new Point(2, 1), new Point(2, 2), new Point(2, 3)},
            // Left side
            new Point[]{new Point(-1, -2), new Point(-2, -2), new Point(-3, -2)}, new Point[]{new Point(-1, -1), new Point(-2, -1), new Point(-3, -1)}, new Point[]{new Point(-1, 0), new Point(-2, 0), new Point(-3, 0)}, new Point[]{new Point(-1, 1), new Point(-2, 1), new Point(-3, 1)}, new Point[]{new Point(-1, 2), new Point(-2, 2), new Point(-3, 2)},};
    private final BasePresent[] presents;
    private final Problem problem;
    private final SimpleLogger logger;
    private Grid grid;

    public RecursiveBacktrackingPart1Solver(BasePresent[] presents, Problem problem, SimpleLogger logger) {
        this.presents = presents;
        this.problem = problem;
        this.logger = logger;
    }

    @Override
    public boolean solve() {
        grid = new Grid(problem.width, problem.length);

        for (int i = 0; i < presents.length; i++) {
            if (problem.shapeQuantities[i] > 0) {
                // Use up one of the available presents
                problem.shapeQuantities[i]--;

                // For all possible shapes of the present
                for (Present present : getShapes(presents[i])) {
                    // Start by placing the first present against the border of the grid (Lemma 1). We
                    // need to try half of the possible positions on two cornering edges. The other
                    // positions are covered by symmetry.
                    for (int x = 0; x < (grid.width() - 3) / 2 + 1; x++) {
                        if (recurse(present, x, 0)) {
                            return true;
                        }
                    }
                    for (int y = 0; y < (grid.length() - 3) / 2 + 1; y++) {
                        if (recurse(present, 0, y)) {
                            return true;
                        }
                    }
                }
                problem.shapeQuantities[i]++;
            }
        }
        return false;
    }

    /**
     * Puts the present at given position and recurses by placing a new present next to it.
     */
    private boolean recurse(Present present, int x, int y) {
        logger.finer("Placing present at (" + x + "," + y + ") with quantities " + Arrays.toString(problem.shapeQuantities));
        grid.put(present, x, y);
        logger.finest(grid.toString());

        if (Arrays.stream(problem.shapeQuantities).allMatch((int v) -> v == 0)) {
            // All presents are placed. We found a solution.
            return true;
        }

        for (int i = 0; i < presents.length; i++) {
            if (problem.shapeQuantities[i] > 0) {
                // Use up one of the available presents
                problem.shapeQuantities[i]--;

                // For all possible shapes of the present
                for (Present shape : getShapes(presents[i])) {

                    // Fan out in all directions until the shape fits
                    for (Point[] fanout : FANNINGS) {
                        for (Point point : fanout) {
                            // If it fits, recurse
                            if (grid.fits(shape, x + point.x, y + point.y)) {
                                if (recurse(shape, x + point.x, y + point.y)) {
                                    return true;
                                }
                                break;
                            }
                        }
                    }
                }
                problem.shapeQuantities[i]++;
            }
        }
        grid.remove(present, x, y);
        return false;
    }

    /**
     * Returns all possible rotations and flips of the given present.
     */
    private Present[] getShapes(Present present) {
        Present[] shapes = new Present[8];
        for (int i = 0; i < 4; i++) {
            // Rotate 90 degrees
            present = new Rotate90Present(present);
            shapes[i] = present;
            shapes[i + 4] = new FlipPresent(present);
        }
        return shapes;
    }
}
