package com.soliman;

public interface PartialOrder<T extends PartialOrder<T>> {
    boolean isLessOrEqualThan(T other);
    boolean isGreaterOrEqualThan(T other);
    T add(T other);

    default boolean isLessThan(T other){
        return isLessOrEqualThan(other) && ! other.isLessOrEqualThan((T) this);
    }

    default boolean isGreaterThan(T other){
        return isGreaterOrEqualThan(other) && ! other.isGreaterOrEqualThan((T) this);
    }
    
}
