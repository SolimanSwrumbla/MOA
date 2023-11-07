package com.soliman;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public interface HeuristicFunction<T> {
    double apply(Node<T> node, Predicate<Node<T>> endNodes, Map<Node<T>, Set<Path<T>>> paths);
}