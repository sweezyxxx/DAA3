package org.daa3.mst;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.daa3.mst.algorithms.KruskalMST;
import org.daa3.mst.algorithms.PrimMST;
import org.daa3.mst.model.Graph;
import org.daa3.mst.model.InputData;
import org.daa3.mst.model.MSTResult;

import java.io.File;
import java.util.*;

public class Main {
    private static final int RUNS = 5;

    public static void main(String[] args) {
        try {
            String[][] inputSets = {
                    {"input.json", "main"},
                    {"input_data/small_input.json", "small"},
                    {"input_data/medium_input.json", "medium"},
                    {"input_data/large_input.json", "large"}
            };

            File resultsDir = new File("results");
            if (!resultsDir.exists()) {
                resultsDir.mkdir();
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Object> globalPerformanceTable = new ArrayList<>();

            for (String[] set : inputSets) {
                String inputPath = set[0];
                String tag = set[1];

                InputData input = mapper.readValue(new File(inputPath), InputData.class);
                List<Object> detailedResults = new ArrayList<>();
                List<Object> performanceResults = new ArrayList<>();

                for (Graph g : input.graphs) {
                    MSTResult primOnce = PrimMST.compute(g);
                    MSTResult kruskalOnce = KruskalMST.compute(g);

                    Map<String, Object> rec = new LinkedHashMap<>();
                    rec.put("graph_id", g.id);
                    rec.put("vertices", g.nodes.size());
                    rec.put("edges", g.edges.size());

                    Map<String, Object> pmap = new LinkedHashMap<>();
                    pmap.put("mst_edges", primOnce.mstEdges);
                    pmap.put("total_cost", primOnce.totalCost);
                    pmap.put("operations_count", primOnce.operationsCount);
                    pmap.put("execution_time_ms", round(primOnce.executionTimeMs));

                    Map<String, Object> kmap = new LinkedHashMap<>();
                    kmap.put("mst_edges", kruskalOnce.mstEdges);
                    kmap.put("total_cost", kruskalOnce.totalCost);
                    kmap.put("operations_count", kruskalOnce.operationsCount);
                    kmap.put("execution_time_ms", round(kruskalOnce.executionTimeMs));

                    rec.put("prim", pmap);
                    rec.put("kruskal", kmap);
                    detailedResults.add(rec);

                    double primTotalTime = 0, kruskalTotalTime = 0;
                    long primTotalOps = 0, kruskalTotalOps = 0;

                    for (int i = 0; i < RUNS; i++) {
                        MSTResult p = PrimMST.compute(g);
                        MSTResult k = KruskalMST.compute(g);

                        primTotalTime += p.executionTimeMs;
                        primTotalOps += p.operationsCount;

                        kruskalTotalTime += k.executionTimeMs;
                        kruskalTotalOps += k.operationsCount;
                    }

                    double primAvgTime = round(primTotalTime / RUNS);
                    double kruskalAvgTime = round(kruskalTotalTime / RUNS);
                    long primAvgOps = primTotalOps / RUNS;
                    long kruskalAvgOps = kruskalTotalOps / RUNS;

                    Map<String, Object> perfEntry = new LinkedHashMap<>();
                    perfEntry.put("graph_id", g.id);
                    perfEntry.put("tag", tag);
                    perfEntry.put("vertices", g.nodes.size());
                    perfEntry.put("edges", g.edges.size());
                    perfEntry.put("prim_avg_time_ms", primAvgTime);
                    perfEntry.put("prim_avg_operations", primAvgOps);
                    perfEntry.put("kruskal_avg_time_ms", kruskalAvgTime);
                    perfEntry.put("kruskal_avg_operations", kruskalAvgOps);
                    performanceResults.add(perfEntry);

                    globalPerformanceTable.add(perfEntry);
                }

                Map<String, Object> output = new LinkedHashMap<>();
                output.put("results", detailedResults);
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(new File("results/output_" + tag + ".json"), output);

                Map<String, Object> performanceReport = new LinkedHashMap<>();
                performanceReport.put("performance", performanceResults);
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(new File("results/performance_" + tag + ".json"), performanceReport);
            }

            System.out.printf("%-10s %-10s %-10s %-10s %-20s %-20s%n",
                    "Tag", "GraphID", "V", "E", "Prim Avg Time", "Kruskal Avg Time");
            System.out.println("----------------------------------------------------------------------------");

            for (Object obj : globalPerformanceTable) {
                Map<?, ?> row = (Map<?, ?>) obj;
                System.out.printf("%-10s %-10s %-10s %-10s %-20s %-20s%n",
                        row.get("tag"),
                        row.get("graph_id"),
                        row.get("vertices"),
                        row.get("edges"),
                        row.get("prim_avg_time_ms"),
                        row.get("kruskal_avg_time_ms"));
            }

            System.out.println("\n✅ Все результаты сохранены в папку 'results'.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double round(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}
