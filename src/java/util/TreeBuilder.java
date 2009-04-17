package util;

import groovy.util.BuilderSupport;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Apr 7, 2009
 * Time: 10:40:06 AM
 */
public class TreeBuilder extends BuilderSupport {

    private Map<String, Integer> order = new HashMap();
    private Map<String, Set<String>> parents = new LinkedHashMap();
    private String root;

    protected void setParent(Object o, Object o1) {
        if (!parents.containsKey(o)) {
            parents.put((String) o, new LinkedHashSet<String>());
        }
        parents.get(o).add((String) o1);
    }

    protected Object createNode(Object o) {
        if (root == null) {
            root = (String) o;
        }
        return o;
    }

    protected Object createNode(Object o, Object o1) {
        return o1;
    }

    protected Object createNode(Object o, Map map) {
        if (map.containsKey("order")) {
            this.order.put((String) map.get("name"), (Integer) map.get("order"));
        }
        System.out.println("createNode: " + o + ":" + map);
        return map.get("name");
    }

    protected Object createNode(Object o, Map map, Object o1) {
        return o;
    }

    public void appendTo(Tree tree) {
        getMaps(tree, root, new String[0]);
    }

    private void getMaps(Tree tree, String location, String[] path) {
        String[] fullPath = new String[path.length + 1];
        int count = 0;
        for (String item : path) {
            fullPath[count] = item;
            count++;
        }
        fullPath[count] = location;
        tree.getMap(fullPath);
        if (order.containsKey(location)) {
            tree.setOrder(order.get(location), fullPath);
        }

        Set<String> children = parents.get(location);
        if (children != null) {
            for (String child : children) {
                getMaps(tree, child, fullPath);
            }
        }
    }
}
