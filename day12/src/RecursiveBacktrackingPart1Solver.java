public class RecursiveBacktrackingPart1Solver implements Part1Solver {
    private Present[] presents;
    private Problem problem;

    public RecursiveBacktrackingPart1Solver(Present[] presents, Problem problem) {
        this.presents = presents;
        this.problem = problem;
    }

    @Override
    public boolean solve() {
        Grid grid = new Grid(problem.width, problem.length);

        for (int i = 0; i < presents.length; i++) {

        }
        return false;
    }

    private void recurse() {

    }
}
