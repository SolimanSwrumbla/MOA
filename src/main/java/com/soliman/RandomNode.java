package com.soliman;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomNode implements Node<String> {
    private int i;

    public RandomNode(int i) {
        this.i = i;
    }

    public RandomNode() {
        this(0);
    }

    @Override
    public Iterable<Child<String>> successors() {
        Random random = new Random(super.hashCode());
        // (3) -> max numbers of child
        int children = random.nextInt(3) + 1;
        return IntStream.range(0, children).mapToObj(i -> new Child<>(new RandomNode(random.nextInt(100)),
                new Costs(random.nextDouble(), random.nextDouble()))).toList();
    }

    @Override
    public String value() {
        return String.valueOf(i);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RandomNode node))
            return false;
        return i == node.i;
    }

    @Override
    public String toString() {
        return String.valueOf(i);
    }

    @Override
    public int hashCode() {
        return i;
    }
}
