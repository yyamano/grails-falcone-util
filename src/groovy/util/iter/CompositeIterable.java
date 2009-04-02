package util.iter;

import java.util.Iterator;

/**
 * This class takes a bunch of classes of the same type and iterates over them as a whole.  It's designed to
 * be implemented inline, where each implementation will provide a method for returning an iterable from the
 * specified type from the wrapped iterable.
 *
 * @author eric
 */
public abstract class CompositeIterable<X, P> implements Iterable<X> {
// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    private final Iterable<P> setOfHasIterables;

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    public CompositeIterable(Iterable<P> setOfHasIterables) {
        this.setOfHasIterables = setOfHasIterables;
    }

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    public Iterator<X> iterator() {
        return new Iterator<X>() {
            Iterator<? extends P> currIter = setOfHasIterables.iterator();
            Iterator<X> currItem;
            X next;

            public boolean hasNext() {
                setNextItem();
                return next != null;
            }

            public void setNextItem() {
                setNextIter();
                if (next != null) {
                    return;
                }
                if (currItem.hasNext()) {
                    next = currItem.next();
                }
            }

            public void setNextIter() {
                if(next != null) {
                    return;
                }
                if (currItem == null || !currItem.hasNext()) {
                    if (currIter.hasNext()) {
                        P p = currIter.next();
                        Iterable<X> iterable = getIterable(p);
                        if (iterable != null) {
                            currItem = iterable.iterator();
                            if (!currItem.hasNext()) {
                                setNextIter();
                            }
                        }
                    }
                }
            }

            public X next() {
                X tmp = next;
                next = null;
                return tmp;
            }

            public void remove() {
            }
        };
    }

// ========================================================================================================================
//    Abstract Methods
// ========================================================================================================================

    public abstract Iterable<X> getIterable(P p);
}
