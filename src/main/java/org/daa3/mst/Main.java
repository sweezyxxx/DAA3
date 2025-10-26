package org.daa3.mst;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.daa3.mst.algorithms.KruskalMST;
import org.daa3.mst.algorithms.PrimMST;
import org.daa3.mst.model.Graph;
import org.daa3.mst.model.InputData;
import org.daa3.mst.model.MSTResult;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
                boolean ok = resultsDir.mkdir();
                if (!ok) {
                    System.out.println("Warning: could not create results directory");
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> globalPerformance = new ArrayList<>();
            StringBuilder summaryBuilder = new StringBuilder();

            for (String[] set : inputSets) {
                String inputPath = set[0];
                String tag = set[1];

                File f = new File(inputPath);
                if (!f.exists()) {
                    System.out.printf("Skipping '%s' (file not found)%n", inputPath);
                    continue;
                }

                InputData input = mapper.readValue(f, InputData.class);
                List<Object> detailedResults = new ArrayList<>();
                List<Object> performanceResults = new ArrayList<>();

                summaryBuilder.append("=== Dataset: ").append(tag).append(" ===\n");

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

                    double primTotalTime = 0;
                    double kruskalTotalTime = 0;
                    long primTotalOps = 0;
                    long kruskalTotalOps = 0;

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
                    perfEntry.put("dataset", tag);
                    perfEntry.put("graph_id", g.id);
                    perfEntry.put("vertices", g.nodes.size());
                    perfEntry.put("edges", g.edges.size());
                    perfEntry.put("prim_cost", primOnce.totalCost);
                    perfEntry.put("kruskal_cost", kruskalOnce.totalCost);
                    perfEntry.put("prim_avg_time_ms", primAvgTime);
                    perfEntry.put("kruskal_avg_time_ms", kruskalAvgTime);
                    perfEntry.put("prim_avg_operations", primAvgOps);
                    perfEntry.put("kruskal_avg_operations", kruskalAvgOps);

                    performanceResults.add(perfEntry);
                    globalPerformance.add(perfEntry);

                    summaryBuilder.append(String.format("Graph ID     | %s%n", g.id));
                    summaryBuilder.append(String.format("Vertices     | %d%n", g.nodes.size()));
                    summaryBuilder.append(String.format("Edges        | %d%n", g.edges.size()));
                    summaryBuilder.append(String.format("Prim Cost    | %d%n", primOnce.totalCost));
                    summaryBuilder.append(String.format("Kruskal Cost | %d%n", kruskalOnce.totalCost));
                    summaryBuilder.append(String.format("Avg Time P   | %s ms%n", primAvgTime));
                    summaryBuilder.append(String.format("Avg Time K   | %s ms%n", kruskalAvgTime));
                    summaryBuilder.append(String.format("Ops P        | %d%n", primAvgOps));
                    summaryBuilder.append(String.format("Ops K        | %d%n", kruskalAvgOps));
                    summaryBuilder.append("------------------------\n");
                }

                Map<String, Object> output = new LinkedHashMap<>();
                output.put("results", detailedResults);
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(new File("results/output_" + tag + ".json"), output);

                Map<String, Object> perf = new LinkedHashMap<>();
                perf.put("performance", performanceResults);
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(new File("results/performance_" + tag + ".json"), perf);

                summaryBuilder.append("\n");
            }

            File summaryFile = new File("results/summary.txt");
            try (PrintWriter pw = new PrintWriter(new FileWriter(summaryFile))) {
                pw.print(summaryBuilder.toString());
            }

            System.out.println("\nSummary (results/summary.txt) created. Quick view:");
            System.out.printf("%-10s %-10s %-6s %-6s %-12s %-12s %-10s %-10s%n",
                    "Dataset", "GraphID", "V", "E", "PrimCost", "KruskCost", "PrimTime", "KruskTime");
            System.out.println("---------------------------------------------------------------------------------");
            for (Map<String, Object> row : globalPerformance) {
                System.out.printf("%-10s %-10s %-6s %-6s %-12s %-12s %-10s %-10s%n",
                        row.get("dataset"),
                        row.get("graph_id"),
                        row.get("vertices"),
                        row.get("edges"),
                        row.get("prim_cost"),
                        row.get("kruskal_cost"),
                        row.get("prim_avg_time_ms"),
                        row.get("kruskal_avg_time_ms"));
            }

            System.out.println("\nAll results saved to 'results/' directory (output_*, performance_*, summary.txt).");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static double round(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}
