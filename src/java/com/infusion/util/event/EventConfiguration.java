package com.infusion.util.event;

import java.util.List;
import java.util.Map;

/**
 * Provides a configuration for event types and consumers.
 */
public interface EventConfiguration {
    public List<EventType> getEventTypes();

    public Map<EventConsumer, String> getEventConsumers();
}
