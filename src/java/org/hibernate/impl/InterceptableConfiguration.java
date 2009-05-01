package org.hibernate.impl;

import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Settings;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Collection;
import org.hibernate.event.EventListeners;
import org.hibernate.engine.Mapping;
import org.hibernate.util.PropertiesHelper;
import org.codehaus.groovy.grails.orm.hibernate.cfg.DefaultGrailsDomainConfiguration;

import java.util.Properties;
import java.util.Iterator;

import com.infusion.util.domain.event.hibernate.InterceptableSessionFactory;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Apr 29, 2009
 * Time: 10:52:42 AM
 */
public class InterceptableConfiguration extends DefaultGrailsDomainConfiguration {
    @Override
    public SessionFactory buildSessionFactory() throws HibernateException {
        secondPassCompile();
        validate();
        final Mapping mapping = buildMapping();
        Environment.verifyProperties(getProperties());
        Properties copy = new Properties();
        copy.putAll(getProperties());
        PropertiesHelper.resolvePlaceHolders(copy);
        Settings settings = buildSettings(copy);

        return null;
//        new InterceptableSessionFactory(
//                this,
//                mapping,
//                settings,
//                getInitializedEventListeners(),
//                getSessionFactoryObserver()
//        );
    }

    private void validate() {
        final Mapping mapping = buildMapping();

        Iterator iter = classes.values().iterator();
        while (iter.hasNext()) {
            ((PersistentClass) iter.next()).validate(mapping);
        }
        iter = collections.values().iterator();
        while (iter.hasNext()) {
            ((Collection) iter.next()).validate(mapping);
        }
    }

    private EventListeners getInitializedEventListeners() {
        EventListeners result = (EventListeners) getEventListeners().shallowCopy();
        result.initializeListeners(this);
        return result;
    }
    
}
