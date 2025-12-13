def main():
    part2_self_test()
    print("Part 1")
    print(f"Example: {part1(parse_file('example.txt'))}")
    print(f"Input: {part1(parse_file('input.txt'))}")
    print("Part 2")
    print(f"Example: {part2_naive(parse_file('example.txt'))}")
    print(f"Input: {part2_naive(parse_file('input.txt'))}")


def part1(ranges: list[tuple[int, int]]) -> int:
    """
    Idea: generate all possible candidates loosely inside the range, and then only
    keep those which are in the range.

    Returns:
        The sum of all invalid IDs.
    """
    id_sum = 0
    for start, end in ranges:
        base = get_first_half(start)
        candidate = duplicate_digits(base)

        while candidate <= end:
            if duplicate_digits(base) >= start:
                id_sum += duplicate_digits(base)
            base += 1
            candidate = duplicate_digits(base)

    return id_sum


def get_first_half(i: int) -> int:
    """Returns the first n/2 digits floored of the given integer with n digits."""
    if i <= 0:
        raise ValueError
    return 1 if i < 10 else int(str(i)[0 : len(str(i)) // 2])


def duplicate_digits(i: int) -> int:
    """Given a number 'n' returns 'nn'."""
    return int(str(i) + str(i))


def part2_naive(ranges: list[tuple[int, int]]) -> int:
    """Naive implementation with linear time complexity on the length of each range."""
    invalid_id_sum = 0
    for start, end in ranges:
        for i in range(start, end + 1):
            if part2_is_invalid(i):
                invalid_id_sum += i
    return invalid_id_sum


def part2_is_invalid(i: int) -> bool:
    digits = str(i)
    if len(digits) == 1:
        return False
    for repeat_length in range(1, (len(digits) // 2) + 1):
        if digits[0:repeat_length] * (len(digits) // repeat_length) == digits:
            return True
    return False


def part2_self_test():
    if part2_is_invalid(1):
        raise ValueError
    if part2_is_invalid(10):
        raise ValueError
    if not part2_is_invalid(11):
        raise ValueError
    if not part2_is_invalid(123123123):
        raise ValueError
    if part2_is_invalid(1231231231):
        raise ValueError
    if part2_is_invalid(123123124):
        raise ValueError


def parse_file(filename: str) -> list[tuple[int, int]]:
    """
    Returns:
        A list of two-element tuples with the first and last ID
    """
    with open(filename) as f:
        line = f.readline()
        ranges = line.split(",")
        return list((int(r.split("-")[0]), int(r.split("-")[1])) for r in ranges)


if __name__ == "__main__":
    main()
