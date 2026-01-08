public class Grid {
    private boolean[][] grid;

    public Grid(int width, int length) {
        grid = new boolean[width][length];
    }

    public void put(int x, int y, Present present) {

    }

    /**
     * Returns true when a 3x3 square with top left corner at (x,y) fits in the grid.
     */
    public boolean isSquareWithinBounds(int x, int y) {
        return false;
    }

    public int width() {
        return grid.length;
    }

    public int length() {
        return grid[0].length;
    }
}
