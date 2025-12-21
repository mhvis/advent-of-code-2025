import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Part 1 is O(n^2). Part 2 is O(n^3) (!).
 * <p>
 * We make the assumption that each line segment has at least length 3. Otherwise,
 * it is possible to 'turn around' by going left twice, without leaving a gap in
 * between.
 * <p>
 * With the above assumption, for part 2, we can simply check if another point (red
 * tile) is strictly inside the rectangle. In that case, the rectangle is invalid,
 * because at least one of the eight surrounding squares of that inner point will
 * be empty.
 */
public class Day9Part2 {
    public static void main(String[] args) {
        // Input parsing
        ArrayList<Point> points = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] comp = scanner.nextLine().split(",");
            points.add(new Point(Integer.parseInt(comp[0]), Integer.parseInt(comp[1])));
        }

        // Maximize the rectangle (with restriction for part 2)
        long maxPart1 = 0;
        long maxPart2 = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Rectangle r = new Rectangle(points.get(i), points.get(j));
                long area = r.area();

                // Part 1
                maxPart1 = Math.max(maxPart1, area);

                if (area ==2) {
                    throw new RuntimeException("Assumption failed :(, see Javadoc");
                }

                // Part 2 (see Javadoc)
                boolean valid = true;
                for (int k = 0; k < points.size(); k++) {
                    if (r.strictlyInside(points.get(k))) {
//                    if (r.lineSegmentIntersects(points.get(k), points.get((k + 1) % points.size()))) {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    maxPart2 = Math.max(maxPart2, area);
                }
            }
        }

        System.out.println(maxPart1);
        System.out.println(maxPart2);
    }

    static class Rectangle {
        Point a;
        Point b;

        Rectangle(Point a, Point b) {
            this.a = a;
            this.b = b;
        }

        public long area() {
            return (long) (Math.abs(a.x - b.x) + 1) * (Math.abs(a.y - b.y) + 1);
        }

        public boolean strictlyInside(Point p) {
            return this.minX() < p.x && p.x < this.maxX() && this.minY() < p.y && p.y < this.maxY();
        }

        /**
         * Returns true when the line *segment* is partly or fully inside the rectangle.
         */
        public boolean lineSegmentIntersects(Point segA, Point segB) {
            if (segA.x == segB.x) {
                // Vertical line segment at x between minY and maxY
                int x = segA.x;
                int minY = Math.min(segA.y, segB.y);
                int maxY = Math.max(segA.y, segB.y);
                // The vertical line should be between the vertical sides of the rectangle
                return (this.minX() < x && x < this.maxX())
                        // At least one of the rectangle horizontal sides needs to cross the line segment
                        && ((minY < a.y && a.y < maxY) || (minY < b.y && b.y < maxY));
            } else if (segA.y == segB.y) {
                // Same as above but with x/y flipped
                int y =  segA.y;
                int minX = Math.min(segA.x, segB.x);
                int maxX = Math.max(segA.x, segB.x);
                return (this.minY() < y && y < this.maxY())
                        && ((minX < a.x && a.x < maxX) || (minX < b.x && b.x < maxX));
            }
            throw new IllegalArgumentException("Line is not vertical or horizontal");
        }

        public int minX() {
            return Math.min(a.x, b.x);
        }

        public int maxX() {
            return Math.max(a.x, b.x);
        }

        public int minY() {
            return Math.min(a.y, b.y);
        }

        public int maxY() {
            return Math.max(a.y, b.y);
        }
    }
}
