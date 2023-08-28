package com.soliman;

import java.util.Locale;
import java.util.Objects;

public class DoubleCost implements PartialOrder<DoubleCost> {
    private final double cost1;
    private final double cost2;
    public DoubleCost(double cost1, double cost2){
        this.cost1 = cost1;
        this.cost2 = cost2;	
    }
    
    @Override
    public boolean isGreaterOrEqualThan(DoubleCost other) {
        return(this.cost1 >= other.cost1 && this.cost2 >= other.cost2);
    }

    @Override
    public boolean isLessOrEqualThan(DoubleCost other) {
        return(this.cost1 <= other.cost1 && this.cost2 <= other.cost2);
    }

    @Override
    public DoubleCost add(DoubleCost other) {
        return new DoubleCost(this.cost1 + other.cost1, this.cost2 + other.cost2);
    }

    public double sum() {
        return cost1 + cost2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DoubleCost other)) return false;
        return cost1 == other.cost1 && cost2 == other.cost2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost1, cost2);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "(%.2fKm %.2fh)", cost1, cost2);
    }
}
