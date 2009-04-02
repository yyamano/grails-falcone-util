package com.infusion.util;


import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilities for dealing with maps.
 */

public abstract class MapUtils {
// ========================================================================================================================
//    Static Methods
// ========================================================================================================================

    /**
     * Inverts this filter (the keys become the values, and the values become the keys).
     */

    public static <T> Map<T, T> invert(Map<T, T> source) {
        return invert(source, new LinkedHashMap<T, T>());
    }

    /**
     * Inverts this filter (the keys become the values, and the values become the keys).  New values get put into target.
     */

    public static <T> Map<T, T> invert(Map<T, T> source, Map<T, T> target) {
        for (Map.Entry<T, T> entry : source.entrySet()) {
            target.put(entry.getValue(), entry.getKey());
        }

        return target;
    }

    public static Map toMap(Reader reader) throws IOException {
        BufferedReader r = new BufferedReader(reader);
        Map map;
        String line = r.readLine();
        map = new LinkedHashMap();
        while (line != null) {
            if (!"".equals(line)) {
                if (line.charAt(0) != '#') {
                    String[] piece = line.split(",");
                    if (piece.length == 1) {
                        map.put(piece[0], piece[0]);
                    } else if (piece.length > 1) {
                        map.put(piece[0], piece[1]);
                    } else if (piece.length == 0) {
                        throw new InternalError("Invalid syntax in file: " + line);
                    }
                }
            }
            line = r.readLine();
        }
        return map;
    }
}
