package util;

import sun.misc.ASCIICaseInsensitiveComparator;

import java.util.Comparator;

/**
 * A key in a tree map.  Basically, it's a string and an integer (used for storing order).
 */
public class TreeKey {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    /**
     * String comparator used in {@link #COMPARATOR}
     */
    private static final Comparator STRING_COMPARATOR = new ASCIICaseInsensitiveComparator();

    /**
     * A default comparator used to sort TreeKeys
     */
    public static final Comparator COMPARATOR = new Comparator() {
        public int compare(Object treeMapKey, Object treeMapKey1) {
            Integer rtn;

            if (treeMapKey == null) {
                rtn = 1;
            } else {
                if (treeMapKey.getClass() == String.class) {
                    final String keyValue = ((TreeKey) treeMapKey1).keyValue;
                    rtn = STRING_COMPARATOR.compare(treeMapKey,  keyValue);
                } else {
                    final TreeKey treeKey1 = (TreeKey) treeMapKey;
                    final TreeKey treeKey2 = (TreeKey) treeMapKey1;

                    rtn = STRING_COMPARATOR.compare(treeKey1.keyValue, treeKey2.keyValue);
                    if (rtn != 0) {
                        Integer locationDifference = treeKey1.getOrder() - treeKey2.getOrder();
                        if(locationDifference != 0) {
                            rtn = locationDifference;
                        }
                    }
                }
            }

            return rtn;
        }
    };

    /**
     * The default value used when creating new TreeKey instances, if a specific order is not provided.
     */
    public static final int MID = 20;


// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    public final String keyValue;
    public Integer order;

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    public TreeKey(String keyValue) {
        this.order = MID;
        this.keyValue = keyValue;
    }

    public TreeKey(Object keyValue) {
        if (keyValue instanceof String) {
            this.keyValue = (String) keyValue;
            this.order = MID;
        } else if (keyValue instanceof TreeKey) {
            this.keyValue = ((TreeKey) keyValue).keyValue;
            this.order = ((TreeKey) keyValue).getOrder();
        } else {
            throw new IllegalArgumentException("Invalid key type");
        }
    }

    public TreeKey(int order, String keyValue) {
        this.order = order;
        this.keyValue = keyValue;
    }

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        //Allow equals based on string
        if (o.getClass() == String.class) {
            return o.equals(keyValue);
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        TreeKey that = (TreeKey) o;

        if (!keyValue.equals(that.keyValue)) {
            return false;
        }

        return true;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public int hashCode() {
        return keyValue.hashCode();
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return keyValue;
    }
}
