package com.infusion.util.event;

/**
 * A class that will handle an event of a particular type.  The Type parameter E is the type
 * of event this record will service.
 *
 * @author eric
 */
public interface EventConsumer<E> {
// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    /**
     * This method will perform the work needed on the event, if any.  Because we can have a heirarchy of
     * events, it also passes in the event class that is calling this method.
     *
     * @param event The event that was fired
     * @param eventBroker The class that defined this handler
     */
    public void consume(E event, EventBroker eventBroker);
}
