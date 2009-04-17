import org.codehaus.groovy.grails.commons.ControllerArtefactHandler
import org.codehaus.groovy.grails.commons.ServiceArtefactHandler
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.commons.TagLibArtefactHandler
import org.codehaus.groovy.grails.plugins.web.filters.FiltersConfigArtefactHandler
import com.infusion.util.grails.GrailsBeanReloader

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Apr 10, 2009
 * Time: 11:26:25 PM
 */
public class BaseGrailsPlugin {

  def watchedResources = [
          "file:../../modules/*/src/groovy/**/*.groovy",
          "file:../../modules/*/grails-app/controllers/**/*Controller.groovy",
          "file:../../modules/*/grails-app/taglib/**/*TagLib.groovy",
          "file:../../modules/*/grails-app/service/**/*Service.groovy",
          "file:../../modules/*/grails-app/domain/**/*.groovy",
          "file:../../modules/*/grails-app/i18n/**/*.properties",
          "file:../../modules/*/grails-app/main/**/*.groovy"
  ]

  def onChange = {
    event ->
    println event
    if (event.source instanceof Class || event.source instanceof String) {
      if (application.isArtefactOfType(ControllerArtefactHandler.TYPE, event.source)) {
        manager?.getGrailsPlugin("controllers")?.notifyOfEvent(event)
      }
      else if (application.isArtefactOfType(ServiceArtefactHandler.TYPE, event.source)) {
        manager?.getGrailsPlugin("services")?.notifyOfEvent(event)
      }

      else if (application.isArtefactOfType(DomainClassArtefactHandler.TYPE, event.source)) {
          manager?.getGrailsPlugin("hibernate")?.notifyOfEvent(event)
          manager?.getGrailsPlugin("domainClass")?.notifyOfEvent(event)
        }

        else if (application.isArtefactOfType(TagLibArtefactHandler.TYPE, event.source)) {
            manager?.getGrailsPlugin("groovyPages")?.notifyOfEvent(event)
          }

          else if (application.isArtefactOfType(FiltersConfigArtefactHandler.TYPE, event.source)) {
              manager?.getGrailsPlugin("filters")?.notifyOfEvent(event)
            }

            else {
              new GrailsBeanReloader().reloadBeans(event, this.doWithSpring) 
            }



    } else {
      BaseGrailsPlugin.getClassLoader().getParent()
    }
  }
}