package present;

/**
 * A present is always 3x3 and requires at least a 3x3 area to fit.
 */
public interface Present {
    /**
     * Returns true when the cell is occupied in the 3x3 grid of the present.
     */
    boolean getCell(int x, int y);
}
