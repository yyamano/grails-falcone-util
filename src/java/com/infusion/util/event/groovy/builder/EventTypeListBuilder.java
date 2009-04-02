package com.infusion.util.event.groovy.builder;

import groovy.lang.GroovyObjectSupport;

import java.util.List;
import java.util.ArrayList;

import com.infusion.util.event.EventType;

/**
 * EventBuilder is a class used to build EventType objects.  You use it like this:
 *
 * <pre>
 * def b = new EventTypeBuilder();
 * b {
 *     myEventName(MyEventClass)
 *     anotherEventName(AnotherEventClass)
 * }
 * </pre>
 */
public class EventTypeListBuilder extends GroovyObjectSupport {

    List<EventType> eventTypes = new ArrayList<EventType>();

    @Override
    public Object invokeMethod(String name, Object o) {
        Object[] args = (Object[]) o;
        eventTypes.add(new EventType((Class) args[0], name));
        return this;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }
}
