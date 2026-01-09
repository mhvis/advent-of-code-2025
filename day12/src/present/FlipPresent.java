package present;

/**
 * Decorator that flips the present horizontally.
 */
public class FlipPresent extends StringablePresent {
    private final Present present;

    public FlipPresent(Present present) {
        this.present = present;
    }

    @Override
    public boolean getCell(int x, int y) {
        return present.getCell(2-x, y);
    }
}
