package util
/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Jun 2, 2009
 * Time: 4:58:12 PM
 * To change this template use File | Settings | File Templates.
 */

public class DomainUtils {

  public static void save(def domainObject) {
    if(!domainObject.save() || domainObject.hasErrors())  {
      domainObject.errors.each {
        domainObject.log.error "Error saving ${domainObject}: ${it}"
      }
      throw new IllegalStateException("Attempted a save, but failed: ${domainObject.errors}")
    }
  }

}