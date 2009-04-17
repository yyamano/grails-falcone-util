package com.infusion.util.event.groovy.builder;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.Closure;

import java.util.Map;
import java.util.HashMap;

import com.infusion.util.event.EventConsumer;


/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Mar 22, 2009
 * Time: 2:04:28 PM
 */
public class EventConsumerListBuilder extends GroovyObjectSupport {
    Map<EventConsumer, String> eventConsumers = new HashMap<EventConsumer, String>();

    @Override
    public Object invokeMethod(String s, Object o) {
        EventConsumerBuilder eventConsumerBuilder = new EventConsumerBuilder(s, this);
        final Object[] args = (Object[]) o;
        Closure c = (Closure) args[0];
        c.setResolveStrategy(Closure.DELEGATE_FIRST);
        c.setDelegate(eventConsumerBuilder);
        c.call();
        return eventConsumerBuilder;
    }

    public Map<EventConsumer, String> getEventConsumers() {
        return eventConsumers;
    }
}
