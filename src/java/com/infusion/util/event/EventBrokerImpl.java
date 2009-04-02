package com.infusion.util.event;

import java.util.*;
import static java.text.MessageFormat.format;

import org.apache.log4j.Logger;

/**
 * The main service that deals with registering and locating consumers for events.
 * <p/>
 * You can "subscribe" to events, or "publish" events.
 *
 * @author eric
 */
public abstract class EventBrokerImpl implements EventBroker {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    protected static Logger log = Logger.getLogger(EventBrokerImpl.class.getName());

// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    /**
     * Listeners on a given event.  Event implementation should implement equals and hashcode properly
     * so they don't fire the same event twice (or register the same event twice)
     */
    protected Map<String, Set<EventConsumer>> consumers = Collections.synchronizedMap(new HashMap<String, Set<EventConsumer>>());

    /**
     * A list of event types that have been registered. If an event is fired that hasn't been registered
     */
    protected Map<String, EventType> eventTypes = Collections.synchronizedMap(new HashMap<String, EventType>());

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    public EventType getEventType(String eventName) {
        return getEventTypes().get(eventName);
    }

    public void publish(Object event) {
        publish(event.getClass().getSimpleName(), event);
    }

    public void publish(String eventName, Object event) {
        if (eventName == null) {
            eventName = event.getClass().getName(); //Casting solve problem?
            log.debug(format("No event name passed, using event class: {0}", eventName));
        }

        log.debug(format("Firing event {0}: {1}", eventName, event));
        Set<EventConsumer> eventConsumers = getConsumers().get(eventName);
        if (eventConsumers != null) {
            log.debug(format("Found {0} consumers for {1}", eventConsumers.size(), eventName));
            if (event instanceof EventDelegate) {
                event = ((EventDelegate) event).getEvent(this);
                log.debug(format("Event delegate encountered: returned {0}", event));
            }
            for (EventConsumer consumer : eventConsumers) {
                log.debug(format("Consumer {0} encountered", consumer));
                try {
                    consumer.consume(event, this);
                } catch (Throwable e) {
                    log.error(format("Error while consuming {0} for {1}", eventName, consumer), e);
                }
            }
        }
    }

    public void registerEventType(EventType eventType) {
        log.debug(format("Registered eventType: {0}", eventType));
        eventTypes.put(eventType.eventName, eventType);
    }

    public void removeEventType(EventType eventType) {
        log.debug(format("Removed eventType: {0}", eventType));
        eventTypes.remove(eventType.eventName);

        //When removing a type, also remove all listeners
        getConsumers().remove(eventType.eventName);
    }

    public void subscribe(String eventType, EventConsumer consumer) {
        log.debug(format("Registered a consumer for {0}: {1}", eventType, consumer));
        if (!getConsumers().containsKey(eventType)) {
            getConsumers().put(eventType, new LinkedHashSet<EventConsumer>());
        }
        getConsumers().get(eventType).add(consumer);
    }

    public boolean unsubscribe(String event, EventConsumer consumer) {
        log.debug(format("Consumer removed for {0}: {1}", event, consumer));
        final Set<EventConsumer> consumers = getConsumers().get(event);
        if (consumers != null) {
            return consumers.remove(consumer);
        }
        return false;
    }

    public void loadConfiguration(EventConfiguration eventConfiguration) {
        for (EventType eventType : eventConfiguration.getEventTypes()) {
            this.registerEventType(eventType);
        }

        for (Map.Entry<EventConsumer, String> consumerEntry : eventConfiguration.getEventConsumers().entrySet()) {
            this.subscribe(consumerEntry.getValue(), consumerEntry.getKey());
        }
    }

    public void removeConfiguration(EventConfiguration eventConfiguration) {
        for (EventType eventType : eventConfiguration.getEventTypes()) {
            this.removeEventType(eventType);
        }

        for (Map.Entry<EventConsumer, String> consumerEntry : eventConfiguration.getEventConsumers().entrySet()) {
            this.unsubscribe(consumerEntry.getValue(), consumerEntry.getKey());
        }
    }


// ========================================================================================================================
//    Non-Public Instance Methods
// ========================================================================================================================

    /**
     * Returns/creates all listeners
     */
    protected Map<String, Set<EventConsumer>> getConsumers() {
        return consumers;
    }

    /**
     * Returns all event types
     */
    protected Map<String, EventType> getEventTypes() {
        return eventTypes;
    }
}