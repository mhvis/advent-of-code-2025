import present.Present;

public class Grid {
    private final boolean[][] grid;

    public Grid(int width, int length) {
        grid = new boolean[width][length];
    }

    public void put(Present present, int x, int y) {
        set(present, x, y, true);
    }

    public void remove(Present present, int x, int y) {
        set(present, x, y, false);
    }

    private void set(Present present, int x, int y, boolean value) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (present.getCell(i, j)) {
                    grid[x + i][y + j] = value;
                }
            }
        }
    }

    /**
     * Returns false when the x/y is outside bounds or one of the cells is occupied.
     */
    public boolean fits(Present present, int x, int y) {
        if (x < 0 || y < 0 || x > grid.length - 3 || y > grid[0].length - 3) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (present.getCell(i, j) && grid[x + i][y + j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public int width() {
        return grid.length;
    }

    public int length() {
        return grid[0].length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < length(); y++) {
            if (y != 0) {
                sb.append('\n');
            }
            for (int x = 0; x < width(); x++) {
                sb.append(grid[x][y] ? '#' : '.');
            }
        }
        return sb.toString();
    }
}
