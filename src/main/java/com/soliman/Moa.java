package com.soliman;

import java.util.*;
import java.util.stream.Collectors;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class Moa {

    public static <Node, Cost extends PartialOrder<Cost>> Map<Node, Set<Path<Node, Cost>>> search(DefaultDirectedWeightedGraph<Node, LabeledEdge<Cost>> graph, Node startNode, Set<Node> endNodes, Cost zero, HeuristicFunction<Node, Cost> heuristicFunction) {
        Set<Node> open = new HashSet<>();
        Set<Node> closed = new HashSet<>();
        Map<Node, Set<Path<Node, Cost>>> solutionCosts = new HashMap<>();
        Map<Node, Set<Path<Node, Cost>>> label = new HashMap<>();

        open.add(startNode);
        label.put(startNode, Set.of(new Path<>(startNode, null, zero)));
        
        mainLoop:
        while (!open.isEmpty()) {
            Set<Node> ND = new HashSet<>();
            for (Node node : open) {
                for (var path : label.get(node)) {
                    Cost cost = path.cost();
                    boolean isCostND = true;
                    for (Node otherNode:solutionCosts.keySet()) {
                        if (node != otherNode) {
                            for (var otherPath : solutionCosts.get(otherNode)) {
                                Cost otherCost = otherPath.cost();
                                if (otherCost.isLessThan(cost)){
                                    isCostND = false;
                                }
                            }
                        }
                    }
                    for (Node otherNode:open) {
                        if (node != otherNode) {
                            for (var otherPath : label.get(otherNode)) {
                                Cost otherCost = otherPath.cost();
                                if (otherCost.isLessThan(cost)) {
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
                Set<Path<Node, Cost>> newPaths = label.get(prescelto).stream().map(p -> new Path<>(child, p, p.cost().add(cost))).collect(Collectors.toSet());
                if (label.putIfAbsent(child, new HashSet<>()) == null) {
                    open.add(child);
                }
                for (var newPath : newPaths) {
                    Cost newCost = newPath.cost();
                    boolean nd = true;
                    List<Path<Node, Cost>> toRemove = new ArrayList<>();
                    for (var otherPath : label.get(child)) {
                        Cost otherCost = otherPath.cost();
                        if (otherCost.isLessThan(newCost)) {
                            nd = false;
                        }
                        if (newCost.isLessThan(otherCost)) {
                            toRemove.addAll(label.get(child).stream().filter(p -> p.cost().equals(otherCost)).toList());
                            closed.remove(child);
                            open.add(child);
                        }
                    }
                    for (var remove : toRemove) {
                        label.get(child).remove(remove);
                    }
                    if (nd) {
                        label.get(child).add(newPath);
                    }
                }
            }
        }

        for (Node node : solutionCosts.keySet()) {
            for (var path : solutionCosts.get(node)) {
                Cost cost = path.cost();
                for (Node otherNode : solutionCosts.keySet()) {
                    List<Path<Node, Cost>> toRemove = new ArrayList<>();
                    for (var otherPath : solutionCosts.get(otherNode)) {
                        Cost otherCost = otherPath.cost();
                        if (cost.isLessThan(otherCost)) {
                            toRemove.add(otherPath);
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