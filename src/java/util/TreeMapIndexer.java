package util;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Mar 18, 2009
 * Time: 2:03:53 PM
 */
public interface TreeMapIndexer<K, I> {
    public void index(K key, Map<I, K> index);
    public void remove(K key, Map<I, K> index);
}
