def main():
    banks_example = parse_input("example.txt")
    banks_input = parse_input("input.txt")

    print("Part 1")
    print("Example: " + str(part1(banks_example)))
    print("Input: " + str(part1(banks_input)))
    print("Part 1")
    print("Example: " + str(part2(banks_example)))
    print("Input: " + str(part2(banks_input)))


def part1(banks: list[str]) -> int:
    total_joltage = 0
    for bank in banks:
        total_joltage += largest_joltage(bank, 2)
    return total_joltage


def part2(banks: list[str]) -> int:
    total_joltage = 0
    for bank in banks:
        total_joltage += largest_joltage(bank, 12)
    return total_joltage


def largest_joltage(bank: str, n: int) -> int:
    """
    Args:
        bank (str): The bank, a string of joltage digits
        n (int): The length of the output joltage in digits
    """
    selected = ""
    remaining_index = 0
    for i in range(0, n):
        search = bank[remaining_index : len(bank) - n + 1 + i]
        battery = max(search)
        remaining_index += search.find(battery) + 1
        selected += battery
    return int(selected)


def parse_input(filename: str) -> list[str]:
    result = []
    with open(filename) as f:
        for line in f:
            if line.strip() != "":
                result.append(line.strip())
    return result


if __name__ == "__main__":
    main()
