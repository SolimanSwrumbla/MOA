package com.soliman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {

        // Inizializzazione delle variabili e impostazioni iniziali
        String input = null;
        String startNode = "";
        Set<String> endNodes = new HashSet<>();
        boolean directional = true;
        Logger<String> logger = Logger.noLogger();
        boolean dynamicGraph = false;

        // Lettura delle impostazioni da un file
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

            String explanation = reader.readLine().split(":")[1].trim();
            if (explanation.isBlank()) {
                System.out.println("\nErrore: Opzione spiegazione vuota. (Default N).\n");
            } else if (!explanation.equalsIgnoreCase("S") && !explanation.equalsIgnoreCase("N")) {
                System.err.println("\nErrore: Opzione spiegazione non valida. Utilizzare 'S' o 'N'. (Default N).");
            } else {
                logger = explanation.equalsIgnoreCase("S") ? new ExplanationLogger() : Logger.noLogger();
            }

            String dynamic = reader.readLine().split(":")[1].trim();
            if (dynamic.isBlank()) {
                System.out.println("\nErrore: Opzione grafo dinamico vuota. (Default N).\n");
            } else if (!dynamic.equalsIgnoreCase("S") && !dynamic.equalsIgnoreCase("N")) {
                System.err.println("\nErrore: Opzione grafo dinamico non valida. Utilizzare 'S' o 'N'. (Default N).");
            } else {
                dynamicGraph = dynamic.equalsIgnoreCase("S");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stampa dell'intestazione per la registrazione delle iterazioni
        if (logger instanceof ExplanationLogger) {
            System.out.println(
                    "\n-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("                                             OPEN");
            System.out.println(
                    "----------------------------------------------------------------------------------------------                                                                                           SOL_COSTS");
            System.out.println(
                    "  k       n                                 New G(n)                                 New F(n)                                  CLOSED                                                     e GOALS");
            System.out.println(
                    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        } else {
            System.out.println();
        }

        // Creazione del grafo
        Node<String> graph = dynamicGraph ? DynamicNode.detectCostLength(input, startNode, directional) : StaticNode.fromFile(input, startNode, directional);

        // Esecuzione dell'algoritmo MOA* e recupero delle soluzioni
        var solutionPath = Moa.search(graph, n -> endNodes.contains(n.value()), new Costs(new double[graph.costLength()]),
                Main::heuristicFunction, logger);

        //var solutionPath = Moa.search(new RandomNode(), n -> Integer.parseInt(n.value()) >= 95, new Costs(0, 0),
        //        Main::heuristicFunction, logger);

        if (logger instanceof ExplanationLogger) {
            System.out.println(
                    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        }

        // Stampa delle soluzioni trovate
        int i = 1;
        for (var endNode : solutionPath.keySet()) {
            for (var path : solutionPath.get(endNode)) {
                System.out.println(path.toString("P" + i + "*"));
                i++;
            }
        }
        System.out.println();
    }

    // Definizione della funzione euristica di esempio
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
