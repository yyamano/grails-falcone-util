package com.infusion.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 */
public class Joiner {
// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    final private String after;
    final private String afterMany;
    final private String before;
    final private String beforeMany;
    final private String between;
    final List<StringList> items = new ArrayList<StringList>();

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    public Joiner() {
        this(null, null, ", ", null, null);
    }

    public Joiner(String between) {
        this(null, null, between, null, null);
    }

    public Joiner(String before, String between, String after) {
        this(before, null, between, null, after);
    }

    /**
     * @param before     Included at the beginning of the string if there are more than zero items
     * @param beforeMany Included after the "before" string if there is more than one item
     * @param between    Included between each item
     * @param afterMany  Included before the "after" string if there is more than one item
     * @param after      Included at the end of the string if there are more than zero items
     */
    public Joiner(String before, String beforeMany, String between, String afterMany, String after) {
        this.before = before;
        this.between = between;
        this.beforeMany = beforeMany;
        this.after = after;
        this.afterMany = afterMany;
    }

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    public Joiner add(String item) {
        if (item != null) {
            items.add(new StringList(item));
        }
        return this;
    }

    public Joiner add(Object item) {
        if(item != null) {
            items.add(new StringList(item.toString()));
        }
        return this;

    }

    public Joiner add(String ... strings) {
        if (strings != null) for (String s : strings) {
            add(s);
        }
        return this;
    }

    public Joiner add(List<String> list) {
        if (list == null) return this;
        for (String s : list) {
            add(s);
        }
        return this;
    }

    /**
     * If the list given is null or empty, then an entry WON'T be added
     *
     * @param list
     * @return
     */
    public Joiner add(StringList list) {
        if (list != null && list.size() > 0) {
            items.add(list);
        }
        return this;
    }

    public Joiner add(Collection<String> collection) {
        if (collection == null) return this;
        for (String s : collection) {
            add(s);
        }
        return this;
    }

    /**
     * Appends the given string on to the end of the last item added
     *
     * @param s
     * @return
     */
    public Joiner append(String s) {
        StringList last = items.get(items.size() - 1);
        last.add(s);
        return this;
    }

    public int size() {
        return items.size();
    }

    public String toString() {
        if (items.size() == 0) return "";

        StringBuilder buf = new StringBuilder(64);
        if (before != null) {
            buf.append(before);
        }
        if (size() > 1 && beforeMany != null) {
            buf.append(beforeMany);
        }
        boolean first = true;
        for (Iterator<StringList> iterator = items.iterator(); iterator.hasNext();) {
            StringList item = iterator.next();
            if (!first && between != null) {
                buf.append(between);
            } else {
                first = false;
            }
            buf.append(item);
        }
        if (size() > 1 && afterMany != null) {
            buf.append(afterMany);
        }
        if (after != null) {
            buf.append(after);
        }
        return buf.toString();
    }

    public StringList toStringList() {
        if (items.size() == 0) return new StringList();

        StringList buf = new StringList();
        if (before != null) {
            buf.add(before);
        }
        if (size() > 1 && beforeMany != null) {
            buf.add(beforeMany);
        }
        boolean first = true;
        for (Iterator<StringList> iterator = items.iterator(); iterator.hasNext();) {
            StringList item = iterator.next();
            if (!first && between != null) {
                buf.add(between);
            } else {
                first = false;
            }
            buf.add(item);
        }
        if (size() > 1 && beforeMany != null) {
            buf.add(beforeMany);
        }
        if (after != null) {
			buf.add(after);
		}
		return buf;
	}

}
