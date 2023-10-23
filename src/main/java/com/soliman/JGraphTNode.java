package com.soliman;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class JGraphTNode implements Node<String> {
    private DefaultDirectedWeightedGraph<String, LabeledEdge<Costs>> graph;
    private String startNode;

    public JGraphTNode(DefaultDirectedWeightedGraph<String, LabeledEdge<Costs>> graph, String startNode) {
        this.graph = graph;
        this.startNode = startNode;
    }

    @Override
    public Iterable<Child<String>> successors() {
        return Graphs.successorListOf(graph, startNode).stream()
                .map(t -> new Child<>(new JGraphTNode(graph, t), graph.getEdge(startNode, t).label())).toList();
    }

    @Override
    public String value() {
        return startNode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JGraphTNode node))
            return false;
        return graph == node.graph && startNode.equals(node.startNode);
    }

    @Override
    public String toString() {
        return startNode;
    }

    @Override
    public int hashCode() {
        return startNode.hashCode();
    }
}
