package com.soliman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class App {
    public static void main(String[] args) {

        DefaultDirectedWeightedGraph<String, LabeledEdge<Costs>> graph = new DefaultDirectedWeightedGraph<>(LabeledEdge.class);

        String startNode = "";
        Set<String> endNodes = new HashSet<>();
        boolean directional = true;
        Integer costLength = null;

        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            startNode = reader.readLine().replace(":", ": ").split(":")[1].trim().toUpperCase();
            String endNodesString = reader.readLine().replace(":", ": ").split(":")[1].trim().toUpperCase();
            if (startNode.isEmpty() && endNodesString.isEmpty()) {
                System.err.println("\nErrore: Nessun nodo iniziale e finale.\n");
                return;
            }
            if (startNode.isEmpty()) {
                System.err.println("\nErrore: Nessun nodo iniziale.\n");
                return;
            }
            if (endNodesString.isEmpty()) {
                System.err.println("\nErrore: Nessun nodo finale.\n");
                return;
            }
            endNodes = new HashSet<>(Arrays.asList(endNodesString.split("\\s*,\\s*")));
            if (endNodes.contains(startNode)) {
                System.err.println("\nErrore: Il nodo iniziale non puo' essere un nodo finale.\n");
                return;
            }
        
            String direct = reader.readLine().replace(":", ": ").split(":")[1].trim();
            if (direct.isBlank()) {
                System.out.println("\nErrore: Opzione direzionale vuota. (Default S).\n");
            } else if (!direct.equalsIgnoreCase("S") && !direct.equalsIgnoreCase("N")) {
                System.err.println("\nErrore: Opzione direzionale non valida. Utilizzare 'S' o 'N'. (Default S).");
            } else {
                directional = direct.equalsIgnoreCase("S");
            }
            if (startNode.contains(",")) {
                System.err.println("\nErrore: Troppi nodi iniziali.\n");
                return;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split("\\s*-\\s*");
                if (parts.length != 3) {
                    System.err.println("\nErrore: Formato della linea invalido : " + line);
                    continue;
                }
                String source = parts[0].toUpperCase();
                if (source.matches(".*[,-].*")){
                    System.err.println("\nErrore: Formato nodo invalido : " + line);
                }
                String[] costs = parts[1].replace(",", ".").replaceAll("\\s+", " ").replace("(", "").replace(")", "").split(" ");
                if (costLength != null && costs.length != costLength) {
                    System.err.println("\nErrore: Formato del costo invalido : " + line + "\n");
                    return;
                }
                costLength = costs.length;
                try {
                    double[] convertedCosts = Arrays.stream(costs).mapToDouble(Double::parseDouble).toArray();
                    String target = parts[2].toUpperCase();
                    if (target.matches(".*[,-].*")){
                        System.err.println("\nErrore: Formato nodo invalido : " + line);
                    }
                    graph.addVertex(source);
                    graph.addVertex(target);
                    graph.addEdge(source, target, new LabeledEdge<>(new Costs(convertedCosts)));
                    if (!directional) {
                        graph.addEdge(target, source, new LabeledEdge<>(new Costs(convertedCosts)));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("\nErrore: Formato del costo invalido : " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!graph.containsVertex(startNode)) {
            System.err.println("\nErrore: Il nodo iniziale non e' presente nel grafo.\n");
            return;
        }

        var solutionPath = Moa.search(graph, startNode, endNodes, new Costs(new double[costLength]), App::heuristicFunction);
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

    public static String heuristicFunction(Set<String> ND, Map<String, Set<Path<String, Costs>>> label) {
        String minNode = null;
        double minCost = Double.POSITIVE_INFINITY;
        for (String node : ND) {
            double actualMin = Integer.MAX_VALUE;
            for (var path : label.get(node)) {
                Costs cost = path.cost();
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
