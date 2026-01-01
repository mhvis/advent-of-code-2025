import java.util.*;

public class Day10Part1 {
    public static void main(String[] args) {
        // Parse input
        ArrayList<Problem> problems = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            problems.add(Problem.fromLine(scanner.nextLine()));
        }

        int sum = 0;
        for (Problem problem : problems) {
            // Breadth-first search solution
            Queue<boolean[][]> queue = new LinkedList<>();

            // Initially all lights are off (false)
            queue.add(new boolean[1][problem.lights.length]);

            int minPresses;
            while (true) {
                boolean[][] current = queue.poll();

                // Stop when the current state is the final state
                assert current != null;
                if (Arrays.equals(current[current.length - 1], problem.lights)) {
                    minPresses = current.length - 1;
                    break;
                }

                // Go over all wirings
                for (int[] wiring : problem.buttons) {
                    // Construct the lights state after pressing the button combination
                    boolean[] nextState = current[current.length - 1].clone();
                    for (int button : wiring) {
                        nextState[button] = !nextState[button];
                    }

                    // If we have seen this state before, prune this tree
                    boolean seen = false;
                    for (boolean[] earlierState : current) {
                        if (Arrays.equals(earlierState, nextState)) {
                            seen = true;
                            break;
                        }
                    }
                    if (seen) {
                        continue;
                    }

                    // Add the new state to the queue
                    boolean[][] newState = Arrays.copyOf(current, current.length + 1);
                    newState[newState.length - 1] = nextState;
                    queue.add(newState);
                }
            }
            sum += minPresses;
        }
        System.out.println(sum);
    }

    static class Problem {
        public boolean[] lights;
        public int[][] buttons;
        public String joltage;

        public Problem(boolean[] lights, int[][] buttons, String joltage) {
            this.lights = lights;
            this.buttons = buttons;
            this.joltage = joltage;
        }

        public static Problem fromLine(String line) {
            String[] components = line.split(" ");

            String diagram = components[0];
            boolean[] lights = new boolean[diagram.length() - 2];
            for (int i = 0; i < lights.length; i++) {
                lights[i] = diagram.charAt(i + 1) == '#';
            }

            int[][] buttons = new int[components.length - 2][];
            for (int i = 0; i < buttons.length; i++) {
                String button = components[i + 1].substring(1, components[i + 1].length() - 1);
                String[] wiringIndexes = button.split(",");
                buttons[i] = new int[wiringIndexes.length];
                for (int j = 0; j < wiringIndexes.length; j++) {
                    buttons[i][j] = Integer.parseInt(wiringIndexes[j]);
                }
            }

            return new Problem(lights, buttons, components[components.length - 1]);
        }

    }
}