package com.infusion.util.event.groovy;

import static java.text.MessageFormat.format;

import groovy.lang.Closure;
import com.infusion.util.event.*;

/**
 * An extension of the base event broker class that adds convenience methods for passing closures as parameters.
 * <p/>
 * You can "subscribe" to events, or "publish" events.
 *
 * @author eric
 */
public class GroovyEventBroker extends EventBrokerImpl {
// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    public void publish(String name, final Closure closure) {
        super.publish(name, new EventDelegate() {
            public Object getEvent(EventBroker eventBroker) {
                return closure.call(eventBroker);
            }
        });
    }

    public void subscribe(String eventType, final Closure closure) {
        subscribe(eventType, "Anonymous", closure);
    }

    /**
     * Registers a new consumer, passing a closure as the event handler
     */
    public void subscribe(String eventType, final String name, final Closure closure) {
        log.debug(format("Registered a closured consumer for {0}: {1}", eventType, name));
        subscribe(eventType, EventUtils.closured(name, closure));
    }
}
