package com.soliman;

import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public interface HeuristicFunction<Node> {
    double apply(DefaultDirectedWeightedGraph<Node, LabeledEdge<Costs>> graph, Node node, Set<Node> endNodes, Map<Node, Set<Path<Node>>> paths);
}
