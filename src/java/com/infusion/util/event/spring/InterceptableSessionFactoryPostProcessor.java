package com.infusion.util.event.spring;

import org.springframework.beans.factory.config.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import com.infusion.util.domain.event.hibernate.InterceptableConfigurableSessionFactoryBean;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Apr 1, 2009
 * Time: 10:52:27 PM
 */
public class InterceptableSessionFactoryPostProcessor implements BeanFactoryPostProcessor {
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //Handle SessionFactory
        final BeanDefinition sessionFactoryBeanDefinition = beanFactory.getBeanDefinition("sessionFactory");
        sessionFactoryBeanDefinition.setBeanClassName(InterceptableConfigurableSessionFactoryBean.class.getName());
        sessionFactoryBeanDefinition.getPropertyValues().addPropertyValue(new PropertyValue("eventBroker", new RuntimeBeanReference("eventBroker")));
    }

}
