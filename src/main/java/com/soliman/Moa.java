package com.soliman;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class Moa {
    public static <Node> boolean isDominated(Path<Node> path, Set<Path<Node>> otherPaths) {
        for (var otherPath : otherPaths) {
            if (otherPath.cost().isLessThan(path.cost())) {
                return true;
            }
        }
        return false;
    }

    public static <Node> boolean isDominatedOrEqual(Path<Node> path, Set<Path<Node>> otherPaths) {
        for (var otherPath : otherPaths) {
            if (path.equals(otherPath) || otherPath.cost().isLessThan(path.cost())) {
                return true;
            }
        }
        return false;
    }

    public static <Node> Map<Node, Set<Path<Node>>> search(DefaultDirectedWeightedGraph<Node, LabeledEdge<Costs>> graph,
            Node startNode, Set<Node> endNodes, Costs zero, HeuristicFunction<Node> heuristicFunction,
            Logger<Node> logger) {
        Set<Node> open = new HashSet<>();
        Set<Node> closed = new HashSet<>();
        Map<Node, Set<Path<Node>>> paths = new HashMap<>();
        Map<Node, Double> h = new HashMap<>();
        Map<Node, Set<Path<Node>>> solutionCosts = new HashMap<>();

        open.add(startNode);
        paths.put(startNode, new HashSet<>());
        paths.get(startNode).add(new Path<>(startNode, null, zero));
        h.put(startNode, heuristicFunction.apply(graph, startNode, endNodes, paths));

        for (int i = 0; !open.isEmpty(); i++) {
            var notDominated = getNotDominated(open, paths, solutionCosts, graph, endNodes);

            if (notDominated.isEmpty()) {
                logger.log(i, open, paths, graph, endNodes, null, notDominated, closed, solutionCosts);
                break;
            }

            Node currentNode = notDominated.stream().min(Comparator.comparingDouble(h::get)).get();
            logger.log(i, open, paths, graph, endNodes, currentNode, notDominated, closed, solutionCosts);
            open.remove(currentNode);
            closed.add(currentNode);

            if (endNodes.contains(currentNode)) {
                solutionCosts.put(currentNode, paths.get(currentNode));
                for (Node node : solutionCosts.keySet()) {
                    removeDominated(paths.get(currentNode), paths.get(node));
                }
                continue;
            }

            for (Node child : Graphs.successorListOf(graph, currentNode)) {
                Costs cost = graph.getEdge(currentNode, child).label();
                if (!closed.contains(child) && !open.contains(child)) {
                    paths.put(child, new HashSet<>());
                    for (Path<Node> path : paths.get(currentNode)) {
                        paths.get(child).add(new Path<>(child, path, path.cost().add(cost)));
                    }
                    removeDominated(paths.get(child), paths.get(child));
                    h.put(child, heuristicFunction.apply(graph, child, endNodes, paths));
                    open.add(child);
                } else {
                    boolean changed = false;
                    for (Path<Node> path : paths.get(currentNode)) {
                        Path<Node> newPath = new Path<>(child, path, path.cost().add(cost));
                        if (!isDominatedOrEqual(newPath, paths.get(child))) {
                            paths.get(child).add(newPath);
                            changed = true;
                        }
                    }
                    removeDominated(paths.get(child), paths.get(child));
                    if (changed) {
                        open.add(child);
                        closed.remove(child);
                    }
                }
            }
        }

        List<Node> toRemove = new ArrayList<>(); // velocizzabile?
        for (Node node : solutionCosts.keySet()) {
            if (solutionCosts.get(node).isEmpty())
                toRemove.add(node);
        }
        for (Node remove : toRemove) {
            solutionCosts.remove(remove);
        }
        return solutionCosts;
    }

    public static <Node> Set<Path<Node>> selectionFunction(DefaultDirectedWeightedGraph<Node, LabeledEdge<Costs>> graph,
            Node node, Map<Node, Set<Path<Node>>> paths, Set<Node> endNodes) {
        if (endNodes.contains(node)) {
            return paths.get(node);
        }
        Set<Path<Node>> result = new HashSet<>();
        for (var child : Graphs.successorListOf(graph, node)) {
            Costs cost = graph.getEdge(node, child).label();
            for (var path : paths.get(node)) {
                result.add(new Path<>(child, path, path.cost().add(cost)));
            }
        }
        removeDominated(result, result);
        return result;
    }

    private static <Node> Set<Node> getNotDominated(Set<Node> open, Map<Node, Set<Path<Node>>> paths,
            Map<Node, Set<Path<Node>>> solutionCosts, DefaultDirectedWeightedGraph<Node, LabeledEdge<Costs>> graph,
            Set<Node> endNodes) {
        Set<Node> result = new HashSet<>();
        for (Node node : open) {
            Set<Path<Node>> nodePaths = selectionFunction(graph, node, paths, endNodes);
            a: for (var path : nodePaths) {
                for (var otherNode : open) {
                    if (otherNode == node) {
                        continue;
                    }
                    if (isDominated(path, selectionFunction(graph, otherNode, paths, endNodes))) {
                        continue a;
                    }
                }
                for (var otherNode : solutionCosts.keySet()) {
                    if (isDominated(path, solutionCosts.get(otherNode))) {
                        continue a;
                    }
                }
                result.add(node);
                break a;
            }
        }
        return result;
    }

    private static <Node> void removeDominated(Set<Path<Node>> paths, Set<Path<Node>> otherPaths) {
        Set<Path<Node>> toRemove = new HashSet<>(); // velocizzabile?
        for (Path<Node> path : paths) {
            if (isDominated(path, otherPaths)) {
                toRemove.add(path);
            }
        }
        paths.removeAll(toRemove);
    }
}
