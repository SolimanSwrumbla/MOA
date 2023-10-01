package com.soliman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class App {
    public static void main(String[] args) {

        DefaultDirectedWeightedGraph<String, LabeledEdge<Costs>> graph = new DefaultDirectedWeightedGraph<>(null, null);

        String startNode = "";
        Set<String> endNodes = new HashSet<>();
        boolean directional = true;
        Logger<String> logger = Logger.noLogger();
        Integer costLength = null;
        String input = null;

        try (BufferedReader reader = new BufferedReader(new FileReader("settings.txt"))) {
            input = reader.readLine().split(":")[1].trim();
            startNode = reader.readLine().split(":")[1].trim().toUpperCase();
            String endNodesString = reader.readLine().split(":")[1].trim().toUpperCase();
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
            endNodes.addAll(Arrays.asList(endNodesString.split("\\s*,\\s*")));

            String direct = reader.readLine().split(":")[1].trim();
            if (direct.isBlank()) {
                System.out.println("\nErrore: Opzione direzionale vuota. (Default S).\n");
            } else if (!direct.equalsIgnoreCase("S") && !direct.equalsIgnoreCase("N")) {
                System.err.println("\nErrore: Opzione direzionale non valida. Utilizzare 'S' o 'N'. (Default S).");
            } else {
                directional = direct.equalsIgnoreCase("S");
            }

            String explaination = reader.readLine().split(":")[1].trim();
            if (direct.isBlank()) {
                System.out.println("\nErrore: Opzione spiegazione vuota. (Default N).\n");
            } else if (!direct.equalsIgnoreCase("S") && !direct.equalsIgnoreCase("N")) {
                System.err.println("\nErrore: Opzione spiegazione non valida. Utilizzare 'S' o 'N'. (Default N).");
            } else {
                logger = explaination.equalsIgnoreCase("S") ? new ExplainationLogger() : Logger.noLogger();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                    return;
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

        if (!graph.containsVertex(startNode)) {
            System.err.println("\nErrore: Il nodo iniziale non e' presente nel grafo.\n");
            return;
        }

        if (logger instanceof ExplainationLogger) {
            System.out.println(
                    "\n-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("                                             OPEN");
            System.out.println(
                    "----------------------------------------------------------------------------------------------                                                                                           SOL_COSTS");
            System.out.println(
                    "  k       n                                 New G(n)                                 New F(n)                                  CLOSED                                                     e GOALS");
            System.out.println(
                    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        } else
            System.out.println();

        var solutionPath = Moa.search(new JGraphTNode(graph, startNode), n ->
        endNodes.contains(n.value()), new Costs(new double[costLength]),
        App::heuristicFunction, logger);
        //var solutionPath = Moa.search(new RandomNode(), n -> Integer.parseInt(n.value()) >= 95, new Costs(0, 0),
        //        App::heuristicFunction, logger);

        if (logger instanceof ExplainationLogger) {
            System.out.println(
                    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        }

        int i = 1;
        for (var endNode : solutionPath.keySet()) {
            for (var path : solutionPath.get(endNode)) {
                System.out.println(path.toString("P" + i + "*"));
                i++;
            }
        }
        System.out.println();
    }

    public static <T> double heuristicFunction(Node<T> node, Predicate<Node<T>> endNodes,
            Map<Node<T>, Set<Path<T>>> paths) {
        if (endNodes.test(node)) {
            return paths.get(node).stream().mapToDouble(p -> p.cost().sum()).min().orElseThrow();
        }
        double minCost = Double.POSITIVE_INFINITY;
        for (var child : node.successors()) {
            Costs cost = child.cost();
            for (var path : paths.get(node)) {
                double value = path.cost().sum() + cost.sum();
                if (value < minCost)
                    minCost = value;
            }
        }
        return minCost;
    }
}