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

class FalconeUtilGrailsPlugin {
  def version = 0.1 
  def dependsOn = [:]
  // TODO Fill in these fields
  def author = "Eric Martineau"
  def authorEmail = ""
  def title = "Falcone Util Project"
  def description = '''\\
The base classes used to support multi-tenancy and the falcone framework
'''

  // URL to the plugin's documentation
  def documentation = "http://grails.org/FalconeUtil+Plugin"

  def doWithSpring = {
    //Register the event broker for the system
    eventBroker(GroovyEventBroker)

    //Register all hibernate events to be published to the event broker
    hibernateEventAdapter(HibernateEventAdapter) {
      eventBroker = eventBroker
      sessionFactory = ref("sessionFactory")
    }
  }

  def doWithApplicationContext = {GrailsApplicationContext ctx->
  }
  
}
