import copy
import sys
from typing import NamedTuple
from unittest import result


def main():
    print("Part 1")
    fresh_ranges, available = parse_file(sys.argv[1])
    print(count_fresh(available, fresh_ranges))
    print("Part 2")
    print(part2(fresh_ranges))


class Range(NamedTuple):
    start: int
    end: int

    def in_range(self, val: int) -> bool:
        return self.start <= val <= self.end

    def size(self) -> int:
        return max(self.end - self.start + 1, 0)


def count_fresh(available: list[int], ranges: list[Range]) -> int:
    return sum(is_fresh(i, ranges) for i in available)


def is_fresh(ingredient: int, ranges: list[Range]) -> bool:
    for r in ranges:
        if r.in_range(ingredient):
            return True
    return False


def remove_overlap(ranges: list[Range]) -> list[Range]:
    """O(n^2) algorithm to remove overlap."""
    result = []

    for r in ranges:
        start = r.start
        end = r.end

        for other in result:
            # If the start is inside another range, move the start behind the other range
            if other.in_range(start):
                start = other.end + 1

            # If the end is inside another range, move the end in front of the other range
            if other.in_range(end):
                end = other.start - 1

        if start <= end:
            result.append(Range(start, end))

    # Remove ranges which are strictly inside another range
    return [
        r
        for r in result
        if not any(r.start > o.start and r.end < o.end for o in result)
    ]


def part2(ranges: list[Range]) -> int:
    return sum(r.size() for r in remove_overlap(ranges))


def parse_file(filename) -> tuple[list[Range], list[int]]:
    fresh_ranges = []
    available = []
    with open(filename) as f:
        for line in f:
            line = line.strip()
            if line == "":
                continue
            if "-" in line:
                fresh_ranges.append(
                    Range(int(line.split("-")[0]), int(line.split("-")[1]))
                )
            else:
                available.append(int(line))
    return fresh_ranges, available


if __name__ == "__main__":
    main()
