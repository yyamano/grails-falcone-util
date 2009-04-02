package com.infusion.util.event.chain;


/**
 * Transforms an event from one class to another.
 */
public interface EventTransformer<F, T> {
// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    /**
     * Performs the transformation.  
     */
    public T transform(F source);
}
