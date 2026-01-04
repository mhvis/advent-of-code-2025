import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static void main() {
        HashMap<String, String[]> input = parse();

        if (input.containsKey("you")) {
            System.out.println(dfsPart1(input, "you"));
        } else {
            System.out.println("'you' not found for part 1");
        }

        if (input.containsKey("svr")) {
            PathCount result = dfsPart2(input, "svr", new HashMap<>());
            System.out.println(result.both);
        } else {
            System.out.println("'svr' not found for part 2");
        }
    }

    /**
     * Part 2: depth-first search to count the paths with constraints.
     * <p>
     * Includes a memoization map to prevent double work (very basic top-down dynamic
     * programming).
     */
    static PathCount dfsPart2(HashMap<String, String[]> input, String device, HashMap<String, PathCount> memo) {
        // Base case
        if (device.equals("out")) {
            return PathCount.unit();
        }

        // Already seen
        if (memo.containsKey(device)) {
            return memo.get(device);
        }

        // Recurse
        PathCount count = new PathCount();
        for (String dev : input.get(device)) {
            count.add(dfsPart2(input, dev, memo));
        }

        if (device.equals("dac")) {
            count.dac = count.total;
            count.both = count.fft;
        }
        if (device.equals("fft")) {
            count.fft = count.total;
            count.both = count.dac;
        }

        memo.put(device, count);
        return count;
    }

    static class PathCount {
        public long total;
        public long fft;
        public long dac;
        public long both;

        public static PathCount unit() {
            PathCount c = new PathCount();
            c.total = 1;
            return c;
        }

        public void add(PathCount c) {
            total += c.total;
            fft += c.fft;
            dac += c.dac;
            both += c.both;
        }
    }

    /**
     * Part 1: depth-first search to count all paths.
     */
    static int dfsPart1(HashMap<String, String[]> input, String device) {
        if (device.equals("out")) {
            return 1;
        }

        int count = 0;
        for (String dev : input.get(device)) {
            count += dfsPart1(input, dev);
        }
        return count;
    }

    static HashMap<String, String[]> parse() {
        HashMap<String, String[]> map = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(" ");
            map.put(parts[0].substring(0, parts[0].length() - 1), Arrays.copyOfRange(parts, 1, parts.length));
        }
        return map;
    }
}
