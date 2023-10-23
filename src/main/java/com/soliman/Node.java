package com.soliman;

public interface Node<T> {
    T value();

    int costLength();

    Iterable<Child<T>> successors();

    public record Child<T>(Node<T> target, Costs cost) {
    }
}
