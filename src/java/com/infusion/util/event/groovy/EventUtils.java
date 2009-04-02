package com.infusion.util.event.groovy;

import groovy.lang.Closure;
import com.infusion.util.event.filter.FilteredEventConsumer;
import com.infusion.util.event.chain.ChainedEventConsumer;
import com.infusion.util.event.filter.EventFilter;
import com.infusion.util.event.EventConsumer;
import com.infusion.util.event.EventBroker;
import com.infusion.util.event.EventException;

/**
 * Convenience utilities for dealing with filtering and chaining event consumers
 */
public class EventUtils {
// ======================================================================================================================== 
//    Static Methods
// ========================================================================================================================

    public static EventConsumer chain(String source, String target, EventBroker broker) throws EventException {
        return new ChainedEventConsumer(broker.getEventType(source), broker.getEventType(target));
    }

    public static EventConsumer chainAndFilter(String source, String target, EventBroker broker, EventFilter filter) throws EventException {
        return filter(chain(source, target, broker), filter);
    }

    public static EventConsumer chainAndFilter(String source, String target, EventBroker broker, Closure closure) throws EventException {
        return filter(chain(source, target, broker), closure);
    }

    public static void chainAndRegister(String source, String target, EventBroker broker) throws EventException {
        broker.subscribe(source, chain(source, target, broker));
    }

    public static void chainFilterAndRegister(String source, String target, EventBroker broker, Closure closure) throws EventException {
        broker.subscribe(source, chainAndFilter(source, target, broker, closure));
    }

    public static EventConsumer closured(final Closure consumer) {
        return closured("Anonymous", consumer);
    }

    public static EventConsumer closured(final String name, final Closure consumer) {
        return new EventConsumer() {
            final String consumerName = name;
            public void consume(Object event, EventBroker eventBroker) {
                consumer.call(new Object[] {event, eventBroker});
            }

            public String toString() {
                return name;
            }
        };
    }

    public static EventConsumer filter(final Closure consumer, final Closure filter) throws EventException {
        return filter("Anonymous", consumer, filter);
    }

    public static EventConsumer filter(EventConsumer filtered, final Closure filter) throws EventException {
        return filter(filtered, new EventFilter() {
            public boolean filter(Object event) {
                return (Boolean) filter.call(event);
            }
        });
    }

    public static EventConsumer filter(EventConsumer consumer, EventFilter filter) {
        return new FilteredEventConsumer(consumer, filter);
    }

    public static EventConsumer filter(String name, final Closure consumer, final Closure filter) throws EventException {
        return filter(closured(name, consumer), filter);
    }
}
