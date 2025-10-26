package org.daa3.mst;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.daa3.mst.model.*;
import org.daa3.mst.algorithms.KruskalMST;
import org.daa3.mst.algorithms.PrimMST;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            String inputPath = "input.json"; // файл в корне проекта
            ObjectMapper mapper = new ObjectMapper();
            InputData input = mapper.readValue(new File(inputPath), InputData.class);

            Map<String, Object> output = new LinkedHashMap<>();
            List<Object> results = new ArrayList<>();

            for (Graph g : input.graphs) {
                Map<String,Object> rec = new LinkedHashMap<>();
                rec.put("graph_id", g.id);
                rec.put("vertices", g.nodes.size());
                rec.put("edges", g.edges.size());

                MSTResult prim = PrimMST.compute(g);
                MSTResult kruskal = KruskalMST.compute(g);

                Map<String,Object> pmap = new LinkedHashMap<>();
                pmap.put("mst_edges", prim.mstEdges);
                pmap.put("total_cost", prim.totalCost);
                pmap.put("operations_count", prim.operationsCount);
                pmap.put("execution_time_ms", prim.executionTimeMs);

                Map<String,Object> kmap = new LinkedHashMap<>();
                kmap.put("mst_edges", kruskal.mstEdges);
                kmap.put("total_cost", kruskal.totalCost);
                kmap.put("operations_count", kruskal.operationsCount);
                kmap.put("execution_time_ms", kruskal.executionTimeMs);

                rec.put("prim", pmap);
                rec.put("kruskal", kmap);

                results.add(rec);

                System.out.printf("Graph %d done: V=%d E=%d PrimCost=%d KruskalCost=%d%n",
                        g.id, g.nodes.size(), g.edges.size(), prim.totalCost, kruskal.totalCost);
            }

            output.put("results", results);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("output.json"), output);
            System.out.println("Wrote output.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
