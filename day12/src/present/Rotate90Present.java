package present;

/**
 * Decorator that rotates the present 90 degrees.
 */
public class Rotate90Present extends StringablePresent {
    private final Present present;

    public Rotate90Present(Present present) {
        this.present = present;
    }

    @Override
    public boolean getCell(int x, int y) {
        return present.getCell(y, 2 - x);
    }
}
