package com.infusion.util.domain.event.hibernate;

import org.codehaus.groovy.grails.orm.hibernate.ConfigurableLocalSessionFactoryBean;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import com.infusion.util.event.EventBroker;
import com.infusion.util.event.spring.InterceptableCurrentSessionContext;

/**
 * This class is used as a factory for creating new SessionFactory instances.  This implementation wraps
 * an existing configuration with an InterceptableSessionFactory instance.
 */
public class InterceptableConfigurableSessionFactoryBean extends ConfigurableLocalSessionFactoryBean {

    /**
     * The event broker is passed down to sessions created by new SessionFactory instances.  It's used
     * to publish hibernate events into the eventing system.
     */
    private EventBroker eventBroker;
    
    @Override
    protected SessionFactory newSessionFactory(Configuration configuration) throws HibernateException {

        //These first two lines are a goofy hack to store the InterceptableSessionFactory so it can be picked up
        //by the InterceptableCurrentSessionContext.
        final InterceptableSessionFactory rtn = new InterceptableSessionFactory();
        InterceptableCurrentSessionContext.sessionFactoryTL.set(rtn);

        try {
            rtn.setWrapped(super.newSessionFactory(configuration));
        } finally {
            InterceptableCurrentSessionContext.sessionFactoryTL.set(null);
        }

        return rtn;
    }


    public void setEventBroker(EventBroker eventBroker) {
        this.eventBroker = eventBroker;
    }
}
