package com.infusion.util.event;

/**
 * Allows you to access the event broker staticly.
 */
public class EventBrokerHolder {
// ======================================================================================================================== 
//    Static Fields 
// ======================================================================================================================== 

    private static EventBroker eventBroker;

// ======================================================================================================================== 
//    Static Methods 
// ======================================================================================================================== 

    public static EventBroker getEventBroker() {
        return eventBroker;
    }

// ======================================================================================================================== 
//    Public Instance Methods
// ======================================================================================================================== 

    public void setEventBroker(EventBroker eventBroker) {
        EventBrokerHolder.eventBroker = eventBroker;
    }
}
