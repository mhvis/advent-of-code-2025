import sys
from dataclasses import dataclass
from functools import reduce


@dataclass
class Problem:
    vals: list[int]

    def solve(self) -> int:
        raise NotImplementedError


class Multiplication(Problem):
    def solve(self) -> int:
        return reduce(lambda x, y: x * y, self.vals)


class Addition(Problem):
    def solve(self) -> int:
        return sum(self.vals)


def main():
    print("Part 1")
    print(part1(sys.argv[1]))
    print("Part 2")
    print(part2(sys.argv[1]))


def part1(filename: str) -> int:
    with open(filename) as f:
        return solve(parse_input_part1(f.readlines()))


def part2(filename: str) -> int:
    with open(filename) as f:
        return solve(parse_input_part2(f.readlines()))


def solve(homework: list[Problem]) -> int:
    return sum(problem.solve() for problem in homework)


def parse_input_part2(lines: list[str]) -> list[Problem]:
    transposed = transpose_input(lines)
    current = None
    problems = []
    for column in transposed:
        if not column:
            # Empty columns reset the current problem so that a new problem will be added
            current = None
            continue

        if current is None:
            if column.endswith("*"):
                current = Multiplication([])
            elif column.endswith("+"):
                current = Addition([])
            else:
                raise ValueError(f"Unexpected column: {column}")
            problems.append(current)
            column = column[:-1]

        current.vals.append(int(column))

    return problems


def transpose_input(lines: list[str]) -> list[str]:
    """Transposes the columns in the input to rows *and discards whitespace*."""
    columns = []  # type: list[str]
    for line in lines:
        for i in range(len(line)):
            if len(columns) <= i:
                columns.append("")
            if not line[i].isspace():
                columns[i] += line[i]
    return columns


def parse_input_part1(lines: list[str]) -> list[Problem]:
    columns = None  # type: list[list[int]]|None
    for line in lines:
        row = line.strip().split()

        if not row:
            continue

        # Process bottom line
        if row[0] == "+" or row[0] == "*":
            return [
                Multiplication(columns[i]) if row[i] == "*" else Addition(columns[i])
                for i in range(len(columns))
            ]

        # Initialize columns
        if columns is None:
            columns = [[] for _ in range(len(row))]

        # Store row values
        for i in range(len(columns)):
            columns[i].append(int(row[i]))

    raise ValueError


if __name__ == "__main__":
    main()
