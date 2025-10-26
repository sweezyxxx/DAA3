package org.daa3.mst.model;

public class Edge implements Comparable<Edge> {
    public String from;
    public String to;
    public int weight;

    public Edge() {}

    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge o) {
        return Integer.compare(this.weight, o.weight);
    }

    @Override
    public String toString() {
        return String.format("(%s-%s:%d)", from, to, weight);
    }
}
