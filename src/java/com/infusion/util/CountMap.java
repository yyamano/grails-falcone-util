package com.infusion.util;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Lets you easily maintain counts in a map.
 *
 * @author eric
 */
public class CountMap<T> {
// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    /**
     * The key is the object you are counting, hte value is the count
     */
    private HashMap<T, Integer> counts = new HashMap<T, Integer>();

    /**
     * The total counts (of everything)
     */
    private int total = 0;

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    /**
     * Increments the counter for a given item
     */
    public int add(T item) {
        if (!counts.containsKey(item)) {
            counts.put(item, 1);
        } else {
            counts.put(item, counts.get(item) + 1);
        }
        total++;
        return counts.get(item);
    }

    /**
     * Returns the count for a given item
     */
    public Integer getCount(T o) {
        return counts.containsKey(o) ? counts.get(o) : 0;
    }

    /**
     * Returns the ratio of the count for a given item.
     */
    public int getRatio(T item) {
        return 100 * getCount(item) / Math.max(1, total);
    }

    /**
     * Returns an iterator of the items
     */
    public Set<Map.Entry<T, Integer>> iterator() {
        return counts.entrySet();
    }

}