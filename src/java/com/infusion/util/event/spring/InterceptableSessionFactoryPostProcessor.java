package com.infusion.util.event.spring;

import org.springframework.beans.factory.config.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.orm.hibernate3.SpringSessionContext;
import com.infusion.util.domain.event.hibernate.InterceptableConfigurableSessionFactoryBean;

/**
 * This class overrides the sessionFactory bean definition to use an Interceptable one instead.  It also wires the event
 * broker bean and the currentSessionContextClass implementation.
 * 
 */
public class InterceptableSessionFactoryPostProcessor implements BeanFactoryPostProcessor {
// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //Handle SessionFactory
        final BeanDefinition sessionFactoryBeanDefinition = beanFactory.getBeanDefinition("sessionFactory");
        sessionFactoryBeanDefinition.setBeanClassName(InterceptableConfigurableSessionFactoryBean.class.getName());
        final MutablePropertyValues propertyValues = sessionFactoryBeanDefinition.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("eventBroker", new RuntimeBeanReference("eventBroker")));
        propertyValues.addPropertyValue(new PropertyValue("currentSessionContextClass", InterceptableCurrentSessionContext.class));
    }
}
