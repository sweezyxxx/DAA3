package org.daa3.mst;

import org.daa3.mst.algorithms.KruskalMST;
import org.daa3.mst.algorithms.PrimMST;
import org.daa3.mst.model.Edge;
import org.daa3.mst.model.Graph;
import org.daa3.mst.model.MSTResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class MstTests {

    private Graph createTestGraph() {
        Graph g = new Graph();
        g.id = 1;
        g.nodes = Arrays.asList("A", "B", "C", "D");
        g.edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("A", "C", 4),
                new Edge("B", "C", 2),
                new Edge("B", "D", 5),
                new Edge("C", "D", 3)
        );
        return g;
    }

    @Test
    public void testMSTCostIsCorrect() {
        Graph g = createTestGraph();

        MSTResult prim = PrimMST.compute(g);
        MSTResult kruskal = KruskalMST.compute(g);

        Assertions.assertEquals(6, prim.totalCost, "Prim should produce correct MST cost");
        Assertions.assertEquals(6, kruskal.totalCost, "Kruskal should produce correct MST cost");
    }

    @Test
    public void testMSTHasCorrectNumberOfEdges() {
        Graph g = createTestGraph();

        MSTResult prim = PrimMST.compute(g);
        MSTResult kruskal = KruskalMST.compute(g);

        int expectedEdges = g.nodes.size() - 1;
        Assertions.assertEquals(expectedEdges, prim.mstEdges.size(), "Prim should produce V-1 edges");
        Assertions.assertEquals(expectedEdges, kruskal.mstEdges.size(), "Kruskal should produce V-1 edges");
    }

    @Test
    public void testPrimAndKruskalCostsAreEqual() {
        Graph g = createTestGraph();

        MSTResult prim = PrimMST.compute(g);
        MSTResult kruskal = KruskalMST.compute(g);

        Assertions.assertEquals(prim.totalCost, kruskal.totalCost, "Prim and Kruskal should produce same total MST cost");
    }
}
