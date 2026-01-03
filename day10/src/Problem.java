public class Problem {
    public boolean[] lights;
    public int[][] wirings;
    public int[] joltage;

    public Problem(boolean[] lights, int[][] wirings, int[] joltage) {
        this.lights = lights;
        this.wirings = wirings;
        this.joltage = joltage;
    }

    /**
     * Parses an input line into a Problem.
     */
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

        String joltageComponent = components[components.length - 1];
        joltageComponent = joltageComponent.substring(1, joltageComponent.length() - 1);
        String[] joltages = joltageComponent.split(",");
        int[] joltage = new int[joltages.length];
        for (int i = 0; i < joltages.length; i++) {
            joltage[i] = Integer.parseInt(joltages[i]);
        }

        return new Problem(lights, buttons, joltage);
    }
}
