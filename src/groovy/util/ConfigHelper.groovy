package util

import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * A class used to help read values from ConfigurationHolder, taking into account
 * default values
 */

public class ConfigHelper {

  /**
   * Returns a given property value, or a default if none exists.
   *
   * For example, to get the value of someProp.someValue from Config.groovy:
   * ...
   * tenant {
   *    resolver.type = "request"
   * }
   * ...
   *
   * ConfigHelper.get("db") {it.tenant.resolver.type}
   */
  static def get(def defaultValue, Closure closure) {
    def rtn = closure(ConfigurationHolder.config)
    if(rtn instanceof ConfigObject) {
      rtn = (rtn.size() == 0) ? defaultValue : rtn
    }
    return rtn;
  }

}