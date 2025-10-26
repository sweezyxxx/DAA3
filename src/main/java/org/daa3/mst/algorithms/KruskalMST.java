package org.daa3.mst.algorithms;

import org.daa3.mst.model.*;
import org.daa3.mst.util.UnionFind;

import java.util.*;

public class KruskalMST {
    public static MSTResult compute(Graph g) {
        MSTResult res = new MSTResult();
        long ops = 0;

        Map<String,Integer> idx = new HashMap<>();
        for (int i = 0; i < g.nodes.size(); i++) idx.put(g.nodes.get(i), i);

        List<Edge> all = new ArrayList<>(g.edges);
        Collections.sort(all);

        int n = g.nodes.size();
        UnionFind uf = new UnionFind(n);

        long t0 = System.nanoTime();
        for (Edge e : all) {
            int u = idx.get(e.from);
            int v = idx.get(e.to);

            ops++;
            if (uf.find(u) != uf.find(v)) {
                boolean merged = uf.union(u, v);
                ops++;
                if (merged) {
                    res.mstEdges.add(e);
                    res.totalCost += e.weight;
                }
            }
            if (res.mstEdges.size() == n - 1) break;
        }
        long t1 = System.nanoTime();

        res.operationsCount = ops + uf.ops;
        res.executionTimeMs = (t1 - t0) / 1_000_000.0;
        return res;
    }
}
