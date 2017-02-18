package nl.remco.service.klant.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.remco.service.common.helpers.OrganisatieDataEnrichmentHelper;
import nl.remco.service.klant.model.Klant;
import nl.remco.service.klant.model.Inschrijving;
import nl.remco.service.organisatie.impl.CRMOrganisationsDelegate;
import nl.remco.service.organisatie.model.Organisatie;

import org.springframework.beans.factory.annotation.Autowired;

public class KlantEnricher {

	@Autowired
	private CRMOrganisationsDelegate cRMOrganisationDelegate;
	
	public CRMOrganisationsDelegate getCRMOrganisationsDelegate() {
		return cRMOrganisationDelegate;
	}

	public void setCRMOrganisationDelegate(
			CRMOrganisationsDelegate cRMOrganisationDelegate) {
		this.cRMOrganisationDelegate = cRMOrganisationDelegate;
	}

	public void enrichOrganisaties( List<Klant> klanten){

		// verzamel de Ids van alle organisaties
		// --> dit is een set zodat automatisch duplicates worden geëlimineerd
		Set<String> organisatieIdSet = new HashSet<String>();
		for (Klant klant: klanten) {
			List<Inschrijving> inschrijvingen= klant.getInschrijvingen();
			if (inschrijvingen!= null) {
				for (Inschrijving inschrijving: inschrijvingen) {
					if (inschrijving.getOrganisatie()!= null && inschrijving.getOrganisatie().getId()!= null) {
						organisatieIdSet.add( inschrijving.getOrganisatie().getId());
					}
				}
			}
		}
		
		if (organisatieIdSet.isEmpty()) {
			return;
		}
		
		// calls naar OrganisatieService: de betreffende organisaties inclusief alle locaties

		Map<String, Organisatie> returnedOrganisatieMap = cRMOrganisationDelegate.getOrganisaties(
				organisatieIdSet);

		for (Klant gebruiker: klanten) {
			List<Inschrijving> inschrijvingen= gebruiker.getInschrijvingen();
			if (inschrijvingen!= null) {
				for (Inschrijving inschrijving: inschrijvingen) {
					OrganisatieDataEnrichmentHelper.enrich( returnedOrganisatieMap,
							inschrijving);
				}
			}
		}
	}


}
