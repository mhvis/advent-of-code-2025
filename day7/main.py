import sys


def main():
    print("Part 1")
    print(part1(sys.argv[1]))


def part1(filename: str) -> int:
    with open(filename) as f:
        return solve_lines_part1(f.readlines())


def solve_lines_part1(lines: list[str]) -> int:
    beams = None  # type: list[bool]|None
    split_count = 0

    for line in lines:
        line = line.strip()
        if line == "":
            continue

        if beams is None:
            beams = get_starters(line)
        else:
            to_split = get_splits(beams, get_splitters(line))
            split_count += sum(to_split)
            split(beams, to_split)

    return split_count


def get_splitters(line: str) -> list[bool]:
    """Returns all splitters in an input line."""
    return [True if c == "^" else False for c in line]


def get_starters(line: str) -> list[bool]:
    return [True if c == "S" else False for c in line]


def merge_beams(a: list[bool], b: list[bool]) -> list[bool]:
    """Merges two beam lists into one beam list."""
    return [a or b for a, b in zip(a, b)]


def get_splits(beams: list[bool], splitters: list[bool]) -> list[bool]:
    """Returns the splits that need to be performed based on the current beams."""
    return [a and b for a, b in zip(beams, splitters)]


def split(beams: list[bool], to_split: list[bool]) -> None:
    """Splits all beams in-place, by adding two new beams left and right and removing the original beam."""
    for i in range(len(beams)):
        if to_split[i]:
            beams[i] = False
            if i - 1 >= 0:
                beams[i - 1] = True
            if i + 1 < len(beams):
                beams[i + 1] = True


if __name__ == "__main__":
    main()
