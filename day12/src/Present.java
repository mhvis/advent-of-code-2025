import java.util.Scanner;

/**
 * A present is always 3x3 and requires at least a 3x3 area to fit.
 */
public class Present {
    private final boolean[][] cells = new boolean[3][3];

    /**
     * Assumes that the next item on the scanner is the first line of the present.
     */
    public static Present fromScanner(Scanner scanner) {
        Present present = new Present();

        for (int y = 0; y < 3; y++) {
            String line = scanner.next();
            for (int x = 0; x < 3; x++) {
                present.set(x, y, line.charAt(x) == '#');
            }
        }

        return present;
    }

    public void set(int x, int y, boolean value) {
        cells[x][y] = value;
    }
}
