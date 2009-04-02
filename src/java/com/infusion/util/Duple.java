package com.infusion.util;

import java.io.Serializable;

/**
 * A convenience class that makes it easy to return multiple objects in a type-safe way
 */
public class Duple<T, U> implements Serializable {
// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    public final T f1;
    public final U f2;

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    public Duple(T f1, U f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Duple duple = (Duple) o;

        if (f1 != null ? !f1.equals(duple.f1) : duple.f1 != null) return false;
        if (f2 != null ? !f2.equals(duple.f2) : duple.f2 != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (f1 != null ? f1.hashCode() : 0);
        result = 29 * result + (f2 != null ? f2.hashCode() : 0);
        return result;
    }

    public String toString() {
        return f1 + ", " + f2;
    }
}
