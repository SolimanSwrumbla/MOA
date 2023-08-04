package com.soliman;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Moa {

    private static <Node> List<Node> generateSuccessors(Node Node) {
        // Implement the logic to generate alternative Nodes based on your domain-specific requirements
        List<Node> successors = new ArrayList<>();
        for (Alternative alternative : Node.alternatives) {
            successors.add(alternative.Node);
        }
        return successors;
    }

    private static <Node> double heuristicFunction(Node Node) {
        // Implement the heuristic function based on your domain-specific requirements
        return 0.0;
    }

    public static <Node, Cost extends PartialOrder<Cost>> List<Node> search(Node startNode) {
        Set<Node> open = new HashSet<>();
        Set<Node> ND = new HashSet<>();
        Set<Node> closed = new HashSet<>();
        Set<Node> solution = new HashSet<>();
        Set<Cost> solutionCosts = new HashSet<>();
        Set<Node> solutionGoals = new HashSet<>();
        Map<Node, Set<Cost>> label = new HashMap<>();

        open.add(startNode);

        while (!open.isEmpty()) {

            // Step 1: Find Nodes in OPEN that are not dominated by solution costs and Node selection values

            // Creiamo un iteratore MOAStar per il grafo
            MOAStarIterator<String, DefaultWeightedEdge> iterator = new MOAStarIterator<>(graph, "S", "D", numObjectives);
            // Inizializziamo l'insieme OPEN con il nodo iniziale
            Set<String> open = new HashSet<>();
            open.add("S");
            // Inizializziamo l'insieme delle soluzioni e dei costi delle soluzioni come vuoti
            Set<String> solutionGoals = new HashSet<>();
            Set<Vector> solutionCosts = new HashSet<>();
            // Troviamo l'insieme ND dei nodi in OPEN che non sono dominati
            Set<String> nonDominated = iterator.findNonDominatedNodesInOpen(open, solutionGoals, solutionCosts);
            // Stampiamo l'insieme ND
            System.out.println(nonDominated); // [S]

            // Step 2: Terminate or select a Node for expansion

            String selected = null;
            if (!nonDominated.isEmpty()) {
                selected = Collections.min(nonDominated, new Comparator<String>() {
                    @Override
                    public int compare(String n1, String n2) {
                        double f1 = getCost(n1) + heuristicFunction(n1);
                        double f2 = getCost(n2) + heuristicFunction(n2);
                        return Double.compare(f1, f2);
                    }
                });
            } else {
                selected = Collections.min(open, new Comparator<String>() {
                    @Override
                    public int compare(String n1, String n2) {
                        double f1 = getCost(n1) + heuristicFunction(n1);
                        double f2 = getCost(n2) + heuristicFunction(n2);
                        return Double.compare(f1, f2);
                    }
                });
            }
            open.remove(selected);
            closed.add(selected);

            // Step 3: Bookkeeping to maintain costs and Node selection values

            for (String successor : Graphs.successorListOf(graph, selected)) {
                if (!closed.contains(successor)) {
                    Vector cost = getCost(successor);
                    Set<Vector> costs = label.get(successor);
                    if (costs == null) {
                        costs = new HashSet<>();
                        label.put(successor, costs);
                    }
                    Set<Vector> dominatedCosts = new HashSet<>();
                    for (Vector c : costs) {
                        if (cost.greaterThan(c)) {
                            dominatedCosts.add(c);
                        }
                    }
                    costs.removeAll(dominatedCosts);
                    if (!dominatesAny(costs, cost)) {
                        costs.add(cost);
                        open.add(successor);
                        nonDominated.add(successor);
                    }
                }
            }

            // Step 4: Identify solutions

            for (String node : nonDominated) {
                boolean isParetoOptimal = true;
                for (String other : nonDominated) {
                    if (!node.equals(other) && dominates(other, node)) {
                        isParetoOptimal = false;
                        break;
                    }
                }
                if (isParetoOptimal) {
                    solution.add(node);
                }
            }

            // Step 5: Expand the selected Node (alternative Nodes) and examine its successors

            // Step 6: Iterate
        }

        return new ArrayList<>(solution);
    }

}
