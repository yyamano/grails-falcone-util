package com.infusion.util.data.filter;

/**
 * An interface that defines a filter for a type of object
 * @author eric
 */
public interface DataFilter<X> {
    public boolean filter(X x);
}
