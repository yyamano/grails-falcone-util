package util;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Nov 21, 2008
 * Time: 12:08:06 PM
 */
public class TreeList {

    List list = new ArrayList();

    public TreeList() {
    }

    public List getTree() {
        return list;
    }

    private void addSafe(List addTo, Object toAdd, int index) {
        for (int i = addTo.size(); i <= index; i++) {
            addTo.add(i, null);
        }
        addTo.set(index, toAdd);
    }

    public void addItem(Object data, int... location) {
        final List list1 = getList(chop(location));
        addSafe(list1, data, location[location.length - 1]);
    }

    public Object getItem(int... location) {
        final List list = getList(chop(location));
        return list.get(location[location.length - 1]);
    }

    private List getList(int ... locations) {
        List currentList = list;
        for (int location : locations) {

            if (currentList.size() <= location) {
                addSafe(currentList, new ArrayList(), location);
            }

            currentList = (List) currentList.get(location);

        }
        assert currentList != null;
        return currentList;
    }

    public int[] chop(int ... location) {
        int[] rtn = new int[location.length - 1];
        for (int i = 0; i < location.length - 1; i++) {
            rtn[i] = location[i];
        }
        return rtn;
    }

    public void addItem(Object data, String location) {
        addItem(data, stringToIntArray(location));
    }

    private int[] stringToIntArray(String str) {
        final String[] strings = str.split("\\.");
        int[] rtn = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            rtn[i] = Integer.parseInt(string);
        }
        return rtn;
    }
}
