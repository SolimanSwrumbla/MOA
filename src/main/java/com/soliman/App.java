package com.soliman;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class App 
{
    public static void main(String[] args) {
        var graph = new DefaultDirectedWeightedGraph<String, LabeledEdge<DoubleCost>>(null, null);

        graph.addVertex("s");
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("y");

        graph.addEdge("s", "1", new LabeledEdge<>(new DoubleCost(1, 3)));
        graph.addEdge("s", "2", new LabeledEdge<>(new DoubleCost(2, 1)));
        graph.addEdge("s", "3", new LabeledEdge<>(new DoubleCost(2, 4)));
        graph.addEdge("1", "4", new LabeledEdge<>(new DoubleCost(1, 1)));
        graph.addEdge("2", "1", new LabeledEdge<>(new DoubleCost(2, 1)));
        graph.addEdge("3", "5", new LabeledEdge<>(new DoubleCost(1, 1)));
        graph.addEdge("4", "y", new LabeledEdge<>(new DoubleCost(4, 6)));
        graph.addEdge("5", "y", new LabeledEdge<>(new DoubleCost(3, 5)));

        Map<String,Set<DoubleCost>> solutionPath = Moa.search(graph, "s", Set.of("y"), new DoubleCost(0, 0), App::heuristicFunction);

        System.out.println(solutionPath);
    }

    public static String heuristicFunction(Set<String> ND, Map<String, Set<DoubleCost>> label) {
        String minNode = null;
        double minCost = Double.POSITIVE_INFINITY;
        for (String node : ND) {
            int actualMin = Integer.MAX_VALUE;
            for (DoubleCost cost : label.get(node)) {
                if (cost.sum() < actualMin) actualMin = cost.sum();
            }
            if (minNode == null || actualMin < minCost) {
                minCost = actualMin;
                minNode = node;
            }
        }
        return minNode;
    }
}
