package util;

import static util.TreeKey.MID;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * See TreeList, but uses a string for the key instead of an integer index, also is backed by a map.
 */
public class Tree<T> {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    /**
     * Indexer, used to index the string value to the actual key (which includes the order)
     */
    private static final TreeMapIndexer<TreeKey, String> INDEXER = new TreeMapIndexer<TreeKey, String>() {
        public void index(TreeKey key, Map<String, TreeKey> index) {
            index.put(key.keyValue, key);
        }

        public void remove(TreeKey key, Map<String, TreeKey> index) {
            index.remove(key.keyValue);
        }
    };

// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    /**
     * The internal data structure
     */
    IndexedTreeMap<TreeKey, String> map = new IndexedTreeMap<TreeKey, String>(TreeKey.COMPARATOR, INDEXER);

    /**
     * The separator used to split strings passed in.
     */
    private final String separator;

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    public Tree() {
        separator = "\\.";
    }

    public Tree(String separator) {
        this.separator = separator;
    }

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    /**
     * Adds a leaf item to the tree, at the given location.  The location should not include separators
     */
    public void addItem(T data, String... location) {

        
        //The map the item will ultimately belong in
        final IndexedTreeMap<TreeKey, String> parentMap = getMap(chop(location));

        //The last item in "location" is the key
        final String itemKey = location[location.length - 1];


        //Only add to the map if it doesn't already exist
        if (!parentMap.containsKeyByIndex(itemKey)) {
            parentMap.put(new TreeKey(nextOrder(parentMap), itemKey), data);
        }
    }

    /**
     * Adds a leaf item to the tree at a given location.  The location should be the full location in a single string.
     */
    public void addItem(T data, String location) {
        addItem(data, stringToArray(location));
    }

    /**
     * Chops the last item off the array.
     */
    public String[] chop(String... location) {
        String[] rtn = new String[location.length - 1];
        for (int i = 0; i < location.length - 1; i++) {
            rtn[i] = location[i];
        }
        return rtn;
    }

    /**
     * Returns an item in the tree.
     *
     * @param location
     * @return
     */
    public T getItem(String... location) {
        final IndexedTreeMap<TreeKey, String> list = getMap(chop(location));
        return (T) list.getByIndex(location[location.length - 1]);
    }

    /**
     * Gets an item from the full path, separated by this.separator
     * <p/>
     * For example, getItemFromPath("tabset.tab.group.column.group.item")
     */
    public T getItemFromPath(String location) {
        return (T) getItem(stringToArray(location));
    }

    /**
     * Getter for the internal map.
     */
    public Map getTree() {
        return map;
    }

    /**
     * Removes an item at the given location
     */
    public void removeItem(String... location) {
        final IndexedTreeMap<TreeKey, String> map = getMap(chop(location));
        map.removeByIndex(location[location.length - 1]);
    }

    /**
     * Removes an item using a full path, separated by this.separator
     * <p/>
     * For example, getItemFromPath("tabset.tab.group.column.group.item")
     */
    public void removeItem(String location) {
        final String[] locationArray = stringToArray(location);
        removeItem(locationArray);

    }

    /**
     * Changes the order of an item already in the map.  The order is just a weight, not an actual order.
     *
     * @param order    The new order
     * @param location The location of the item you want to reorder.
     */
    public void setOrder(Integer order, String... location) {
        IndexedTreeMap<TreeKey, String> parentMap = getMap(chop(location));
        final String itemKey = location[location.length - 1];
        Object value = parentMap.getByIndex(itemKey);

        //Put back into map with new key
        parentMap.removeByIndex(itemKey);
        parentMap.put(new TreeKey(order, itemKey), value);
    }

    public void setOrder(String location, Integer order) {
        String[] path = stringToArray(location);
        setOrder(order, path);

    }

// ========================================================================================================================
//    Non-Public Instance Methods
// ========================================================================================================================

    /**
     * Gets a bucket at the specified location.  It's assumed that you are passing a location that contains a bucket, not
     * a  node.
     * @param locations
     * @return
     */
    public IndexedTreeMap<TreeKey, String> getMap(String... locations) {
        IndexedTreeMap<TreeKey, String> currentList = map;
        for (String location : locations) {
            if (!currentList.containsKeyByIndex(location)) {
                final int nextOrder = nextOrder(currentList);

                currentList.put(new TreeKey(nextOrder, location), new IndexedTreeMap<TreeKey, String>(TreeKey.COMPARATOR, INDEXER));
            }
            currentList = (IndexedTreeMap<TreeKey, String>) currentList.getByIndex(location);
        }
        assert currentList != null;
        return currentList;
    }

    /**
     * Returns the next available order from a map.  It will count up from MID until it finds an empty spot.  This is
     * so the map maintains the order items were added.
     */
    private int nextOrder(IndexedTreeMap<TreeKey, String> map) {
        int nextOrder = MID;
        if (map.size() > 0) {
            keys:
            for (TreeKey treeKey : map.keySet()) {
                if (treeKey.getOrder() > MID) {
                    if (treeKey.getOrder() == nextOrder + 1) {
                        nextOrder = treeKey.getOrder();
                    } else {
                        nextOrder++;
                        break keys;
                    }
                }
            }
        }
        return nextOrder;
    }

    private String[] stringToArray(String str) {
        final String[] strings = str.split(separator);
        return strings;
    }
}