package com.soliman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class JGraphTNode implements Node<String> {
    private DefaultDirectedWeightedGraph<String, LabeledEdge<Costs>> graph;
    private String startNode;
    private int costLength;

    public static JGraphTNode fromFile(String input, String startNode, boolean directional){
        // Creazione del grafo orientato pesato
        DefaultDirectedWeightedGraph<String, LabeledEdge<Costs>> graph = new DefaultDirectedWeightedGraph<>(null, null);

        Integer costLength = null;

        // Lettura del grafo da un file e creazione del grafo
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank())
                    continue;
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length != 3) {
                    System.out.println(parts.length);
                    System.err.println("\nErrore: Formato della linea invalido : " + line);
                    continue;
                }
                String source = parts[0].toUpperCase();
                if (source.matches(".*[,].*")) {
                    System.err.println("\nErrore: Formato nodo invalido : " + line);
                }
                String[] costs = parts[1].replace(",", ".").replaceAll("\\s+", " ").replace("(", "").replace(")", "")
                        .split(" ");
                if (costLength != null && costs.length != costLength) {
                    System.err.println("\nErrore: Formato del costo invalido : " + line + "\n");
                    return null;
                }
                costLength = costs.length;
                try {
                    double[] convertedCosts = Arrays.stream(costs).mapToDouble(Double::parseDouble).toArray();
                    String target = parts[2].toUpperCase();
                    if (target.matches(".*[,].*")) {
                        System.err.println("\nErrore: Formato nodo invalido : " + line);
                    }
                    if (source.equals(target))
                        continue;
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

        // Controllo se il nodo iniziale è presente nel grafo
        if (!graph.containsVertex(startNode)) {
            System.err.println("\nErrore: Il nodo iniziale non e' presente nel grafo.\n");
            return null;
        }

        return new JGraphTNode(graph, startNode, costLength);
    }

    public JGraphTNode(DefaultDirectedWeightedGraph<String, LabeledEdge<Costs>> graph, String startNode, int costLength) {
        this.graph = graph;
        this.startNode = startNode;
        this.costLength = costLength;
    }

    @Override
    public Iterable<Child<String>> successors() {
        return Graphs.successorListOf(graph, startNode).stream()
                .map(t -> new Child<>(new JGraphTNode(graph, t, costLength), graph.getEdge(startNode, t).label())).toList();
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

    @Override
    public int costLength() {
        return costLength;
    }
}
