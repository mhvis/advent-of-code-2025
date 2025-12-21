import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * O(n^2) implementation.
 * A O(n log n) implementation may be possible, but I couldn't think of it.
 */
public class Day9Part1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Point> points = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String[] comp = scanner.nextLine().split(",");
            points.add(new Point(Integer.parseInt(comp[0]), Integer.parseInt(comp[1])));
        }

        // Maximize the area
        long max = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point p = points.get(i);
                Point q = points.get(j);
                long area = (long) (Math.abs(p.x - q.x) + 1) * (Math.abs(p.y - q.y) + 1);
                if (area > max) {
                    max = area;
                }
            }
        }

        System.out.println(max);
    }
}
