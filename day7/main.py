import sys


def main():
    filename = sys.argv[1]
    with open(filename) as f:
        print(solve(f.readlines()))


def solve(lines: list[str]) -> tuple[int, int]:
    """
    Solves part 1 and 2 given a list of lines.

    Returns:
        A tuple with (number of splits (part 1), number of timelines (part 2)).
    """
    initial_state, splitter_rows = parse_input(lines)

    beam_state = [1 if s else 0 for s in initial_state]
    split_count = 0

    for splitters in splitter_rows:
        split_count += split(beam_state, splitters)

    return split_count, sum(beam_state)


def parse_input(lines: list[str]) -> tuple[list[bool], list[list[bool]]]:
    """Returns a tuple with the initial state, and rows of splitters."""
    initial_state = None  # type: list[bool]|None
    splitter_rows = []  # type: list[list[bool]]

    for line in lines:
        line = line.strip()
        if line == "":
            continue

        if initial_state is None:
            initial_state = [True if c == "S" else False for c in line]
        else:
            splitter_rows.append([True if c == "^" else False for c in line])

    return initial_state, splitter_rows


def split(beam_state: list[int], splitters: list[bool]) -> int:
    """
    Splits all beams, given a list of splitters.

    The current beam state is modified in-place!

    Args:
        beam_state: The current beam state. If the value is 0, there is no beam at that spot. Else,
            the integer denotes the number of overlapping beams (timelines).
        splitters: When True, there is a splitter at that spot.

    Returns:
        The number of splits performed.
    """
    split_count = 0
    for i in range(len(beam_state)):
        if splitters[i]:
            # Track the number of splits
            if beam_state[i] > 0:
                split_count += 1

            # Add beams to the left and right, and copy the number of timelines
            if i - 1 >= 0:
                beam_state[i - 1] += beam_state[i]
            if i + 1 < len(beam_state):
                beam_state[i + 1] += beam_state[i]
            # Remove the original beam
            beam_state[i] = 0

    return split_count


if __name__ == "__main__":
    main()
