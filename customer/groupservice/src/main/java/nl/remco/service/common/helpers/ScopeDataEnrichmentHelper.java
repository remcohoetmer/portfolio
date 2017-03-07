package nl.remco.service.common.helpers;

import java.util.Map;

import nl.remco.group.service.domain.Group;
import nl.remco.group.service.domain.Scope;
import nl.remco.service.common.model.Benoembaar;
import nl.remco.service.common.model.Identifiable;



public class ScopeDataEnrichmentHelper {

	private static final String ONBEKEND = "-Onbekend-";

	public static void enrichScope(Map<String, Scope> returnedScopeMap, Group group) {
		Scope scope=group.getScope(); 
		if (scope== null) {
			return;
		}
		Scope enriched= new Scope();
		enriched.setId( scope.getId());
		if (scope.getId()== null) {
			enriched.setName( ONBEKEND);
		} else  {
			Scope returnedScope= returnedScopeMap.get( scope.getId());
			if (returnedScope!= null) {
				enriched.setName( returnedScope.getName());
			} else {
				enriched.setName( ONBEKEND);
			}

		}
		group.setScope( enriched);
	}
}
