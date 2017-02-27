package nl.remco.service.application;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("resources")
public class GroupApplication extends ResourceConfig {
	public GroupApplication() {
		packages("nl.remco.service.groep.web",
				"nl.remco.service.klant.web",
				"nl.remco.service.scope.web",
				"nl.remco.service.organisatie.web");
	}
}