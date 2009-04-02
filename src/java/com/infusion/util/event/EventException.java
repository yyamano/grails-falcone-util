package com.infusion.util.event;

/**
 * An exception thrown when dealing with events.
 */
public class EventException extends Exception {
    public EventException() {
        super();
    }

    public EventException(String s) {
        super(s);
    }

    public EventException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EventException(Throwable throwable) {
        super(throwable);
    }
}
