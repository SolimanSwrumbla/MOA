package com.soliman;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public interface Logger<T> {
    void log(int k, Set<Node<T>> open, Map<Node<T>, Set<Path<T>>> paths, Predicate<Node<T>> endNodes,
            Node<T> prescelto, Set<Node<T>> ND, Set<Node<T>> closed, Map<Node<T>, Set<Path<T>>> solutionCosts);

    static <T> Logger<T> noLogger() {
        return (k, open, paths, endNodes, prescelto, ND, closed, solutionCosts) -> {
        };
    }
}