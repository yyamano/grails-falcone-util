package com.infusion.util.domain.event.hibernate;

import org.codehaus.groovy.grails.orm.hibernate.ConfigurableLocalSessionFactoryBean;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import com.infusion.util.event.EventBroker;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Mar 31, 2009
 * Time: 5:07:41 PM
 */
public class InterceptableConfigurableSessionFactoryBean extends ConfigurableLocalSessionFactoryBean {
    private EventBroker eventBroker;
    
    @Override
    protected SessionFactory newSessionFactory(Configuration configuration) throws HibernateException {
        return new InterceptableSessionFactory(super.newSessionFactory(configuration), eventBroker);
    }


    public void setEventBroker(EventBroker eventBroker) {
        this.eventBroker = eventBroker;
    }
}
