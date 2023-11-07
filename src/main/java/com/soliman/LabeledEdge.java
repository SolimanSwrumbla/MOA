package com.soliman;

public class LabeledEdge<T> {
    private final T label;

    public LabeledEdge(T label) {
        this.label = label;
    }

    public T label() {
        return label;
    }

    @Override
    public String toString() {
        return label.toString();
    }
}