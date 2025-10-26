package org.daa3.mst.algorithms;

import org.daa3.mst.model.*;
import org.daa3.mst.util.Pair;

import java.util.*;

public class PrimMST {
    public static MSTResult compute(Graph g) {
        MSTResult res = new MSTResult();
        long ops = 0;

        Map<String, List<Edge>> adj = new HashMap<>();
        for (String v : g.nodes) adj.put(v, new ArrayList<>());
        for (Edge e : g.edges) {
            adj.get(e.from).add(e);
            Edge rev = new Edge(e.to, e.from, e.weight);
            adj.get(e.to).add(rev);
        }

        Map<String, Integer> key = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> inMst = new HashSet<>();

        for (String v : g.nodes) key.put(v, Integer.MAX_VALUE);

        String start = g.nodes.get(0);
        key.put(start, 0);

        PriorityQueue<Pair<String,Integer>> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.value));
        pq.add(new Pair<>(start, 0));

        long t0 = System.nanoTime();
        while (!pq.isEmpty() && inMst.size() < g.nodes.size()) {
            Pair<String,Integer> cur = pq.poll();
            String node = cur.key;
            if (inMst.contains(node)) continue;
            inMst.add(node);
            if (parent.containsKey(node)) {

                Edge chosen = new Edge(parent.get(node), node, key.get(node));
                res.mstEdges.add(chosen);
                res.totalCost += chosen.weight;
            }

            for (Edge e : adj.get(node)) {
                String neigh = e.to;
                ops++;
                if (!inMst.contains(neigh) && e.weight < key.get(neigh)) {
                    key.put(neigh, e.weight);
                    parent.put(neigh, node);
                    pq.add(new Pair<>(neigh, e.weight));
                    ops++;
                }
            }
        }
        long t1 = System.nanoTime();

        res.operationsCount = ops;
        res.executionTimeMs = (t1 - t0) / 1_000_000.0;
        return res;
    }
}
