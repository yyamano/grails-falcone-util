package com.infusion.util.event;

/**
 * An event type is a combination of the object class to be passed as the event as well as the name
 * of the event as it was registered.
 */
public class EventType {
    public final Class eventClass;
    public final String eventName;

    public EventType(Class eventClass, String eventName) {
        this.eventClass = eventClass;
        this.eventName = eventName;
    }

    public EventType(Class eventClass) {
        this.eventClass = eventClass;
        this.eventName = eventClass.getSimpleName();
    }

    public boolean canActLike(EventType childEvent) {
        return childEvent.eventClass.isAssignableFrom(eventClass);
    }

    @Override
    public String toString() {
        return eventName + ": " + eventClass;
    }
}
