package com.infusion.util.event;

/**
 * A delegate is passed into the eventBroker as an event, but defers the creation of the
 * event object until the broker knows that there are people that actually care about the
 * event.  This could be used when:
 *
 * <ul>
 * <li>The creation of the event object is expensive</li>
 * <li>There is work needed to transform one event to another form and you don't want to spend cycles when there's
 * no one that really cares</li>
 * </ul>
 */
public interface EventDelegate {
// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    /**
     * Returns the actual event
     * @return
     */
    public Object getEvent(EventBroker eventBroker);
}
