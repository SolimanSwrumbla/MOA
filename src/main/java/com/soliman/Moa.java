package com.soliman;

import java.util.*;
import java.util.stream.Collectors;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class Moa {

    public static <Node, Cost extends PartialOrder<Cost>> Map<Node, Set<Cost>> search(DefaultDirectedWeightedGraph<Node, LabeledEdge<Cost>> graph, Node startNode, Set<Node> endNodes, Cost zero, HeuristicFunction<Node, Cost> heuristicFunction) {
        Set<Node> open = new HashSet<>();
        Set<Node> closed = new HashSet<>();
        Map<Node, Set<Cost>> solutionCosts = new HashMap<>();
        Map<Node, Set<Cost>> label = new HashMap<>();

        open.add(startNode);
        label.put(startNode, Set.of(zero));
        
        mainLoop:
        while (!open.isEmpty()) {
            Set<Node> ND = new HashSet<>();
            for (Node node:open) {
                for (Cost cost:label.get(node)) {
                    boolean isCostND = true;
                    for (Node otherNode:solutionCosts.keySet()) {
                        if (node != otherNode) {
                            for (Cost otherCost:solutionCosts.get(otherNode)){
                                if (otherCost.isLessThan(cost)){
                                    isCostND = false;
                                }
                            }
                        }
                    }
                    for (Node otherNode:open) {
                        if (node != otherNode) {
                            for (Cost otherCost:label.get(otherNode)){
                                if (otherCost.isLessThan(cost)){
                                    isCostND = false;
                                }
                            }
                        }
                    }
                    if (isCostND) {
                        ND.add(node);
                    }
                }
            }

            if (ND.isEmpty()) {
                break mainLoop;
            }

            Node prescelto = heuristicFunction.apply(ND, label);

            open.remove(prescelto);
            closed.add(prescelto);
            if (endNodes.contains(prescelto)) {
                solutionCosts.put(prescelto, label.get(prescelto));
                continue;
            }

            for (Node child : Graphs.successorListOf(graph, prescelto)) {
                Cost cost = graph.getEdge(prescelto, child).label();
                Set<Cost> newCosts = label.get(prescelto).stream().map(c -> c.add(cost)).collect(Collectors.toSet());
                if (label.putIfAbsent(child, new HashSet<>()) == null) {
                    open.add(child);
                }
                for (Cost newCost : newCosts) {
                    boolean nd = true;
                    for (Cost otherCost : label.get(child)) {
                        if (otherCost.isLessThan(newCost)) {
                            nd = false;
                        }
                        if (newCost.isLessThan(otherCost)) {
                            label.get(child).remove(otherCost);
                            closed.remove(child);
                            open.add(child);
                        }
                    }
                    if (nd) {
                        label.get(child).add(newCost);
                    }
                }
            }
            // System.out.println("esaminando " + prescelto + " label: " + label + " (closed=" + closed + ", open=" + open + ")");
        }

        for (Node node : solutionCosts.keySet()) {
            for (Cost cost : solutionCosts.get(node)) {
                for (Node otherNode : solutionCosts.keySet()) {
                    List<Cost> toRemove = new ArrayList<>();
                    for (Cost otherCost : solutionCosts.get(otherNode)) {
                        if (cost.isLessThan(otherCost)) {
                            toRemove.add(otherCost);
                        }
                    }
                    solutionCosts.get(otherNode).removeAll(toRemove);
                }
            }
        }
        List<Node> toRemove = new ArrayList<>();
        for (Node node : solutionCosts.keySet()) {
            if (solutionCosts.get(node).isEmpty()) toRemove.add(node);
        }
        for (Node remove : toRemove) {
            solutionCosts.remove(remove);
        }
        return solutionCosts;
    }

}
