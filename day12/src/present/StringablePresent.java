package present;

public abstract class StringablePresent implements Present {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < 3; y++) {
            if (y != 0) {
                builder.append('\n');
            }
            for (int x = 0; x < 3; x++) {
                builder.append(getCell(x, y) ? '#' : '.');
            }
        }
        return builder.toString();
    }
}
