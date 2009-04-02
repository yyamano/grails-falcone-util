package com.infusion.util.event.filter;


/**
 * Class used to filter a certain type of event.
 */
public interface EventFilter<E> {
// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    /**
     * Examine an event object and return true if it matches the requirements of this filter.
     */
    public boolean filter(E e);
}
