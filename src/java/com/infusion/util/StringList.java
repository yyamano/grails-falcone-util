package com.infusion.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class is highly (and very carefully) optimized, unless you REALLY know what you are doing, don't mess with it.
 */
@SuppressWarnings({"ForLoopReplaceableByForEach"})
public class StringList {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    /**
     * should never be modified
     */
    public static final StringList empty = new StringList();
    /**
     * how many new entries to allocated in the array when space is run out
     */
    private static final int JUMP = 10;

// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    /**
     * never null
     */
    String[] list;
    int used;

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    public StringList() {
        list = new String[JUMP];
    }

    public StringList(final String... strings) {
        list = strings;
        used = list.length;
    }

    /**
     * @param strings
     */
    public StringList(Object... strings) {
        // this particular method isn't called often enough to justify improving the
        // performance beyond what is already here, although most other methods of this
        // class are.

        ArrayList<String> newList = new ArrayList<String>(strings.length);
        // this must NOT be a for each loop, this is a performance
        // critical method and the extra iterator object adds too much
        // overhead
        for (int i = 0; i < strings.length; i++) {
            final Object o = strings[i];
            if (o instanceof StringList) {
                final StringList stringList = (StringList) o;
                newList.addAll(Arrays.asList(stringList.list));
            } else {
                newList.add(o.toString());
            }
        }

        fromList(newList);
    }

    public StringList(int capacity) {
        list = new String[capacity];
    }

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    /**
     * @param s null has a special meaning, and is used for merging
     * @return
     */
    public StringList add(String s) {
        resize();
        list[used++] = s;
        return this;
    }

    public StringList add(StringList list) {
        if (list == null) {
            return this;
        }

        // this must NOT be a for each loop, this is a performance
        // critical method and the extra iterator object adds too much
        // overhead
        for (int i = 0; i < list.used; i++) {
            add(list.list[i]);
        }
        return this;
    }

    public int length() {
        int total = 0;
        for (int i = 0; i < used; i++) {
            final String s = list[i];
            if (s != null) {
                total += s.length();
            }
        }
        return total;
    }

    public int size() {
        return used;
    }

    public String toString() {
        // this IS called frequently, and must use the fastest code possible
        final StringBuilder b = new StringBuilder(length());
        for (int i = 0; i < used; i++) {
            final String s = list[i];
            if (s != null) {
                b.append(s);
            }
        }
        return b.toString();
    }

// ========================================================================================================================
//    Non-Public Instance Methods
// ========================================================================================================================

    private void fromList(List<String> newList) {
        list = new String[newList.size() + JUMP];
        for (int i = 0; i < newList.size(); i++) {
            list[i] = newList.get(i);
        }
        used = newList.size();
    }

    /**
     * resize the array and make sure there is at least one array entry free
     */
    private void resize() {
        if (used == list.length) {
            final String[] newList = new String[list.length + JUMP];
            System.arraycopy(list, 0, newList, 0, list.length);
			list = newList;
		}
	}
}
