package util.hibernate

import org.hibernate.SessionFactory
import org.hibernate.event.EventListeners
import org.codehaus.groovy.grails.orm.hibernate.ConfigurableLocalSessionFactoryBean

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Mar 22, 2009
 * Time: 12:26:11 AM
 */
public class HibernateEventUtil {
  public static void addListener(SessionFactory factory, String type, Object listener) {
     //Event listeners are stored in an array (for each event) on the sessionfactory
      EventListeners eventListeners = factory.eventListeners
      eventListeners."${type}EventListeners";
      Object[] listeners = eventListeners."${type}EventListeners"
      Object[] expandedListeners = new Object[listeners.length + 1];
      System.arraycopy(listeners, 0, expandedListeners, 0, listeners.length)
      expandedListeners[-1] = listener;
      eventListeners."${type}EventListeners" = expandedListeners
  }

}