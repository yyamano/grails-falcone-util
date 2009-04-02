package com.infusion.util;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Dec 17, 2008
 * Time: 9:55:52 PM
 */
public interface Proxy<S,T> {
    public T get(S s);
}
