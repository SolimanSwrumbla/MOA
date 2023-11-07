package com.soliman;

public record Path<N>(Node<N> actual, Path<N> previous, Costs cost) {
    public String pathToString() {
        if (previous == null)
            return actual.toString();
        return previous.pathToString() + ", " + actual.toString();
    }

    public String toString(String name) {
        return name + " = {" + pathToString() + "}" + "       c" + "(" + name + ") = " + cost;
    }

    public String toString() {
        return cost.toString();
    }
}