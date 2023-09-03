package com.soliman;

import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public interface Logger<Node> {
    void log(Set<Node> open, Set<Node> ND, Set<Node> closed, Map<Node, Set<Path<Node>>> solutionCosts,
            Map<Node, Set<Path<Node>>> label, DefaultDirectedWeightedGraph<Node, LabeledEdge<Costs>> graph, int k, Node prescelto, Set<Node> endNodes, Map<Node, Set<Path<Node>>> paths);

    static <Node> Logger<Node> noLogger() {
        return (open, ND, closed, solutionCosts, label, graph, k, prescelto, endNodes, paths) -> {};
    }
}