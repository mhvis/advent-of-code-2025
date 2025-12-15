import sys


def main():
    print("Part 1")
    print(remove_accessible(parse_file(sys.argv[1])))
    print("Part 2")
    print(remove_until_settled(parse_file(sys.argv[1])))


def remove_until_settled(grid: list[list[bool]]) -> int:
    total = 0
    while True:
        removed = remove_accessible(grid)
        if removed == 0:
            break
        total += removed
    return total


def remove_accessible(grid: list[list[bool]], max_neighbors: int = 3) -> int:
    """
    Returns the number of accessible rolls and modifies the grid.

    Args:
        grid (list[list[bool]]): The grid is modified in-place. Accessed rolls are set to False.

    Returns:
        The total number of rolls accessed.
    """
    accessible = []
    for i in range(len(grid)):
        for j in range(len(grid[0])):
            if grid[i][j]:
                neighbor_count = sum(
                    (
                        grid[i - 1][j - 1] if i - 1 >= 0 and j - 1 >= 0 else False,
                        grid[i - 1][j] if i - 1 >= 0 else False,
                        grid[i - 1][j + 1]
                        if i - 1 >= 0 and j + 1 < len(grid[0])
                        else False,
                        grid[i][j - 1] if j - 1 >= 0 else False,
                        grid[i][j + 1] if j + 1 < len(grid[0]) else False,
                        grid[i + 1][j - 1]
                        if i + 1 < len(grid) and j - 1 >= 0
                        else False,
                        grid[i + 1][j] if i + 1 < len(grid[0]) else False,
                        grid[i + 1][j + 1]
                        if i + 1 < len(grid) and j + 1 < len(grid[0])
                        else False,
                    )
                )
                if neighbor_count <= max_neighbors:
                    accessible.append((i, j))

    # Remove the accessible rolls
    for i, j in accessible:
        grid[i][j] = False

    return len(accessible)


def parse_file(filename: str) -> list[list[bool]]:
    with open(filename) as file:
        return parse_input(file.readlines())


def parse_input(lines: list[str]) -> list[list[bool]]:
    result = []
    for line in lines:
        line = line.strip()
        if line == "":
            continue
        result.append([True if c == "@" else False for c in line])
    return result


if __name__ == "__main__":
    main()
