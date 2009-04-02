package com.infusion.util.event.groovy;

import com.infusion.util.event.chain.ChainedEventConsumer;
import com.infusion.util.event.chain.EventTransformer;
import com.infusion.util.event.EventType;
import com.infusion.util.event.EventException;
import groovy.lang.Closure;

/**
 * An extension of ChainedEventConsumer that allows to pass closures as parameters.
 */
public class GroovyChainedEventConsumer extends ChainedEventConsumer {
// ========================================================================================================================
//    Constructors
// ========================================================================================================================

     /**
     * Constructor that takes in a closure for the transformer.
     *
     * @param source
     * @param target
     * @param transformer
     */
    public GroovyChainedEventConsumer(String source, String target, final Closure transformer) {
        super(source, target);
        this.targetEvent = target;
        this.eventTransformer = new EventTransformer() {
            public Object transform(Object source) {
                return transformer.call(source);
            }
        };
    }

    public GroovyChainedEventConsumer(EventType source,
                                EventType target, final Closure transformer) throws EventException {
        super(source, target);
        this.eventTransformer = new EventTransformer() {
            public Object transform(Object source) {
                return transformer.call(source);
            }
        };
    }
}
