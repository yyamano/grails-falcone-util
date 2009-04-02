package com.infusion.util;


import static com.infusion.util.StringUtils.Nz;
import com.infusion.util.data.filter.DataFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Static functions useful for dealing with collections.
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: May 22, 2003
 * Time: 6:01:33 PM
 *
 */
public class CollectionUtil {
// ========================================================================================================================
//    Static Methods
// ========================================================================================================================

    /**
     * Converts an array to a collection
     *
     * @param list Can be null (will return blank HashSet)
     * @return Collection
     */
    public static <X> Set<X> ArrayToCol(X[] list) {
        if (list == null) return new HashSet<X>();
        LinkedHashSet<X> set = new LinkedHashSet<X>();
        for (X aList : list) {
            set.add(aList);
        }
        return set;
    }

    public static <X> Integer sum(Proxy<X, Integer> proxy, X ... set) {
        int sum = 0;
        for (X x : set) {
            sum += proxy.get(x);
        }
        return sum;
    }

    public static void test() {
        Integer i = 4;
        Integer b = 2;
        Integer c = 7;

        sum(new Proxy<Integer, Integer>() {
            public Integer get(Integer integer) {
                return integer;
            }
        }, i, b, c);

        String s = "Eric";
        String m = "Martineau";

        sum(new Proxy<String, Integer>() {
            public Integer get(String s) {
                return s.length();
            }
        }, "Eric", "Martineau", "John");
    }




    /**
     * Reverses a map, but maintains a list of keys associated with values, instead of
     * maintainting only one value per key
     * @param map
     * @return
     */
    public static <K, V> Map<V, Set<K>> ReverseMapWithList(Map<K, V> map) {
        Map<V, Set<K>> rtn = new HashMap<V, Set<K>>();
        for (Map.Entry<K, V> kvEntry : map.entrySet()) {
            final V value = kvEntry.getValue();
            if (!rtn.containsKey(value)) {
                rtn.put(value, new HashSet<K>());
            }

            rtn.get(value).add(kvEntry.getKey());
        }
        return rtn;
    }

    /**
     * Converts an array to a map, using each element as both the
     * key and value
     *
     * @return Map
     */
    public static <X> Map<X, X> ArrayToMap(X[] ary) {
        Map<X, X> tmpMap = new LinkedHashMap<X, X>();
        if (ary == null) return tmpMap;
        for (X anAry : ary) {
            if (anAry != null) {
                tmpMap.put(anAry, anAry);
            }
        }
        return tmpMap;
    }

