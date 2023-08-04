package com.soliman;

public class DoubleCost implements PartialOrder<DoubleCost> {
    private final int cost1;
    private final int cost2;
    public DoubleCost(int cost1, int cost2){
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
}
