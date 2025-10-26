# MST Algorithms Comparison (Prim vs Kruskal)

**Project:** MST Performance Evaluation  
**Student:** **Almukhamedov Temirlan**

This project evaluates and compares two classic Minimum Spanning Tree (MST) algorithms: **Prim's Algorithm** and **Kruskal's Algorithm**.  
The evaluation is performed on multiple datasets with different graph sizes to measure and compare:

Total MST cost  
Execution time (average, ms)  
Number of operations (average)

All datasets are stored in JSON format and processed automatically.

---

##  Project Structure

daa3/
└── src/main/java/org/daa3/mst/
├── Main.java # Main runner (auto execution + summary generation)
├── algorithms/ # Contains PrimMST and KruskalMST implementations
├── model/ # Graph, InputData, EdgeData, MSTResult classes
└── input.json # Main dataset
└── input_data/ # Additional datasets (small, medium, large)
└── results/ # Auto-generated output and performance data
├── output_.json
├── performance_.json
├── summary.txt # Final readable summary
└── report.md # Analytical report (B1-B2 English)

---

##  How to Run

1. Open the project in **IntelliJ IDEA**
2. Make sure Maven dependencies are loaded
3. Open `Main.java`
4. Click **Run ▶**

 After execution, results will be generated automatically.

---

##  Generated Results (in `/results/`)

| File | Description |
|------|-------------|
| `output_main.json` | One-time MST result for each graph (Prim + Kruskal) |
| `performance_main.json` | Performance statistics (repeated average runs) |
| `summary.txt` | **Readable final comparison in key/value format**  |
| Same files exist for `small`, `medium`, and `large` datasets |

---

##  Summary Example (from summary.txt)

=== Dataset: small ===
Graph ID | 1
Vertices | 4
Edges | 5
Prim Cost | 11
Kruskal Cost | 11
Avg Time P | 0.013 ms
Avg Time K | 0.003 ms
Ops P | 15
Ops K | 23

---

##  Full Report

The full analytical report is available in `report.md`.

It includes:
✔ Algorithm explanation  
✔ Dataset overview  
✔ Full performance tables (Markdown format)  
✔ Theory vs practice comparison  
✔ Final conclusions

---

##  Technologies Used

| Tool | Purpose |
|------|---------|
| Java | Language |
| Jackson | JSON parsing |
| Maven | Dependency management |
| IntelliJ IDEA | Development environment |

---

##  Conclusion

 This project helps understand performance differences between Prim’s and Kruskal’s MST algorithms using real test cases and dynamic results.

 For final evaluation and discussion, refer to `report.md`.

---
