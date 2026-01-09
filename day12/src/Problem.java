import java.util.Arrays;

public class Problem {
    public final int width;
    public final int length;
    public final int[] shapeQuantities;

    public Problem(int width, int length, int[] shapeQuantities) {
        this.width = width;
        this.length = length;
        this.shapeQuantities = shapeQuantities;
    }

    public static Problem parseLine(String line) {
        int width = Integer.parseInt(line.substring(0, line.indexOf('x')));
        int length = Integer.parseInt(line.substring(line.indexOf('x') + 1, line.indexOf(':')));
        String[] quantities = line.substring(line.indexOf(':') + 2).split(" ");
        int[] shapeQuantities = new int[quantities.length];
        for (int i = 0; i < shapeQuantities.length; i++) {
            shapeQuantities[i] = Integer.parseInt(quantities[i]);
        }
        return new Problem(width, length, shapeQuantities);
    }
}
