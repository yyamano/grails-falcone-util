package com.infusion.util;

import java.util.*;

/**
 * A class that takes two collections and divides them into three groups: that which is only in the first collection,
 * that which is in both collections, and that which is only in the second collection.
 */
public class Venn<T> {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    public static final int WHICH_ALL = 3;
    /**
     * Includes only the elements which are not in both sets
     */
    public static final int WHICH_DIFF = 2;
    /**
     * Includes only the elements that are in both sets
     */
    public static final int WHICH_SAME = 1;

// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    public final Set<T> groupA = new LinkedHashSet<T>();
    public final Set<T> groupAB = new LinkedHashSet<T>();
    public final Set<T> groupB = new LinkedHashSet<T>();

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    /**
     * Null for either or both parameters is okay.
     */
    public Venn(final Iterable<T> cA, final Iterable<T> cB) {
        this(cA, cB, null, WHICH_ALL);
    }

    /**
     * @param comparator the method used to compare two objects of the given sets. null means to use the equals() method.
     */
    public Venn(Iterable<T> cA, Iterable<T> cB, Comparator<T> comparator) {
        this(cA, cB, comparator, WHICH_ALL);
    }

    public Venn(Iterable<T> cA, Iterable<T> cB, Comparator<T> comparator, int which) {
        if (cA == null) {
            if (cB != null) {
                if ((which & WHICH_DIFF) > 0) {
                    addAll(groupB, cB);
                }
            }
        } else if (cB == null) {
            if ((which & WHICH_DIFF) > 0) {
                addAll(groupA, cA);
            }
        } else {
            HashSet<T> remaining = new LinkedHashSet<T>();
            addAll(remaining, cB);
            for (Iterator<T> iterator = cA.iterator(); iterator.hasNext();) {
                T o = iterator.next();
                if (remove(remaining, o, comparator)) {
                    if ((which & WHICH_SAME) > 0) groupAB.add(o);
                } else {
                    if ((which & WHICH_DIFF) > 0) groupA.add(o);
                }
            }

            if ((which & WHICH_DIFF) > 0) groupB.addAll(remaining);
        }
    }

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    public VennResult getResult() {
        if (!groupA.isEmpty()) {
            return VennResult.A;
        }
        if (!groupB.isEmpty()) {
            return VennResult.B;
        }
        return VennResult.OK;
    }

    /**
     * Whether or not this venn is a complete match
     */
    public boolean matches() {
        return
                groupA.size() == 0 &&
                        groupB.size() == 0;
    }

// ========================================================================================================================
//    Non-Public Instance Methods
// ========================================================================================================================

    private void addAll(Set<T> groupA, Iterable<T> cA) {
        for (T t : cA) {
            groupA.add(t);
        }
    }

    private boolean remove(Collection<T> set, T o, Comparator<T> comparator) {
        if (comparator == null) {
            if (set.contains(o)) {
                set.remove(o);
                return true;
            }
            return false;
        }

        for (Iterator<T> iterator = set.iterator(); iterator.hasNext();) {
            T t = iterator.next();
            if (comparator.compare(o, t) == 0) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

}
