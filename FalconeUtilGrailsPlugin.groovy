import com.infusion.util.event.groovy.GroovyEventBroker
import com.infusion.util.domain.event.HibernateEventAdapter
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import com.infusion.util.event.EventType
import org.hibernate.event.LoadEvent
import org.hibernate.event.PreDeleteEvent
import org.hibernate.event.PreUpdateEvent
import org.hibernate.event.PreInsertEvent
import com.infusion.util.event.groovy.EventUtils
import org.hibernate.event.PreInsertEvent
import com.infusion.util.event.groovy.GroovyEventBroker
import org.codehaus.groovy.grails.commons.spring.GrailsApplicationContext
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.plugins.DefaultGrailsPluginManager
import org.codehaus.groovy.grails.plugins.GrailsPlugin
import com.infusion.util.event.EventBroker
import com.infusion.util.event.groovy.builder.EventBuilder
import com.infusion.util.event.groovy.builder.EventConsumerListBuilder
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.GrailsClass
import com.infusion.util.event.spring.InterceptableSessionFactoryPostProcessor
import org.hibernate.Criteria
import org.hibernate.criterion.Expression
import grails.spring.BeanBuilder
import org.springframework.util.ClassUtils
import grails.util.GrailsUtil
import com.infusion.util.event.EventBrokerHolder

class FalconeUtilGrailsPlugin {
  def version = 0.6
  def dependsOn = [:]
  // TODO Fill in these fields
  def author = "Eric Martineau"
  def authorEmail = "ericm@infusionsoft.com"
  def title = "Falcone Util Project"
  def description = '''\\
The base classes used to support multi-tenancy and the falcone framework
'''

  // URL to the plugin's documentation
  def documentation = "http://grails.org/FalconeUtil+Plugin"

  def doWithSpring = {
    //Register the event broker for the system
    eventBroker(GroovyEventBroker)

    eventBrokerHolder(EventBrokerHolder) {
      eventBroker = eventBroker
    }

    //Register all hibernate events to be published to the event broker
    hibernateEventAdapter(HibernateEventAdapter) {
      eventBroker = eventBroker
      sessionFactory = ref("sessionFactory")
    }

    //This post-processor wraps the session factory with a tenant-aware one
    interceptableSessionFactoryPostProcessor(InterceptableSessionFactoryPostProcessor)
  }

  def doWithApplicationContext = {GrailsApplicationContext ctx ->
  }


  def doWithDynamicMethods = {
    GrailsApplicationContext ctx ->
    EventBroker eventBroker = ctx.eventBroker;

    //Attach doWithEvents handler to all plugin classes
    DefaultGrailsPluginManager grailsManager = (DefaultGrailsPluginManager) manager;
    grailsManager.getAllPlugins().each {
      GrailsPlugin plugin ->
      if (plugin.getInstance().getProperties().containsKey("doWithEvents")) {
        Closure doWithEventsClosure = plugin.getInstance().doWithEvents;
        EventConsumerListBuilder builder = new EventConsumerListBuilder();
        doWithEventsClosure.delegate = builder;
        doWithEventsClosure.resolveStrategy = Closure.DELEGATE_ONLY;
        doWithEventsClosure.call(ctx);
        builder.getEventConsumers().each {
          entry -> eventBroker.subscribe(entry.value, entry.key)
        }
      }
    }

    try {
      Class groovyEventResourcesClass = null;
      try {
        groovyEventResourcesClass = ClassUtils.forName("events",
                application.classLoader);
      } catch (ClassNotFoundException e) {
        // ignore
      }
      if (groovyEventResourcesClass != null) {
        EventConsumerListBuilder eventConsumerListBuilder = new EventConsumerListBuilder();
        Script script = (Script) groovyEventResourcesClass.newInstance();
        script.run();
        Closure beans = script.getProperty("consumers");
        beans.delegate = eventConsumerListBuilder
        beans.call()
        eventConsumerListBuilder.getEventConsumers().each{entry ->
          eventBroker.subscribe(entry.value, entry.key)  
        }
      }
    } catch (Exception ex) {
      GrailsUtil.deepSanitize(ex);
    }

    //This is the publish closure to be assigned
    def publishClosure = {
      String name, Object event ->
      eventBroker.publish(name, event)
    }

    //Attach publish method to domain classes
    for (domainClass in application.domainClasses) {
      def mc = domainClass.clazz.metaClass
      mc.publishEvent = publishClosure
    }

    //Attach publish method to controllers
    for (GrailsClass controller in application.controllerClasses) {
      def mc = controller.clazz.metaClass
      mc.publishEvent = publishClosure
    }
  }
}
