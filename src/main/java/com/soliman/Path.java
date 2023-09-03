package com.soliman;

public record Path<Node>(Node actual, Path<Node> previous, Costs cost) {
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
