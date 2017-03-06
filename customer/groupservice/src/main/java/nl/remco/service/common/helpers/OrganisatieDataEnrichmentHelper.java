package nl.remco.service.common.helpers;

import java.util.Map;

import nl.remco.service.common.model.Benoembaar;
import nl.remco.service.common.model.Identifiable;
import nl.remco.service.organisatie.model.Organisatie;
import nl.remco.service.organisatie.model.OrganisatieDataHolder;

public class OrganisatieDataEnrichmentHelper {

	private static final String ONBEKEND = "-Onbekend-";

	public static void enrich(Map<String, Organisatie> returnedOrganisatieMap,
			OrganisatieDataHolder subject ) {
		Identifiable organisatie= subject.getOrganisatie();
		if (organisatie== null) {
			return;
		}

		Benoembaar enrichedOrganisatie= new Benoembaar();
		enrichedOrganisatie.setId( organisatie.getId());
		subject.setOrganisatie( enrichedOrganisatie);

		if (enrichedOrganisatie.getId()== null) {
			enrichedOrganisatie.setNaam( ONBEKEND);
			return;
		}

		Organisatie returnedOrganisatie= returnedOrganisatieMap.get( organisatie.getId());
		if (returnedOrganisatie!= null) {
			enrichedOrganisatie.setNaam( returnedOrganisatie.getNaam());
			enrichedOrganisatie.setStatus( returnedOrganisatie.getStatus());
		} else {
			enrichedOrganisatie.setNaam( ONBEKEND);
		}
	}

}
