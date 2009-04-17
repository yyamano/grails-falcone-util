package com.infusion.util.grails;

import org.codehaus.groovy.grails.commons.spring.GrailsWebApplicationContext;
import org.codehaus.groovy.grails.commons.spring.RuntimeSpringConfiguration;
import org.codehaus.groovy.grails.commons.spring.DefaultRuntimeSpringConfiguration;
import org.codehaus.groovy.grails.commons.spring.BeanConfiguration;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.DefaultGrailsPluginManager;
import org.codehaus.groovy.grails.plugins.DefaultGrailsPlugin;
import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import grails.spring.BeanBuilder;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Apr 11, 2009
 * Time: 3:26:46 PM
 */
public class GrailsBeanReloader {
    public void reloadBeans(Map event, Closure doWithSpring) throws ClassNotFoundException {
        GrailsWebApplicationContext applicationContext = (GrailsWebApplicationContext) event.get("ctx");
        final String[] beanNamesForType =
                applicationContext.getBeanNamesForType(this.getClass().getClassLoader().loadClass(((Class) event.get("source")).getName()));
        if (beanNamesForType.length > 0) {
            final DefaultGrailsPluginManager manager = (DefaultGrailsPluginManager) event.get("manager");
            final DefaultGrailsPlugin plugin = (DefaultGrailsPlugin) manager.getGrailsPluginForClassName(event.get("plugin").getClass().getName());
            final GrailsApplication application = (GrailsApplication) event.get("application");
            BeanBuilder beanBuilder = new BeanBuilder(plugin.getParentCtx(), new GroovyClassLoader(application.getClassLoader()));
            doWithSpring.setDelegate(beanBuilder);
            doWithSpring.call();
            Set<String> beanNames = new HashSet<String>();
            for (String beanName : beanNamesForType) {
                beanNames.add(beanName);
            }
            final RuntimeSpringConfiguration configFromClosure = beanBuilder.getSpringConfig();
            final RuntimeSpringConfiguration filteredConfig = new DefaultRuntimeSpringConfiguration(applicationContext);
            for (Object runtimeNameUncast : configFromClosure.getBeanNames()) {
                String runtimeName = (String) runtimeNameUncast;
                if (beanNames.contains(runtimeName)) {
                    final BeanConfiguration beanConfiguration = configFromClosure.getBeanConfig(runtimeName);
                    filteredConfig.addBeanConfiguration(runtimeName, beanConfiguration);
                }
            }

            if (filteredConfig.getBeanNames().size() > 0) {
                beanBuilder.setSpringConfig(filteredConfig);
                beanBuilder.registerBeans(applicationContext);
            }
        }

    }
}
