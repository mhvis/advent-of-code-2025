package present;

import java.util.Scanner;

public class BasePresent extends StringablePresent {
    private final boolean[][] cells = new boolean[3][3];

    /**
     * Assumes that the next item on the scanner is the first line of the present.
     */
    public static BasePresent fromScanner(Scanner scanner) {
        BasePresent present = new BasePresent();

        for (int y = 0; y < 3; y++) {
            String line = scanner.next();
            for (int x = 0; x < 3; x++) {
                present.cells[x][y] = line.charAt(x) == '#';
            }
        }

        return present;
    }

    @Override
    public boolean getCell(int x, int y) {
        return cells[x][y];
    }
}
