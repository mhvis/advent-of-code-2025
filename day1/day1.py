def main():
    print("Part 1:")
    print(f"Example: {solve_part1(parse_input('example.txt'))}")
    print(f"Input: {solve_part1(parse_input('input.txt'))}")

    print("Part 2 Maarten:")
    print(f"Example: {solve_part2_maarten(parse_input('example.txt'))}")
    print(f"Input: {solve_part2_maarten(parse_input('input.txt'))}")

    print("Part 2 Mathijs:")
    print(f"Example: {solve_part2_mathijs(parse_input('example.txt'))}")
    print(f"Input: {solve_part2_mathijs(parse_input('input.txt'))}")


def solve_part2_maarten(rotation_input: list[int]) -> int:
    """
    The gist: we plot our position on a linear line and see in which 100th
    'section'/'segment' we fall, before and after each rotation.

    Args:
        rotation_input: list with positive integers for rotations to the right and negative when to the left

    Returns:
        The password.
    """
    # The global position on a 'linear line' (without modulo)
    global_position = 50
    password = 0
    for i in rotation_input:
        # When moving left, we shift all segments by one to the right to account for turning around on a zero.
        direction_correction = -1 if i < 0 else 0

        # Determine the section before the rotation
        rotation_before = (global_position + direction_correction) // 100
        # Do the rotation
        global_position += i
        # Determine the section after the rotation
        rotation_after = (global_position + direction_correction) // 100

        # The distance moved in sections is the number of times we passed 0
        password += abs(rotation_after - rotation_before)

    return password


def solve_part2_mathijs(input) -> int:
    """
    Mostly doctored out by Mathijs.

    Args:
        input: list of integers with 'R10' as 10 and 'L5' as -5
    """
    total = 50
    password = 0
    for i in input:
        password += abs(i) // 100
        remainder = abs(i) % 100
        if remainder == 0:
            pass
        elif i < 0:
            new_total = total - remainder
            if (new_total % 100) > (total % 100) and total % 100 != 0:
                password += 1
            if new_total % 100 == 0:
                password += 1
        elif i > 0:
            new_total = total + remainder
            if (new_total % 100) < (total % 100):
                password += 1

        total += i

    return password


def solve_part1(input) -> int:
    total = 50
    password = 0
    for i in input:
        total += i
        if total % 100 == 0:
            password += 1
    return password


def parse_input(filename: str) -> list[int]:
    result = []
    with open(filename) as file:
        for line in file:
            if line.startswith("L"):
                result.append(-1 * int(line[1:]))
            else:
                result.append(int(line[1:]))
    return result


if __name__ == "__main__":
    main()
