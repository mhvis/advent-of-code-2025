
## Notes for the recursive backtracking solver

The problem is NP-complete and the recursive backtracking solver (RecursiveBacktrackingPart1Solver)
is too slow. The CountingPart1Solver does work on the large input.


### Lemma 1
If there is a solution, there will always be a solution where
one of the presents touches the border of the grid.

Proof: trivial.

*Result: we only need to try arrangements with presents touching the border.*


### Lemma 2

If there is a solution, there will always be a solution where
all presents touch another present on at least one side.

Proof: trivial.

*Result: we can build up a solution by placing the next present touching the current present.*

### Assumptions

* A present is always 3x3 and always requires at least a 3x3 space to fit.
