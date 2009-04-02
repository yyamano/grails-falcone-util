package com.infusion.util.event.filter;

import com.infusion.util.event.filter.EventFilter;
import com.infusion.util.event.EventConsumer;
import com.infusion.util.event.EventBroker;

import static java.text.MessageFormat.*;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Mar 20, 2009
 * Time: 8:23:44 PM
 */
public class FilteredEventConsumer<E> implements EventConsumer<E> {
    private final EventConsumer<E> consumer;
    private final EventFilter<E> filter;

    public FilteredEventConsumer(EventConsumer<E> consumer, EventFilter<E> filter) {
        this.consumer = consumer;
        this.filter = filter;
    }

    public void consume(E event, EventBroker eventBroker) {
        if (filter.filter(event)) {
            consumer.consume(event, eventBroker);
        }
    }

    public String toString() {
        return format("Filtered Consumer Wrapper: {0}", consumer);
    }
}