    /**
     * Converts an array to a map
     *
     * @param args The array to convert
     * @param s    The delimiter to split on
     * @return The map
     */
    public static Map<String, String> ArrayToMap(String[] args, String s) {
        if (args == null) {
            return null;
        }
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String s1 : args) {
            String[] parts = s1.split(s);
            map.put(parts[0], parts[1]);
        }
        return map;
    }

    /**
     * Prints out the values of an array, separated by commas
     *
     * @return Array Values
     */
    public static String ArrayToString(Object[] values) {
        return ArrayToString(values, ",");
    }

    /**
     * Prints out the values of an array, using a specified delimited
     *
     * @param values Array to be printed
     * @param del    Delimiter to use
     * @return String
     */
    public static String ArrayToString(Object[] values, String del) {
        if (values == null)
            return null;

        else {
            StringBuffer tmp = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                tmp.append(values[i]);
                if (i < (values.length - 1)) tmp.append(del);
            }
            return tmp.toString();
        }
    }

    /**
     * Creates a map from a collection, using each element as the key and value
     *
     * @param c Collection to be converted
     * @return Map
     */
    public static <X> Map<X, X> CollectionToMap(Collection<X> c) {
        if (c == null) {
            return null;
        }
        Map<X, X> tmpMap = new LinkedHashMap<X, X>();
        for (X x : c) {
            tmpMap.put(x, x);
        }
        return tmpMap;
    }

    /**
     * Prints out the values of a collection
     *
     * @param c            Collection to print
     * @param delimiter    What to use as a delimiter
     * @param includeEmpty whether or not to include empty items
     * @return Collection values
     */
    public static String CollectionValues(Collection c, String delimiter, boolean includeEmpty) {
        if (c == null) return "";
        StringBuffer tmpString = new StringBuffer();
        delimiter = StringUtils.Nz(delimiter, ",");

        for (Object aC : c) {
            String next = "" + aC;
            if (includeEmpty || StringUtils.is(next)) {
                tmpString.append(next).append(delimiter);
            }
        }

        return StringUtils.rtrim(tmpString.toString(), delimiter.length());
    }


    public static <X> X[] CollectionToArray(Collection<X> collection, Class<X> clazz) {
        if (collection == null) {
            return null;
        }

        return collection.toArray((X[]) Array.newInstance(clazz, collection.size()));
    }

    public static <X> X[] CollectionToArray(Collection collection) {
        return (X[]) CollectionToArray(collection, Object.class);
    }

    /**
     * Produces a shallow copy of a map (will not clone objects inside the map)
     *
     * @return Copy of the map
     */
    public static <K, V> Map<K, V> CopyMap(Map<K, V> toCopy) {
        Map<K, V> tmp = new LinkedHashMap<K, V>();
        if (toCopy == null) return tmp;
        for (Object o : toCopy.entrySet()) {
            Map.Entry<K, V> e = (Map.Entry<K, V>) o;
            tmp.put(e.getKey(), e.getValue());
        }
        return tmp;
    }

    /**
     * Makes a copy of a given map, but only for the specified keys
     *
     * @return Copied map
     */
    public static <K, V> Map<K, V> CopyMapValuesFromColKeys(Map<K, V> orig, Collection<K> keys) {
        if (orig == null || keys == null) {
            return null;
        }
        Map<K, V> tmpMap = new LinkedHashMap<K, V>();
        for (K val : keys) {
            tmpMap.put(val, orig.get(val));
        }
        return tmpMap;
    }

    /**
     * Produces a shallow copy of a map (will not clone objects inside the map)
     *
     * @return Copy of the map
     */
    public static Map<String, String> CopyMapToStringString(Map<?, ?> toCopy) {
        Map<String, String> tmp = new LinkedHashMap<String, String>();
        if (toCopy == null) return tmp;
        for (Object o : toCopy.entrySet()) {
            Map.Entry<String, String> e = (Map.Entry<String, String>) o;
            tmp.put(e.getKey(), e.getValue());
        }
        return tmp;
    }

    /**
     * Constructs a map from duples.
     */
    public static <K, V> Map<K, V> DupleMap(Duple<K, V>... duples) {
        Map<K, V> ret = new LinkedHashMap<K, V>();
        for (Duple<K, V> duple : duples) {
            ret.put(duple.f1, duple.f2);
        }
        return ret;
    }

    /**
     * Converts an array of enums to a map of options for a form. Requires you to override the toString method unless
     * you want to use the enum names for both the key and value pair in the map. Will also spacify the display string.
     * Please use Pascal code format for enums.
     */
    public static Map<String, String> EnumMap(Enum... enums) {
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (Enum anEnum : enums) {
            values.put(anEnum.name(), StringUtils.spacify(anEnum.toString()));
        }
        return values;
    }

    /**
     * This method is used to round robin entries by ratio, evenly spreading items until ratios run out.  For example,
     * if I have the following ratios:
     * <p/>
     * A: 2<BR>
     * B: 5<BR>
     * C: 7<BR>
     * <p/>
     * The method would return the follwoing list:
     * <p/>
     * A,B,C,A,B,C,B,C,B,C,B,C,C,C
     *
     * @see #SpreadOut(Map,int)
     */
    public static <X> List<X> HandOut(Map<X, Integer> ratios) {
        if (ratios == null) {
            return null;
        }
        int i = 0;
        int sum = CollectionUtil.SumInteger(ratios.values());
        List<X> items = new ArrayList<X>(sum);

        while (true) {
            i++;
            boolean found = false;
            for (Map.Entry<X, Integer> entry : ratios.entrySet()) {
                if (entry.getValue() >= i) {
                    items.add(entry.getKey());
                    found = true;
                }
            }
            if (!found) {
                break;
            }
        }
        return items;
    }

    /**
     * Performs an array join based on a certain index.
     *
     * @param parts     The array to join
     * @param start     The index to start at
     * @param delimiter The delimited
     */
    public static String Join(String[] parts, int start, String delimiter) {
        if (parts == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        for (int i = start; i < parts.length; i++) {
            ret.append(parts[i]);
            if (i < parts.length - 1) {
                ret.append(delimiter);
            }
        }
        return ret.toString();
    }

    /**
     * Prints out the key values of a map, separated by commas
     *
     * @return Values
     */
    public static String KeyValues(Map map) {
        return CollectionValues(map.keySet(), ",", false);
    }

    /**
     * Returns the last item in a collection.  Uses indexes if it's a list, otherwise, iterates to the last one.
     */
    public static <X> X LastItem(Collection<X> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        if (items instanceof List) {
            List<X> itemsL = (List<X>) items;
            return itemsL.get(itemsL.size() - 1);
        } else {
            X item = null;
            for (X x : items) {
                item = x;
            }
            return item;
        }
    }

    /**
     * Will dive into a map as deep as it can go.  When it hits a wall, will return null;
     */
    public static Object MapDiver(Map diveInto, Object... keys) {
        Object _value = diveInto;
        int i = 0;
        while (_value != null && i < keys.length) {
            Object key = keys[i];
            _value = ((Map) _value).get(key);
            if (!(_value instanceof Map) || _value == null) {
                return _value;
            }
            i++;
        }
        return _value;
    }

    /**
     * Creates a map from a request (out of parameters only)
     *
     * @return Map
     */
    public static Map<String, String> MapFromRequest(HttpServletRequest request) {
        Map<String, String> m = new LinkedHashMap<String, String>();
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            m.put(key, StringUtils.Nz(StringUtils.printArray(request.getParameterValues(key), ",", false)));
        }
        return m;
    }

    /**
     * Puts the values from the request into a given map
     *
     * @param map Map to append values to
     */
    public static void MapFromRequest(Map<String, String> map, HttpServletRequest request) {
        map.putAll(MapFromRequest(request));
    }

    /**
     * Converts a map into a string
     *
     * @param map               THe map to convert
     * @param entrySeparator    What to separate map entries with
     * @param keyValueSeparator What to separate key/value pairs with
     * @return String
     */
    public static String MapToString(Map map, String entrySeparator, String keyValueSeparator) {
        return MapToString(map, entrySeparator, keyValueSeparator, false);
    }

    /**
     * Converts a map into a string
     *
     * @param map               THe map to convert
     * @param entrySeparator    What to separate map entries with
     * @param keyValueSeparator What to separate key/value pairs with
     * @param ignoreNulls       Whether or not to ignore null values
     * @return String
     */
    public static String MapToString(Map map, String entrySeparator, String keyValueSeparator, boolean ignoreNulls) {
        if (map == null) {
            return null;
        }
        StringBuffer tmp = new StringBuffer();
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            if (ignoreNulls || entry.getValue() != null) {
                tmp.append(entry.getKey())
                        .append(keyValueSeparator)
                        .append(entry.getValue())
                        .append(entrySeparator);
            }
        }
        return tmp.toString();
    }

    /**
     * Moves an item in a list to another location in a list.
     *
     * @param orig The original list
     * @param from The index to move from
     * @param to   The index to move to
     * @return A copy of the original list with the items in the correct order
     */
    public static <X> List<X> MoveItem(List<X> orig, int from, int to) {
        if (orig == null) {
            return null;
        }
        List<X> ret = new ArrayList<X>();
        X fromItem = orig.get(from);
        for (int i = 0; i < orig.size(); i++) {
            if (i != from) {
                if (i == to && from > to) {
                    ret.add(fromItem);
                }
                ret.add(orig.get(i));
                if (i == to && from < to) {
                    ret.add(fromItem);
                }
            }
        }
        return ret;
    }

    /**
     * Creates a number map starting with a given number with specified size. Starts at the passed in number minus 1 up
     * to the start number minus 1 + the size.
     *
     * @param startNumber The number to start with
     * @param size        THe size of the map
     * @return The map
     */
    public static Map<String, String> NumberMap(int startNumber, int size) {
        Map<String, String> returnMap = new LinkedHashMap<String, String>(size);
        startNumber--;
        for (int i = 0; i < size; i++) {
            String startStr = "" + (startNumber++);
            returnMap.put(startStr, startStr);
        }
        return returnMap;
    }

    /**
     * Creates a map of numbers from start to finish
     *
     * @param start  What number to start with
     * @param end    WHat number to end with
     * @param prefix What to prefix the value with
     * @return The map
     */
    public static Map<String, String> NumberMap(int start, int end, String prefix) {
        Map<String, String> returnMap = new LinkedHashMap<String, String>(end - start);
        for (int i = start; i <= end; i++) {
            returnMap.put("" + i, StringUtils.Nz(prefix) + i);
        }
        return returnMap;
    }

    public static Map<String, String> ParseProperties(String str) {
        String[] parts = str.split("\\r?\\n");
        Map<String, String> ret = new LinkedHashMap<String, String>();
        for (String part : parts) {
            String[] kv = part.split(":");
            String val = kv.length > 1 ? kv[1] : "";
            ret.put(kv[0], val);
        }
        return ret;
    }

    /**
     * Converts a map to a string
     *
     * @return The printed out string
     */
    public static String PrintOutMap(Map toConvert) {
        return MapToString(toConvert, "\n", "=");
    }

    /**
     * Converts properties into Map<String, String>
     */

    public static Map<String, String> PropToMap(Properties properties) {
        Map<String, String> ret = new LinkedHashMap<String, String>(properties.size());
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            ret.put((String) entry.getKey(), (String) entry.getValue());
        }
        return ret;
    }

    /**
     * Creates an inline List of objects
     *
     * @param object Varargs list of object to put into list
     * @return List of objects
     */
    public static <X> List<X> QuickList(X... object) {
        List<X> list = new ArrayList<X>();
        if (object == null) return list;
        for (X x : object) {
            list.add(x);
        }
        return list;
    }

    /**
     * Creates a quick map of objects, each item is both the key and the value
     * 3 or more parameters must be passed in, otherwise QuickMap(X obj) or QuickMap(K key, V value)
     * will be called instead
     *
     * @param obj Varargs entries
     * @return The resulting map
     */
    public static <X> Map<X, X> QuickMap(X... obj) {
        return QuickMap(null, obj);
    }



    /**
     * Creates a quick map of objects, each item is both the key and the value
     * 3 or more parameters must be passed in, otherwise QuickMap(X obj) or QuickMap(K key, V value)
     * will be called instead
     *
     * @param obj Varargs entries
     * @return The resulting map
     */
    public static <X> Map<X, X> QuickMap(DataFilter<X> filter, X... obj) {
        Map<X, X> map = new LinkedHashMap<X, X>(obj.length);
        for (X x : obj) {
            if (filter != null) {
                if (!filter.filter(x)) {
                    continue;
                }
            }
            map.put(x, x);
        }
        return map;
    }

    /**
     * Creates a quick map with one item in it (key and value are the same)
     *
     * @param obj Key and value in the map
     * @return Map with single object
     */
    public static <X> Map<X, X> QuickMap(X obj) {
        Map<X, X> map = new LinkedHashMap<X, X>(1);
        map.put(obj, obj);
        return map;
    }

    /**
     * Creates a quick map with one item in it
     *
     * @param key   Key in the map
     * @param value Value in the map
     * @return Map
     */
    public static <K, V> Map<K, V> QuickMap(K key, V value) {
        Map<K, V> map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }

    /**
     * Creates a map from key value pairs, every other one is the key.
     */
    public static <X> Map<X, X> QuickMapPair(X... kv) {
        Map<X, X> ret = new LinkedHashMap<X, X>();
        for (int i = 0; i < kv.length; i++) {
            X key = kv[i];
            i++;
            X value = null;
            if (i >= kv.length) {
                value = kv[i - 1];
            } else {
                value = kv[i];
            }
            ret.put(key, value);
        }
        return ret;
    }

    /**
     * Creates a quick map with two items in it. Each item is both the key and the value.  This method was
     * created to override QuickMap(K key, V value). What happened previously was this:
     * When you tried to use QuickMap(X ... Obj) to create a map with just 2 entries,
     * QuickMap(K key, V value) was called instead.  This method fixes that by allowing two values to be passed
     * in instead of a key and a value.
     *
     * @param value1 Key in the map
     * @param value2 Value in the map
     * @return Map
     */
    public static <V> Map<V, V> QuickMapTwo(V value1, V value2) {
        Map<V, V> map = new HashMap<V, V>();
        map.put(value1, value1);
        map.put(value2, value2);
        return map;
    }

    /**
     * Creates an inline Set of objects
     *
     * @param object Varargs list of object to put into set
     * @return List of objects
     */
    public static <X> Set<X> QuickSet(X... object) {
        Set<X> set = new LinkedHashSet<X>();
        for (X x : object) {
            set.add(x);
        }
        return set;
    }

    /**
     * Takes a passed in map and adds a key, value set. This is just a convenience method to put a new object in a map
     * all on the same line of code.
     */
    public static <K, V> Map<K, V> QuikMapPut(Map<K, V> map, K key, V value) {
        map.put(key, value);
        return map;
    }

    /**
     * Creates a copy of a map, and then removes the specified keys
     *
     * @param removeFrom   The map you are removing from
     * @param whatToRemove The keys to remove
     * @return Map with keys stripped out
     */
    public static <K, V> Map<K, V> RemoveFromMap(Map<K, V> removeFrom, Collection<K> whatToRemove) {
        Map<K, V> tmp = CopyMap(removeFrom);
        for (K aWhatToRemove : whatToRemove) {
            tmp.remove(aWhatToRemove);
        }
        return tmp;
    }

    /**
     * Copies a map and then removes all items given the keyset from another map
     *
     * @param removeFrom   the map you are removing items from
     * @param whatToRemove Will return all matching keys in the map passed in
     * @return The copies map with items removed
     */
    public static <K, V> Map<K, V> RemoveFromMap(Map<K, V> removeFrom, Map<K, V> whatToRemove) {
        Map<K, V> tmp = CopyMap(removeFrom);
        for (K o : whatToRemove.keySet()) {
            tmp.remove(o);
        }
        return tmp;
    }




    /**
     * Returns a reversed version of the map
     */
    public static <K, V> Map<K, V> ReverseMap(Map<V, K> map) {
        Map<K, V> reversed = new LinkedHashMap<K, V>();
        for (Map.Entry<V, K> entry : map.entrySet()) {
            reversed.put(entry.getValue(), entry.getKey());
        }
        return reversed;
    }

    /**
     * reverse the order of a map
     * @param map map
     * @return reversed order map
     */
    public static <K, V> Map<K, V> ReverseMapOrder(Map<K, V> map) {
        List<K> keys = new ArrayList<K>(map.keySet());
        Map<K, V> reversed = new LinkedHashMap<K, V>();
        for (ListIterator<K> iterator = keys.listIterator(keys.size()); iterator.hasPrevious();) {
            K k = iterator.previous();
            reversed.put(k, map.get(k));
        }
        return reversed;
    }

    /**
     * reverse the order of a set
     * @param set set
     * @return reversed order set
     */
    public static <V> Set<V> ReverseSetOrder(Set<V> set) {
        List<V> values = new ArrayList<V>(set);
        Set<V> reversed = new LinkedHashSet<V>();
        for (ListIterator<V> iterator = values.listIterator(values.size()); iterator.hasPrevious();) {
            V v = iterator.previous();
            reversed.add(v);
        }
        return reversed;
    }

    /**
     * Returns a collection with a single item in it
     *
     * @see #QuickSet(Object...)
     */
    public static <X> Collection<X> Set(X object) {
        if (object == null) {
            return null;
        } else {
            return QuickSet(object);
        }
    }

    /**
     * Creates a map from a set, using each element in the set as both the key and value in the map
     *
     * @param options The set containing the options
     * @return Set converted to map
     */
    public static Map<String, String> SetToMap(Set<String> options) {
        if (options == null) return new HashMap<String, String>();
        Map<String, String> tmp = new LinkedHashMap<String, String>(options.size());
        for (String s : options) {
            if (StringUtils.is(s)) {
                tmp.put(s, s);
            }
        }
        return tmp;
    }

    /**
     * Prints out the values of a set, using a specified delimited
     *
     * @param set Set to be printed
     * @param del Delimiter to use
     * @return String
     */
    public static String SetToString(Set set, String del) {
        return CollectionValues(set, del, false);
    }

    /**
     * Splits up a string using the most common separators
     *
     * @return a List of Strings
     */
    public static List<String> SmartSplit(String toSplit) {
        return SmartSplit(toSplit, new String[]{";", ",", ":", "\r\n", "\n\r", "\n", "\t", " ", "\\s"});
    }

    /**
     * Splits up a string using a specified list of separators
     *
     * @return a List of Strings
     */
    public static List<String> SmartSplit(String toSplit, String[] splitters) {
        if (toSplit == null || splitters == null) {
            return null;
        }
        List<String> returnVal = new ArrayList<String>();
        StringBuffer reg = new StringBuffer();
        reg.append("(");
        for (String splitter : splitters) {
            reg.append(splitter).append("|");
        }
        String regS = StringUtils.chop(reg.toString()) + ")";


        String[] split = toSplit.split(regS, 500);
        for (String s : split) {
            if (StringUtils.is(s)) {
                returnVal.add(s);
            }
        }
        return returnVal;
    }

    /**
     * This method will round robin items, keeping in line with ratios. For example,
     * if I have the following ratios:
     * <p/>
     * A: 2<BR>
     * B: 4<BR>
     * C: 6<BR>
     * <p/>
     * The method would return the follwoing list:
     * <p/>
     * <p/>
     * C,C,C,B,B,A,C,C,C,B,B,A
     *
     * @param total The number of items to return in the list
     */
    public static <X> List<X> SpreadOut(Map<X, Integer> ratios, int total) {
        CountMap<X> map = new CountMap<X>();
        List<X> ret = new ArrayList<X>();
        for (int i = 0; i < total; i++) {
            int min = Integer.MAX_VALUE;
            X minX = null;

            for (X x : ratios.keySet()) {
                int currRatio = map.getRatio(x);
                int diff = NumberUtils.getPercentDiff(currRatio, ratios.get(x));
                if (diff < min) {
                    minX = x;
                    min = diff;
                }
            }

            map.add(minX);
            ret.add(minX);
        }

        return ret;
    }

    /**
     * Converts a String to a Collection
     *
     * @return Collection of Strings
     * @see #StringToSet(String,String)
     */
    public static Collection<String> StringToCol(String string, String del) {
        return StringToSet(string, del);
    }

    /**
     * Creates a set of Integers from a comma-separated String
     *
     * @return Set of Integers
     */
    public static Set<Integer> StringToIntSet(String parameter) {
        return StringToIntSet(parameter, ",");
    }

    /**
     * Creates a set of Integers from a comma-separated String
     *
     * @return Set of Integers
     */
    public static Set<Integer> StringToIntSet(String parameter, String separator) {
        Set<String> strings = StringToSet(parameter, separator);
        Set<Integer> ret = new LinkedHashSet<Integer>();
        for (String s : strings) {
            ret.add(NumberUtils.parseInt(s));
        }
        return ret;
    }

    /**
     * Creates a list of strings. Defaults the delimiter to a comma.
     *
     * @return List of Strings
     * @see @StringToList
     */
    public static List<String> StringToList(String str) {
        return StringToList(str, ",");
    }

    /**
     * Creates a list of Strings from a String
     *
     * @return List of Strings
     */
    public static List<String> StringToList(String string, String del) {
        if (string == null) return new ArrayList<String>();
        List<String> tmp = new ArrayList<String>();
        String[] vals = string.split(del);
        for (String val : vals) {
            tmp.add(Nz(val).trim());
        }
        return tmp;
    }

    /**
     * Converts a map to a String, using request-like separators as a default:
     * name=Eric&phone=3329993&email=me@mymail.com
     *
     * @param s String to be parsed
     * @return Map of Strings
     */
    public static Map<String, String> StringToMap(String s) {
        return StringToMap(s, "&", "=");
    }

    /**
     * Converts a key-only String into a map
     *
     * @param del What the keys are separated by
     * @return A Map of Strings
     */
    public static Map<String, String> StringToMap(String string, String del) {
        if (string == null || del == null) return new LinkedHashMap<String, String>();
        Map<String, String> tmp = new LinkedHashMap<String, String>();
        String[] vals = string.split(del);
        for (String val : vals) {
            tmp.put(val.trim(), val.trim());
        }
        return tmp;
    }

    /**
     * Converts a String into a Map
     *
     * @param orig              The String to be parsed
     * @param entrySeparator    What the entries in the map are separated by
     * @param keyValueSeparator What the key/value pairs are separated by
     * @return A Map
     */
    public static Map<String, String> StringToMap(String orig, String entrySeparator, String keyValueSeparator) {
        Map<String, String> tmp = new LinkedHashMap<String, String>();
        if (orig == null || entrySeparator == null || keyValueSeparator == null) return null;
        String[] sets = orig.split(entrySeparator);
        for (String set : sets) {
            String[] keyValue = set.split(keyValueSeparator);
            if (keyValue.length > 0) {
                if (keyValue.length > 1) {
                    tmp.put(keyValue[0], keyValue[1]);
                } else {
                    tmp.put(keyValue[0], "");
                }
            }
        }
        return tmp;
    }

    /**
     * Splits a string into a map, using the most common separators
     *
     * @return The Map of Strings
     */
    public static Map<String, String> StringToMapSmart(String string) {
        if (string == null) return new LinkedHashMap<String, String>();
        Map<String, String> tmp = new LinkedHashMap<String, String>();
        String[] vals = string.split(",|\n|\r\n");
        for (String val : vals) {
            if (StringUtils.is(val)) {
                tmp.put(val.trim(), val.trim());
            }
        }
        return tmp;
    }

    /**
     * Assumes comma as a separator
     *
     * @see #StringToSet(String,String)
     */
    public static Set<String> StringToSet(String data) {
        return StringToSet(data, ",");
    }

    /**
     * Converts a String into a Set of Strings
     *
     * @param del the delimiter
     * @return Set of Strings
     */
    public static Set<String> StringToSet(String string, String del) {
        if (string == null || del == null) return new LinkedHashSet<String>();
        Set<String> tmp = new LinkedHashSet<String>();
        String[] vals = string.split(del);
        for (String val : vals) {
            if (StringUtils.is(val)) {
                tmp.add(val.trim());
            }
        }
        return tmp;
    }

    /**
     * Takes in a set of integers and returns their sum.
     */
    public static int SumInteger(Collection<Integer> ints) {
        int t = 0;
        for (Integer anInt : ints) {
            t += anInt;
        }
        return t;
    }

    /**
     * Converts map of any type to Map<String, String>
     */
    public static Map<String, String> ToStringStringMap(Map<? extends Object, ? extends Object> map) {
        Map<String, String> ret = new LinkedHashMap<String, String>();
        for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
            ret.put("" + entry.getKey(), "" + entry.getValue());
        }
        return ret;
    }

    /**
     * Determines if an enum is in an array of items.  Useful with varargs
     */
    public static <X extends Enum> boolean containsEnum(X[] set, X item) {
        for (int i = 0; i < set.length; i++) {
            X x = set[i];
            if (x == item) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reads all lines from a file into a list of Strings. Returns an empty list of there are problems
     *
     * @param fileName to read from
     * @return List of Strings
     */
    public static List<String> listFromFile(String fileName) {
        List<String> list = new ArrayList<String>();
        if (fileName == null){
            return list;
        }
        BufferedReader r;
        try {
            r = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            return list;
        }

        try {
            while (r.ready()) {
                final String line = r.readLine();
                if (line != null && line.length() > 0) list.add(line);
            }
        } catch (IOException e) {
            return list;
        }
        return list;
    }

    /**
     * Takes a list of strings and iterates through it putting it in a string separated by some specified separator.
     * This separator defaults to a new line if none is specified.
     *
     * @param strings   list of strings
     * @param separator string separator
     * @return new combined string
     */
    public static String listToString(List<String> strings, String separator) {
        if (strings != null) {
            String sep = separator == null ? "\n" : separator;
            Joiner joiner = new Joiner(null, sep, null);
            joiner.add(strings);
            return joiner.toString();
        }
        return null;
    }

    /**
     * Converts a collection of labels into a map (so they can be put into a select)
     *
     * @param labelItems
     * @return
     */
    public static Map<String, String> LabelsToMap(Iterable<? extends Labelable> labelItems) {
        Map<String, String> rtn = new LinkedHashMap<String, String>();
        if (labelItems != null) {
            for (Labelable labelItem : labelItems) {
                rtn.put(labelItem.getName(), labelItem.getLabel());
            }
        }
        return rtn;
    }

    /**
     * Only puts the value in the map if it is unique or if the current value is empty string or null.
     *
     * @param key name of companion link field
     * @param value             value of field in infusionsoft
     * @param map     companion link map
     */
    public static void putIfUnique(String key, String value, Map<String, String> map) {
        if (!map.containsKey(key) || (map.containsKey(key) && !StringUtils.is(map.get(key)))) {
            map.put(key, value);
        }
    }
}