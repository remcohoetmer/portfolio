package nl.remco.service.common.helpers;

import java.util.Map;

import nl.remco.service.common.model.Benoembaar;
import nl.remco.service.common.model.Identifiable;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.Scope;


public class ScopeDataEnrichmentHelper {

	private static final String ONBEKEND = "-Onbekend-";

	public static void enrichScope(Map<String, Scope> returnedScopeMap, Groep groep) {
		Identifiable product=groep.getScope(); 
		if (product== null) {
			return;
		}
		Benoembaar enriched= new Benoembaar();
		enriched.setId( product.getId());
		if (product.getId()== null) {
			enriched.setNaam( ONBEKEND);
		} else  {
			Scope returnedScope= returnedScopeMap.get( product.getId());
			if (returnedScope!= null) {
				enriched.setNaam( returnedScope.getName());
			} else {
				enriched.setNaam( ONBEKEND);
			}

		}
		groep.setScope( enriched);
	}
}
