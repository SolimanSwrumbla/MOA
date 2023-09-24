package com.soliman;

import java.util.*;
import java.util.function.Predicate;

public class Moa {
    public static <N> boolean isDominated(Path<N> path, Set<Path<N>> otherPaths) {
        for (var otherPath : otherPaths) {
            if (otherPath.cost().isLessThan(path.cost())) {
                return true;
            }
        }
        return false;
    }

    public static <N> boolean isDominatedOrEqual(Path<N> path, Set<Path<N>> otherPaths) {
        for (var otherPath : otherPaths) {
            if (path.equals(otherPath) || otherPath.cost().isLessThan(path.cost())) {
                return true;
            }
        }
        return false;
    }

    public static <N> Map<Node<N>, Set<Path<N>>> search(Node<N> startNode, Predicate<Node<N>> endNodes, Costs zero,
            HeuristicFunction<N> heuristicFunction, Logger<N> logger) {
        Set<Node<N>> open = new HashSet<>();
        Set<Node<N>> closed = new HashSet<>();
        Map<Node<N>, Set<Path<N>>> paths = new HashMap<>();
        Map<Node<N>, Double> h = new HashMap<>();
        Map<Node<N>, Set<Path<N>>> solutionCosts = new HashMap<>();

        open.add(startNode);
        paths.put(startNode, new HashSet<>());
        paths.get(startNode).add(new Path<>(startNode, null, zero));
        h.put(startNode, heuristicFunction.apply(startNode, endNodes, paths));

        for (int i = 0; !open.isEmpty(); i++) {
            var notDominated = getNotDominated(open, paths, solutionCosts, endNodes);

            if (notDominated.isEmpty()) {
                logger.log(i, open, paths, endNodes, null, notDominated, closed, solutionCosts);
                break;
            }

            Node<N> currentNode = notDominated.stream().min(Comparator.comparingDouble(h::get)).get();
            logger.log(i, open, paths, endNodes, currentNode, notDominated, closed, solutionCosts);
            open.remove(currentNode);
            closed.add(currentNode);

            if (endNodes.test(currentNode)) {
                solutionCosts.put(currentNode, paths.get(currentNode));
                for (var node : solutionCosts.keySet()) {
                    removeDominated(paths.get(currentNode), paths.get(node));
                }
                continue;
            }

            for (var child : currentNode.successors()) {
                Costs cost = child.cost();
                if (!closed.contains(child.target()) && !open.contains(child.target())) {
                    paths.put(child.target(), new HashSet<>());
                    for (var path : paths.get(currentNode)) {
                        paths.get(child.target()).add(new Path<>(child.target(), path, path.cost().add(cost)));
                    }
                    removeDominated(paths.get(child.target()), paths.get(child.target()));
                    h.put(child.target(), heuristicFunction.apply(child.target(), endNodes, paths));
                    open.add(child.target());
                } else {
                    boolean changed = false;
                    for (Path<N> path : paths.get(currentNode)) {
                        Path<N> newPath = new Path<>(child.target(), path, path.cost().add(cost));
                        if (!isDominatedOrEqual(newPath, paths.get(child.target()))) {
                            paths.get(child.target()).add(newPath);
                            changed = true;
                        }
                    }
                    removeDominated(paths.get(child.target()), paths.get(child.target()));
                    if (changed) {
                        open.add(child.target());
                        closed.remove(child.target());
                    }
                }
            }
        }

        List<Node<N>> toRemove = new ArrayList<>(); // velocizzabile?
        for (var node : solutionCosts.keySet()) {
            if (solutionCosts.get(node).isEmpty())
                toRemove.add(node);
        }
        for (var remove : toRemove) {
            solutionCosts.remove(remove);
        }
        return solutionCosts;
    }

    public static <N> Set<Path<N>> selectionFunction(Node<N> node, Map<Node<N>, Set<Path<N>>> paths,
            Predicate<Node<N>> endNodes) {
        if (endNodes.test(node)) {
            return paths.get(node);
        }
        Set<Path<N>> result = new HashSet<>();
        for (var child : node.successors()) {
            Costs cost = child.cost();
            for (var path : paths.get(node)) {
                result.add(new Path<>(child.target(), path, path.cost().add(cost)));
            }
        }
        removeDominated(result, result);
        return result;
    }

    private static <N> Set<Node<N>> getNotDominated(Set<Node<N>> open, Map<Node<N>, Set<Path<N>>> paths,
            Map<Node<N>, Set<Path<N>>> solutionCosts, Predicate<Node<N>> endNodes) {
        Set<Node<N>> result = new HashSet<>();
        for (var node : open) {
            var nodePaths = selectionFunction(node, paths, endNodes);
            a: for (var path : nodePaths) {
                for (var otherNode : open) {
                    if (otherNode == node) {
                        continue;
                    }
                    if (isDominated(path, selectionFunction(otherNode, paths, endNodes))) {
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

    private static <N> void removeDominated(Set<Path<N>> paths, Set<Path<N>> otherPaths) {
        Set<Path<N>> toRemove = new HashSet<>(); // velocizzabile?
        for (Path<N> path : paths) {
            if (isDominated(path, otherPaths)) {
                toRemove.add(path);
            }
        }
        paths.removeAll(toRemove);
    }
}
