package com.soliman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class App 
{
    public static void main(String[] args){
        
        DefaultDirectedWeightedGraph<String, LabeledEdge<DoubleCost>> graph = new DefaultDirectedWeightedGraph<>(LabeledEdge.class);

        String startNode = "";
        Set<String> endNodes = new HashSet<>();
        boolean directional = true;

        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            startNode = reader.readLine().split(":")[1].trim();
            endNodes = new HashSet<>(Arrays.asList(reader.readLine().split(":")[1].trim().split(", ")));
            directional = reader.readLine().split(":")[1].trim().equalsIgnoreCase("S");

            String line;
            while ((line = reader.readLine()) != null) {
                if(line.isBlank()) continue;
                String[] parts = line.split(" - ");
                String source = parts[0];
                String[] costs = parts[1].replace("(", "").replace(")", "").split(" ");
                double cost1 = Double.parseDouble(costs[0]);
                double cost2 = Double.parseDouble(costs[1]);
                String target = parts[2];
                graph.addVertex(source);
                graph.addVertex(target);
                graph.addEdge(source, target, new LabeledEdge<>(new DoubleCost(cost1, cost2)));
                if(!directional){
                    graph.addEdge(target, source, new LabeledEdge<>(new DoubleCost(cost1, cost2)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        var solutionPath = Moa.search(graph, startNode, endNodes, new DoubleCost(0, 0), App::heuristicFunction);
        System.out.println();
        int i = 1;
        for (var endNode : solutionPath.keySet()) {
            for (var path : solutionPath.get(endNode)) {
                System.out.println(path.toString("P" + i + "*"));
                i++;
            }
        }
        System.out.println();
    }

    public static String heuristicFunction(Set<String> ND, Map<String, Set<Path<String, DoubleCost>>> label) {
        String minNode = null;
        double minCost = Double.POSITIVE_INFINITY;
        for (String node : ND) {
            double actualMin = Integer.MAX_VALUE;
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
