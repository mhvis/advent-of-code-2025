import present.BasePresent;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static void main() {
        SimpleLogger logger = new SimpleLogger(System.out);
        logger.setFine();

        // Input parsing
        Scanner scanner = new Scanner(System.in);
        ArrayList<BasePresent> presentList = new ArrayList<>();
        ArrayList<Problem> problems = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.endsWith(":")) {
                presentList.add(BasePresent.fromScanner(scanner));
            } else if (line.contains("x")) {
                // Tree region line
                problems.add(Problem.parseLine(line));
            }
        }
        BasePresent[] presents = presentList.toArray(new BasePresent[0]);
        int total = 0;
        for (Problem problem : problems) {
            if (new RecursiveBacktrackingPart1Solver(presents, problem, logger).solve()) {
                total += 1;
            }
        }
        System.out.println();
        System.out.println(total);
    }
}
