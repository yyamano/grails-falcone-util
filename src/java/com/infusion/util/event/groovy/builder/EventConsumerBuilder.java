package com.infusion.util.event.groovy.builder;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.Closure;
import com.infusion.util.event.EventConsumer;
import com.infusion.util.event.EventBroker;
import com.infusion.util.event.groovy.GroovyChainedEventConsumer;
import com.infusion.util.event.chain.ChainedEventConsumer;

/**
 * Builder that builds out eventConsumer instances for a given eventName.
 */
public class EventConsumerBuilder extends GroovyObjectSupport {
// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    public final String eventName;
    final String CHAIN_METHOD = "chain";
    EventConsumerListBuilder eventConsumerListBuilder;

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    public EventConsumerBuilder(String eventName, EventConsumerListBuilder eventConsumerListBuilder) {
        this.eventName = eventName;
        this.eventConsumerListBuilder = eventConsumerListBuilder;
    }

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    @Override
    public Object invokeMethod(final String s, Object o) {
        final Object[] args = (Object[]) o;
        final EventConsumer eventConsumer;
        if (CHAIN_METHOD.equals(s)) {
            String chainTo = (String) args[0];
            if(args.length>1 && args[1] instanceof Closure) {
                eventConsumer = new GroovyChainedEventConsumer(eventName, chainTo, (Closure) args[1]);
            } else {
                eventConsumer = new ChainedEventConsumer(eventName, chainTo);
            }
        } else {
            eventConsumer = new EventConsumer() {
                public void consume(Object event, EventBroker broker) {
                    ((Closure) args[0]).call(new Object[]{event, broker});
                }

                public String toString() {
                    return s;
                }
            };
        }
        eventConsumerListBuilder.eventConsumers.put(eventConsumer, eventName);
        return this;
    }
}
