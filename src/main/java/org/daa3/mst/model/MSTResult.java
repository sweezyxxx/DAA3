package org.daa3.mst.model;

import java.util.List;
import java.util.ArrayList;

public class MSTResult {
    public List<Edge> mstEdges = new ArrayList<>();
    public int totalCost = 0;
    public long operationsCount = 0;
    public double executionTimeMs = 0.0;
}
