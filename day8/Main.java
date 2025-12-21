import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Point> points = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] comp = scanner.nextLine().split(",");
            points.add(new Point(Integer.parseInt(comp[0]), Integer.parseInt(comp[1]), Integer.parseInt(comp[2])));
        }

        ArrayList<Edge> edges = new ArrayList<>(points.size() * points.size());
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                edges.add(new Edge(i, j, points.get(i).distance(points.get(j))));
            }
        }

        Collections.sort(edges);

        UnionFind unionFind = new UnionFind(points.size());

        int conns = 1000;
        for (int i = 0; i < conns; i++) {
            unionFind.union(edges.get(i).i, edges.get(i).j);
        }

        int[] setSizes = new int[points.size()];
        for (int i = 0; i < setSizes.length; i++) {
            setSizes[unionFind.find(i)] = unionFind.getSetSize(i);
        }
        Arrays.sort(setSizes);
        System.out.println(setSizes[setSizes.length - 1] * setSizes[setSizes.length - 2] * setSizes[setSizes.length - 3]);

        // Continue until fully connected
        int i;
        for (i = conns; unionFind.getNumSets() > 1; i++) {
            unionFind.union(edges.get(i).i, edges.get(i).j);
        }
        Edge edge = edges.get(i - 1);
        System.out.println(points.get(edge.i).x * points.get(edge.j).x);
    }
}


class Edge implements Comparable<Edge> {
    public int i;
    public int j;
    public int weight;

    public Edge(int i, int j, int weight) {
        this.i = i;
        this.j = j;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge o) {
        return this.weight - o.weight;
    }
}


class Point {
    public int x;
    public int y;
    public int z;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int distance(Point p) {
        return (int) (Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2) + Math.pow(this.z - p.z, 2));
    }
}


class UnionFind {
    private final int[] parent;
    private final int[] rank;
    private final int[] setSize;
    private int numSets;

    public UnionFind(int size) {
        parent = new int[size];
        rank = new int[size];
        setSize = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
            setSize[i] = 1;
        }
        numSets = size;
    }

    public int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]);  // Path compression
        }
        return parent[i];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return;
        }

        if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
            setSize[rootX] += setSize[rootY];
        } else {
            parent[rootX] = rootY;
            setSize[rootY] += setSize[rootX];
            if (rank[rootX] == rank[rootY]) {
                rank[rootY] += 1;
            }
        }

        numSets--;
    }

    public int getSetSize(int i) {
        return setSize[i];
    }

    public int getNumSets() {
        return numSets;
    }
}
