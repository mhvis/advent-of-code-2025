from __future__ import annotations

import sys
import time
from math import sqrt

from dataclasses import dataclass


@dataclass
class Point:
    x: int
    y: int
    z: int

    def distance(self, other: Point) -> float:
        return sqrt(
            (self.x - other.x) ** 2 + (self.y - other.y) ** 2 + (self.z - other.z) ** 2
        )


@dataclass
class Edge:
    a: int
    b: int
    weight: float


@dataclass
class UnionFind:
    """Union-Find Disjoint Set data structure."""

    def __init__(self, size: int):
        self.parent = [i for i in range(size)]
        self.rank = [0 for _ in range(size)]
        self.set_size = [1 for _ in range(size)]
        self.num_sets = size

    def find_set(self, i: int) -> int:
        if self.parent[i] == i:
            return i
        self.parent[i] = self.find_set(self.parent[i])
        return self.parent[i]

    def is_same_set(self, i: int, j: int) -> bool:
        return self.find_set(i) == self.find_set(j)

    def union_set(self, i: int, j: int) -> None:
        if not self.is_same_set(i, j):
            x = self.find_set(i)
            y = self.find_set(j)

            if self.rank[x] > self.rank[y]:
                self.parent[y] = x
                self.set_size[x] += self.set_size[y]
            else:
                self.parent[x] = y
                self.set_size[y] += self.set_size[x]
                if self.rank[x] == self.rank[y]:
                    self.rank[y] += 1

            self.num_sets -= 1

    def size_of_set(self, i: int) -> int:
        return self.set_size[self.find_set(i)]

    def num_of_sets(self) -> int:
        return self.num_sets


# Parse input
points = []  # type: list[Point]
for line in sys.stdin:
    line = line.strip()
    points.append(Point(*map(int, line.split(","))))

# Construct graph data structure using an edge list representation
edge_list = []  # type: list[Edge]
for i in range(len(points)):
    for j in range(i + 1, len(points)):
        edge_list.append(Edge(i, j, points[i].distance(points[j])))

# Kruskal's algorithm for Minimum Spanning Tree

# Sort edges by increasing weight (distance)
edge_list.sort(key=lambda e: e.weight)
union_find = UnionFind(len(points))

# Join the first 1000 closest points
connections = 1000
for i in range(connections):
    union_find.union_set(edge_list[i].a, edge_list[i].b)

# Extract all sets sizes and find the answer for part 1
set_sizes = [0 for _ in range(len(points))]
for i in range(len(points)):
    set_sizes[union_find.find_set(i)] = union_find.size_of_set(i)
set_sizes.sort(reverse=True)
print(set_sizes[0] * set_sizes[1] * set_sizes[2])

# Continue until there is only one set for part 2
i = connections
while union_find.num_of_sets() > 1:
    union_find.union_set(edge_list[i].a, edge_list[i].b)
    i += 1
i -= 1
print(points[edge_list[i].a].x * points[edge_list[i].b].x)
