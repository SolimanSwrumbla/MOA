package com.soliman;

public record Path<Node, Cost extends PartialOrder<Cost>>(Node actual, Path<Node, Cost> previous, Cost cost) {
    public String pathToString() {
        if (previous == null) return actual.toString();
        return previous.pathToString() + ", " + actual.toString();
    }
    
    public String toString(String name) {
        return name + " = {" + pathToString() + "}" + "   c" + "(" + name + ") = " + cost;
    }
}
