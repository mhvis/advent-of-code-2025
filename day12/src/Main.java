import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static void main() {
        // Set log level
        LOGGER.getParent().getHandlers()[0].setLevel(Level.ALL);
        LOGGER.getParent().setLevel(Level.OFF);

        // Input parsing
        Scanner scanner = new Scanner(System.in);
        ArrayList<Present> presents = new ArrayList<>();
        ArrayList<Problem> regions = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.endsWith(":")) {
                presents.add(Present.fromScanner(scanner));
            } else if (line.contains("x")) {
                // Tree region line
                regions.add(Problem.parseLine(line));
            }
        }

        // For each present, an array of all its possible shapes derived from the standard shape
        boolean[][][][] derivatives = new boolean[presents.size()][][][];
        for (int i = 0; i < presents.size(); i++) {
            derivatives[i] = constructDerivatives(presents.get(i));
        }

        // for (int i = 0; i < derivatives.length; i++) {
        //     for (int j = 0; j < derivatives[i].length; j++) {
        //         System.out.println("Shape " + i + " with derivative " + j);
        //         System.out.println(presentToString(derivatives[i][j]));
        //     }
        // }

        int total = 0;
        for (Problem region : regions) {
            if (dfsPart1(derivatives, region.toGrid(), region.toWorkList(), 0)) {
                total++;
            }
            System.out.print(".");
        }
        System.out.println();
        System.out.println(total);
    }



    static boolean dfsPart1(boolean[][][][] shapes, boolean[][] grid, int[] workList, int depth) {
        LOGGER.finer("dfsPart1 with depth: " + depth);
        LOGGER.finest("Grid:\n" + gridToString(grid));

        if (depth == workList.length) {
            // Got all presents fitted
            return true;
        }

        // The current present to fit
        boolean[][][] p = shapes[workList[depth]];

        // Try all shape derivatives
        for (boolean[][] s : p) {
            // Try all spots in the grid
            for (int y = 0; y < grid.length - s.length + 1; y++) {
                for (int x = 0; x < grid[y].length - s[0].length + 1; x++) {
                    // Skip when it does not fit
                    if (!fits(grid, s, x, y)) {
                        continue;
                    }

                    put(grid, s, x, y, true);

                    boolean result = dfsPart1(shapes, grid, workList, depth + 1);

                    // If we have already found *a* solution, we can stop directly
                    if (result) {
                        return true;
                    }

                    // Undo placing this shape (instead of copying the grid which would require a lot of memory allocations)
                    put(grid, s, x, y, false);
                }
            }
        }

        // No solution found
        return false;
    }

    /**
     * Returns true if the shape can be placed, i.e. there is no other shape there already.
     */
    static boolean fits(boolean[][] grid, boolean[][] shape, int x, int y) {
        for (int j = 0; j < shape.length; j++) {
            for (int i = 0; i < shape[j].length; i++) {
                if (shape[j][i] && grid[y + j][x + i]) {
                    return false;
                }
            }
        }
        return true;
    }

    static void put(boolean[][] grid, boolean[][] shape, int x, int y, boolean value) {
        for (int j = 0; j < shape.length; j++) {
            for (int i = 0; i < shape[j].length; i++) {
                if (shape[j][i]) {
                    grid[y + j][x + i] = value;
                }
            }
        }
    }

    /**
     * Returns all rotations and flips of a present.
     */
    static boolean[][][] constructDerivatives(boolean[][] p) {
        // All shapes seem to be square so let's assume that
        int s = p.length;
        boolean[][][] shapes = new boolean[8][s][s];
        // Rotations
        for (int y = 0; y < p.length; y++) {
            for (int x = 0; x < p[y].length; x++) {
                shapes[0][y][x] = p[y][x];
                shapes[1][y][x] = p[x][s - y - 1];
                shapes[2][y][x] = p[s - y - 1][s - x - 1];
                shapes[3][y][x] = p[s - x - 1][y];
            }
        }
        // (Horizontal) flips
        for (int y = 0; y < p.length; y++) {
            for (int x = 0; x < p[y].length; x++) {
                shapes[4][y][x] = shapes[0][y][s - x - 1];
                shapes[5][y][x] = shapes[1][y][s - x - 1];
                shapes[6][y][x] = shapes[2][y][s - x - 1];
                shapes[7][y][x] = shapes[3][y][s - x - 1];
            }
        }

        return shapes;
    }

    static String gridToString(boolean[][] p) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < p.length; y++) {
            if (y != 0) {
                sb.append('\n');
            }
            for (int x = 0; x < p[y].length; x++) {
                sb.append(p[y][x] ? '#' : '.');
            }
        }
        return sb.toString();
    }
}
