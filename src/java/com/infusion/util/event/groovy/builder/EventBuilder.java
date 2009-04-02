package com.infusion.util.event.groovy.builder;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.Closure;
import com.infusion.util.event.*;
import com.infusion.util.event.groovy.EventUtils;
import com.infusion.util.event.groovy.GroovyChainedEventConsumer;

import java.util.List;
import java.util.Map;

/**
 * A dsl used for building {@link EventType} and {@link EventConsumer}.  You use it like this:
 * <p/>
 * <pre>
 * def e = new EventBuilder().events {
 * <p/>
 *     //This section contains eventType definitions (you can skip it)
 *     eventTypes {
 *         someEventName(SomeEventClass)
 *         anotherEventName(AnotherEventClass)
 *     }
 * <p/>
 *     //This section contains eventConsumer definitions
 *     eventConsumers {
 * <p/>
 *         //Everything inside this block will be a consumer for the "someEventName" event
 *         someEventName {
 * <p/>
 *             arbitraryHandlerName() {
 *                 SomeEventClass event, bus ->
 *                 event.doSomeStuff()
 *             }
 * <p/>
 *             //Log is an arbitrary name that tells the eventing system what you're subscribing for - makes debugging easier.
 *             log() {
 *                 SomeEventClass event, bus ->
 *                 println "Handled event ${event}"
 *             }
 * <p/>
 *         }
 * <p/>
 *     }
 * }
 * </pre>
 * <p/>
 * If you don't like the DSL, there are methods to do all the things described above.
 */
public class EventBuilder extends GroovyObjectSupport implements EventConfiguration {
// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    EventConsumerListBuilder eventConsumerListBuilder = new EventConsumerListBuilder();

    EventTypeListBuilder eventTypeListBuilder = new EventTypeListBuilder();

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    public EventBuilder addChainedEvent(String source, String target, Closure filter) throws EventException {
        eventConsumerListBuilder.eventConsumers.put(new GroovyChainedEventConsumer(source, target, filter), source);
        return this;
    }

    public EventBuilder addEventConsumer(String event, EventConsumer consumer) {
        eventConsumerListBuilder.eventConsumers.put(consumer, event);
        return this;
    }

    public EventBuilder addEventConsumer(String event, Closure closure) {
        final EventConsumer consumer = EventUtils.closured(closure);
        eventConsumerListBuilder.eventConsumers.put(consumer, event);
        return this;
    }

    public EventBuilder addEventType(EventType eventType) {
        eventTypeListBuilder.eventTypes.add(eventType);
        return this;
    }

    public Map<EventConsumer, String> getEventConsumers() {
        return eventConsumerListBuilder.eventConsumers;
    }

    public List<EventType> getEventTypes() {
        return eventTypeListBuilder.eventTypes;
    }

    @Override
    public Object invokeMethod(String s, Object o) {
        Object[] args = (Object[]) o;
        if ("events".equals(s)) {
            return invokeEventsClosure((Closure) args[0]);
        } else if ("eventTypes".equals(s)) {
            return invokeEventTypesClosure((Closure) args[0]);
        } else if ("eventConsumers".equals(s)) {
            return invokeEventConsumerClosure((Closure) args[0]);
        }
        return super.invokeMethod(s, o);
    }

// ========================================================================================================================
//    Non-Public Instance Methods
// ========================================================================================================================

    /**
     * Redirects method chaining to the eventConsumerBuilder instance.
     */
    private Object invokeEventConsumerClosure(Closure closure) {
        closure.setDelegate(eventConsumerListBuilder);
        closure.call();
        return eventConsumerListBuilder;
    }

    /**
     * Redirects method chaining to the eventConsumerBuilder instance.
     */
    private Object invokeEventTypesClosure(Closure closure) {
        closure.setDelegate(eventTypeListBuilder);
        closure.call();
        return eventTypeListBuilder;
    }

    /**
     * Dummy method - doesn't do anything, but provides a top-level container.
     */
    private Object invokeEventsClosure(Closure closure) {
        closure.setDelegate(this);
        closure.call();
        return this;
    }
}
