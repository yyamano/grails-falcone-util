package com.infusion.util.event;

/**
 * Iterface for defining an event broker - an object to which people subscribe to events and publish events.
 */
public interface EventBroker {
// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    /**
     * Returns an eventType definition with a given name
     */
    EventType getEventType(String eventName);

    /**
     * Loads a bunch of config from an eventConfiguration instance
     *
     * @param eventConfiguration
     */
    void loadConfiguration(EventConfiguration eventConfiguration);

    /**
     * Public method - this is the entry point to fire an event.  This class will pick up top-level consumers for the event
     * and use the notifiers to notify lower level events.
     *
     * @param event THe event being thrown
     */
    void publish(Object event);

    /**
     * Internal method for calling an event, looks up notifiers and calls them appropriately.
     *
     * @param event     The original event being fired
     * @param eventName Which event class we are currently examining
     */
    void publish(String eventName, Object event);

    /**
     * Registers a new event type
     */
    void registerEventType(EventType eventType);

    /**
     * Removes a bunch of config from an eventConfiguration instance
     *
     * @param eventConfiguration
     */
    void removeConfiguration(EventConfiguration eventConfiguration);

    /**
     * Removes an event type definition.
     *
     * @param eventType
     */
    void removeEventType(EventType eventType);

    /**
     * This adds an item to the consumers map.
     *
     * @param eventType The event class this will be fired on
     * @param consumer  The services to handle that event (generics shuold enforce the contract)
     */
    void subscribe(String eventType, EventConsumer consumer);

    /**
     * Removes a consumer from listening ona given event.
     */
    boolean unsubscribe(String event, EventConsumer consumer);
}
