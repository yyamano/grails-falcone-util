package com.infusion.util.event.groovy.builder;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.Closure;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.infusion.util.event.EventConsumer;
import com.infusion.util.event.EventBroker;
import com.infusion.util.StringUtils;
import com.infusion.util.CollectionUtil;


/**
 * Builder class for defining event listeners
 */
public class EventConsumerListBuilder extends GroovyObjectSupport {
    Map<EventConsumer, String> eventConsumers = new HashMap<EventConsumer, String>();
    List<String> parts = new ArrayList<String>();

    @Override
    public Object getProperty(String s) {
        parts.add(s);
        return this;
    }

    @Override
    public Object invokeMethod(String eventNamePart, Object o) {
        final Object[] args = (Object[]) o;
        parts.add(eventNamePart);
        final String fullEventName = CollectionUtil.CollectionValues(parts, ".", false);
        parts.clear();
        final Closure eventClosure;
        final String name;
        if (args.length == 2) {
            eventClosure = (Closure) args[1];
            name = (String) args[0];
        } else if (args.length == 1) {
            eventClosure = (Closure) args[0];
            name = "anonymous";
        } else {
            name = null;
            eventClosure = null;
        }
        if (eventClosure != null) {
            EventConsumer eventConsumer = new EventConsumer() {
                public void consume(Object event, EventBroker broker) {
                    if (eventClosure.getMaximumNumberOfParameters() == 2) {
                        eventClosure.call(new Object[]{event, broker});
                    } else {
                        eventClosure.call(event);
                    }
                }
                
                public String toString() {
                    return name;
                }
            };
            eventConsumers.put(eventConsumer, fullEventName);
        }

        return this;
    }

    public Map<EventConsumer, String> getEventConsumers() {
        return eventConsumers;
    }
}
