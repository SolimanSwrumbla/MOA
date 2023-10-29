package com.soliman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticNode implements Node<String> {
    private Map<String, List<WeightedEdge>> graph; // Rappresentazione del grafo con nodi collegati a liste di archi pesati
    private String startNode;
    private int costLength;

    private StaticNode(Map<String, List<WeightedEdge>> graph, String startNode, int costLength) {
        this.graph = graph;
        this.startNode = startNode;
        this.costLength = costLength;
    }

    // Creazione di un'istanza di StaticNode da un file di input
    public static StaticNode fromFile(String input, String startNode, boolean directional) {
        Map<String, List<WeightedEdge>> graph = new HashMap<>();
        int costLength = -1;

        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank())
                    continue;
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length != 3) {
                    System.err.println("\nErrore: Formato della linea invalido : " + line);
                    continue;
                }
                String source = parts[0].trim().toUpperCase();
                if (source.matches(".*[,].*")) {
                    System.err.println("\nErrore: Formato nodo invalido : " + line);
                    continue;
                }
                String target = parts[2].trim().toUpperCase();
                if (target.matches(".*[,].*")) {
                    System.err.println("\nErrore: Formato nodo invalido : " + line);
                    continue;
                }
                if (source.equals(target))
                    continue;
                    
                String[] costs = parts[1].replace(",", ".").replace("(", "")
                        .replace(")", "").trim()
                        .split("\\s+");
                if (costLength == -1) {
                    costLength = costs.length;
                } else if (costs.length != costLength) {
                    System.err.println("\nErrore: Formato del costo invalido : " + line + "\n");
                    return null;
                }

                double[] convertedCosts = new double[costs.length];
                boolean validCost = true;
                for (int i = 0; i < costs.length; i++) {
                    try {
                        convertedCosts[i] = Double.parseDouble(costs[i]);
                    } catch (NumberFormatException e) {
                        validCost = false;
                        break;
                    }
                }
                if (!validCost) {
                    System.err.println("\nErrore: Formato del costo invalido : " + line);
                    continue;
                }

                graph.putIfAbsent(source, new ArrayList<>());
                graph.get(source).add(new WeightedEdge(target, convertedCosts));
                if (!directional) {
                    graph.putIfAbsent(target, new ArrayList<>());
                    graph.get(target).add(new WeightedEdge(source, convertedCosts));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!graph.containsKey(startNode)) {
            System.err.println("\nErrore: Il nodo iniziale non e' presente nel grafo.\n");
            return null;
        }

        return new StaticNode(graph, startNode, costLength);
    }

    // Restituisce i successori del nodo corrente
    @Override
    public Iterable<Child<String>> successors() {
        List<Child<String>> successors = new ArrayList<>();
        List<WeightedEdge> outgoingEdges = graph.get(startNode);

        if (outgoingEdges != null) {
            for (WeightedEdge edge : outgoingEdges) {
                String target = edge.getTarget();
                double[] costs = edge.getCosts();
                successors.add(new Child<>(new StaticNode(graph, target, costLength), new Costs(costs)));
            }
        }

        return successors;
    }

    @Override
    public String value() {
        return startNode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StaticNode node))
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

    @Override
    public int costLength() {
        return costLength;
    }
}

class WeightedEdge {
    private String target; // Nodo di destinazione
    private double[] costs; // Costi dell'arco

    public WeightedEdge(String target, double[] costs) {
        this.target = target;
        this.costs = costs;
    }

    public String getTarget() {
        return target;
    }

    public double[] getCosts() {
        return costs;
    }
}