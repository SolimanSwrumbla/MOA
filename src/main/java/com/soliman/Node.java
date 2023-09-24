package com.soliman;

public interface Node<T> {
    T value();

    Iterable<Child<T>> successors();

    public record Child<T>(Node<T> target, Costs cost) {
    }
}
