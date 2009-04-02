package com.infusion.util.event.chain;

import com.infusion.util.event.*;

import static java.text.MessageFormat.format;

import groovy.lang.Closure;

/**
 * Event consumer class that listens to one event, and then re-publishes the event as another event.
 * <p/>
 * You can optionally provide a tranformer to convert the event from one type to another (if the target event requires
 * a different type of object).  A transformer that returns null will cause the chaining to not occur.
 */
public class ChainedEventConsumer implements EventConsumer {
// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    protected EventTransformer eventTransformer;
    String sourceEvent;
    protected String targetEvent;

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    /**
     * The advantage of using this constructor is that it will verify that the events you are chaining
     * are allowed to be chained (the target event takes in the same type of event as the source)
     *
     * @param source
     * @param target
     * @throws EventException
     */
    public ChainedEventConsumer(EventType source,
                                EventType target) throws EventException {
        this.sourceEvent = source.eventName;
        this.targetEvent = target.eventName;
        if (target == null || source == null) {
            throw new EventException(format("Source {0} or target {1} was null", source, target));
        }
        if (!source.canActLike(target)) {
            throw new EventException(format("Source {0} can't act like {1}, should 'be assignable from' (extend)", source, target));
        }
    }

    public ChainedEventConsumer(String source, String target) {
        this.sourceEvent = source;
        this.targetEvent = target;
    }



// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    public void consume(Object event, EventBroker broker) {
        if (eventTransformer != null) {
            event = eventTransformer.transform(event);
        }
        if (event != null) {
            broker.publish(targetEvent, event);
        }
    }

    public String toString() {
        return format("Chained Event: from {0} to {1}", sourceEvent, targetEvent);
    }
}
