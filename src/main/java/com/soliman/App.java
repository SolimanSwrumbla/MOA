package com.soliman;

import java.util.Map;
import java.util.Set;

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
        graph.addVertex("6");
        graph.addVertex("7");
        graph.addVertex("8");
        graph.addVertex("9");
        graph.addVertex("y1");
        graph.addVertex("y2");
        graph.addVertex("y3");

        graph.addEdge("s", "1", new LabeledEdge<>(new DoubleCost(1, 2)));
        graph.addEdge("s", "2", new LabeledEdge<>(new DoubleCost(3, 1)));
        graph.addEdge("s", "3", new LabeledEdge<>(new DoubleCost(1, 3)));
        graph.addEdge("1", "4", new LabeledEdge<>(new DoubleCost(2, 1)));
        graph.addEdge("1", "5", new LabeledEdge<>(new DoubleCost(1, 2)));
        graph.addEdge("2", "5", new LabeledEdge<>(new DoubleCost(2, 1)));
        graph.addEdge("2", "6", new LabeledEdge<>(new DoubleCost(2, 1)));
        graph.addEdge("3", "6", new LabeledEdge<>(new DoubleCost(1, 2)));
        graph.addEdge("4", "7", new LabeledEdge<>(new DoubleCost(5, 7)));
        graph.addEdge("5", "7", new LabeledEdge<>(new DoubleCost(1, 3)));
        graph.addEdge("5", "8", new LabeledEdge<>(new DoubleCost(1, 1)));
        graph.addEdge("6", "8", new LabeledEdge<>(new DoubleCost(5, 2)));
        graph.addEdge("6", "9", new LabeledEdge<>(new DoubleCost(2, 4)));
        graph.addEdge("7", "y1", new LabeledEdge<>(new DoubleCost(1, 4)));
        graph.addEdge("7", "y2", new LabeledEdge<>(new DoubleCost(8, 7)));
        graph.addEdge("8", "y2", new LabeledEdge<>(new DoubleCost(5, 7)));
        graph.addEdge("8", "y3", new LabeledEdge<>(new DoubleCost(3, 2)));
        graph.addEdge("9", "y3", new LabeledEdge<>(new DoubleCost(1, 2)));

        var solutionPath = Moa.search(graph, "s", Set.of("y1","y2","y3"), new DoubleCost(0, 0), App::heuristicFunction);
        for (var endNode : solutionPath.keySet()) {
            System.out.println(endNode);
            for (var path : solutionPath.get(endNode)) {
                System.out.println(path);
            }
            System.out.println();
            System.out.println();
        }
    }

    public static String heuristicFunction(Set<String> ND, Map<String, Set<Path<String, DoubleCost>>> label) {
        String minNode = null;
        double minCost = Double.POSITIVE_INFINITY;
        for (String node : ND) {
            int actualMin = Integer.MAX_VALUE;
            for (var path : label.get(node)) {
                DoubleCost cost = path.cost();
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
