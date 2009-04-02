package util;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Mar 18, 2009
 * Time: 1:56:32 PM
 */
public class IndexedTreeMap<K, I> implements Map<K, Object> {
    private TreeMap<K, Object> internal;
    private Map<I, K> keyIndex;
    private TreeMapIndexer treeMapIndexer;

    public IndexedTreeMap(Comparator comparator, TreeMapIndexer<K, I> indexer) {
        this.internal = new TreeMap<K, Object>(comparator);
        this.keyIndex = new HashMap<I, K>();
        this.treeMapIndexer = indexer;
    }

    public boolean containsValue(Object o) {
        return internal.containsValue(o);
    }

    public Object get(Object o) {
        return internal.get(o);
    }

    public Comparator<? super K> comparator() {
        return internal.comparator();
    }

    public K firstKey() {
        return internal.firstKey();
    }

    public K lastKey() {
        return internal.lastKey();
    }

    public void putAll(Map<? extends K, ? extends Object> map) {
        internal.putAll(map);
    }

    public Object put(K k, Object o) {
        if(treeMapIndexer != null) {
            treeMapIndexer.index(k, keyIndex);
        }
        return internal.put(k, o);
    }

    public Object remove(Object o) {
        if(treeMapIndexer != null) {
            treeMapIndexer.remove(o, keyIndex);
        }
        return internal.remove(o);
    }

    public void clear() {
        internal.clear();
        keyIndex.clear();
    }

    public Object clone() {
        return internal.clone();
    }

    public Set<K> keySet() {
        return internal.keySet();
    }

    public Collection<Object> values() {
        return internal.values();
    }

    public Set<Entry<K, Object>> entrySet() {
        return internal.entrySet();
    }

    public SortedMap<K, Object> subMap(K k, K k1) {
        return internal.subMap(k, k1);
    }

    public SortedMap<K, Object> headMap(K k) {
        return internal.headMap(k);
    }

    public SortedMap<K, Object> tailMap(K k) {
        return internal.tailMap(k);
    }

    public boolean isEmpty() {
        return internal.isEmpty();
    }

    public boolean equals(Object o) {
        return internal.equals(o);
    }

    public int hashCode() {
        return internal.hashCode();
    }

    public String toString() {
        return internal.toString();
    }

    public int size() {
        return internal.size();
    }

    public boolean containsKey(Object o) {
        return internal.containsKey(o);
    }

    public Object getByIndex(I i) {
        return get(keyIndex.get(i));
    }

    public boolean containsKeyByIndex(I i) {
        return containsKey(keyIndex.get(i));
    }

    public Object removeByIndex(I i) {
        return remove(keyIndex.get(i));
    }

}
