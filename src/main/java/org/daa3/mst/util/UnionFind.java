package org.daa3.mst.util;

public class UnionFind {
    private final int[] parent;
    private final int[] rank;
    public long ops = 0; // счётчик операций для внешнего подсчёта (можно читать)

    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i]=i;
    }

    public int find(int x) {
        ops++;
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    public boolean union(int x, int y) {
        ops++;
        int rx = find(x);
        int ry = find(y);
        if (rx == ry) return false;
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[ry] < rank[rx]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
        return true;
    }
}
