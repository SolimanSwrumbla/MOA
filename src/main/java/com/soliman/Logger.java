package com.soliman;

import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public interface Logger<Node> {
    void log(int k, Set<Node> open, Map<Node, Set<Path<Node>>> paths, DefaultDirectedWeightedGraph<Node, LabeledEdge<Costs>> graph, Set<Node> endNodes, Node prescelto, Set<Node> ND, Set<Node> closed, Map<Node, Set<Path<Node>>> solutionCosts);

    static <Node> Logger<Node> noLogger() {
        return (k, open, paths, graph, endNodes, prescelto, ND, closed, solutionCosts) -> {};
    }
}