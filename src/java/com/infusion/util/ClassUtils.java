package com.infusion.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Infusion Software<BR>
 * Date: Jul 10, 2006<BR>
 * Time: 3:16:24 PM<BR>
 *
 * @author eric
 */
public class ClassUtils {
// ========================================================================================================================
//    Static Methods
// ========================================================================================================================

    /**
     * Invokes a deep getter command, given the pattern to dive after and the original object.  If the following parameters are passed:
     * <p/>
     * deep = "class.name.class.name"<BR>
     * o = new Object()
     * <p/>
     * The result will be: java.lang.String="java.lang.String".  The method would have done the following operations:
     * new Object().getClass = java.lang.Object.CLASS<BR>
     * java.lang.Object.CLASS.getName() = "java.lang.Object"<BR>
     * "java.lang.Object".getClass() = java.lang.String.CLASS<BR>
     * java.lang.String.CLASS.getName() = "java.lang.String"<BR>
     *
     * @param deep The deep string (dot separated) to dive after
     * @param o    The original object
     */

    public static Object deepGetter(String deep, Object o) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String[] parts = deep.split("\\.");
        String first = parts[0];
        Method get = o.getClass().getMethod("get" + StringUtils.firstLetterUpper(first));
        Object got = get.invoke(o);

        if (deep.contains(".")) {
            String remain = CollectionUtil.Join(parts, 1, ".");
            return deepGetter(remain, got);
        } else {
            return got;
        }
    }
}
