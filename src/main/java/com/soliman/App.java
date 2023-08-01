package com.soliman;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class App 
{
    public static void main(String[] args) {

        var graph = new DefaultDirectedWeightedGraph<String, DoubleCost>(null, null);

        graph.addVertex("s");
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");

        graph.addEdge("s", "1", new DoubleCost(1, 2));

        List<String> solutionPath = search(startString);

        for (String String : solutionPath) {
            
        }
    }
}
