package com.infusion.util.factory;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Dec 18, 2008
 * Time: 8:59:03 PM
 */
public interface GenericKeyedFactory<K, T> extends GenericFactory<T> {
    public T create(K k);
}
