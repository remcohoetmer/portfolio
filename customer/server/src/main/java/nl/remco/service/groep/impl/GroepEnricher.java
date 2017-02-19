package nl.remco.service.groep.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import nl.remco.service.common.helpers.OrganisatieDataEnrichmentHelper;
import nl.remco.service.common.helpers.ScopeDataEnrichmentHelper;
import nl.remco.service.common.helpers.ScopeServiceHelper;
import nl.remco.service.common.model.Benoembaar;
import nl.remco.service.common.model.Identifiable;
import nl.remco.service.groep.dao.GroepDao;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.Lidmaatschap;
import nl.remco.service.groep.web.GRP_Selectie;
import nl.remco.service.klant.impl.CRMCustomersDelegate;
import nl.remco.service.klant.model.Inschrijving;
import nl.remco.service.klant.model.Klant;
import nl.remco.service.organisatie.impl.CRMOrganisationsDelegate;
import nl.remco.service.organisatie.model.Organisatie;
import nl.remco.service.scope.model.Scope;
import nl.remco.service.scope.web.SCO_ScopeService;

public class GroepEnricher {
	@Autowired
	private SCO_ScopeService scopeService;

	@Autowired
	private CRMCustomersDelegate cRMCustomersDelegate;

	@Autowired
	private CRMOrganisationsDelegate cRMOrganisationsDelegate;

	public CRMOrganisationsDelegate getCRMOrganisationsDelegate() {
		return cRMOrganisationsDelegate;
	}

	public void setCRMOrganisationsDelegate(
			CRMOrganisationsDelegate cRMOrganisationsDelegate) {
		this.cRMOrganisationsDelegate = cRMOrganisationsDelegate;
	}

	public CRMCustomersDelegate getCRMCustomersDelegate() {
		return cRMCustomersDelegate;
	}

	public void setCRMCustomersDelegate(CRMCustomersDelegate cRMCustomersDelegate) {
		this.cRMCustomersDelegate = cRMCustomersDelegate;
	}

	/*
	 * Verrijkt groepen met data van externe services
	 * Organisaties, scopes en klanten.
	 * Klanten refereren ook naar Organisatie: De eventuele verrijkig van Klanten met Organisatiegegevens wordt gedelegeerd naar de KlantenService
	 * Verrijking wordt gestuurd door een filter
	 */
	public void execute(List<Groep> groepen,
			GRP_Selectie selectie,
			GroepDao groepDao) {

		// zet alle Ids van de klanten/organisatie/groep/scope een lijst
		// --> dit is een set zodat automatisch duplicates worden ge�limineerd
		
		Set<String> klantIdSet= groepen.stream()
				.map(Groep::getLidmaatschappen)
				.flatMap(List::stream)
				.map(Lidmaatschap::getKlant)
				.map(Identifiable::getId)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		Set<String> organisatieIdSet= groepen.stream()
				.map(Groep::getOrganisatie)
				.map(Identifiable::getId)
				.collect(Collectors.toSet());
		Set<String> scopeIdSet= groepen.stream()
				.map(Groep::getScope)
				.map(Identifiable::getId)
				.collect(Collectors.toSet());
		Set<String> hoofdgroepIdSet= groepen.stream()
				.map(Groep::getHoofdgroep)
				.filter(Objects::nonNull)
				.map(Identifiable::getId)
				.collect(Collectors.toSet());
		
		// zet de klanten in een hashmap om ze snel te vinden
		Map<String, Klant> klantMap= new HashMap<String, Klant>();
		Map<String, Benoembaar> hoofdgroepMap= new HashMap<String, Benoembaar>();

		// initialiseer default (omwille van robuustheid)

		for (String id: klantIdSet) {
			Klant klant= new Klant();
			klant.setId( id);
			klant.setAchternaam("Onbekend");
			klantMap.put( id, klant);
		}	

		for (String id: hoofdgroepIdSet) {
			Benoembaar gebruikersgroep= new Benoembaar();
			gebruikersgroep.setId( id);
			gebruikersgroep.setNaam("Onbekend");
			hoofdgroepMap.put( id, gebruikersgroep);
		}	

		// Data ophalen
		
		if (selectie.isSelectKlanten() && !klantIdSet.isEmpty()) {
			
			// Override the customer data with the data found in the CRM
			Map<String,Klant> crmKlanten= cRMCustomersDelegate.getKlanten( klantIdSet)
					.stream().collect(Collectors.toMap(Klant::getId, Function.identity()));
			klantMap.putAll(crmKlanten);
			// ensure that all the data for the organisations is retrieved as well
			if (selectie.isSelectOrganisaties()) {
				organisatieIdSet.addAll (
						klantMap.values().stream()
						.map( Klant::getInschrijvingen)
						.flatMap(List::stream)
						.map( Inschrijving::getOrganisatie)
						.map( Identifiable::getId)
						.filter( Objects::nonNull)
						.collect( Collectors.toSet()));
			}
		}


		Map<String, Organisatie> returnedOrganisatieMap= null;
		if (selectie.isSelectOrganisaties() && !organisatieIdSet.isEmpty()) {
			// calls naar OrganisatieService: de betreffende organisaties inclusief alle locaties
			returnedOrganisatieMap = cRMOrganisationsDelegate.getOrganisaties( organisatieIdSet);
		}
		Map<String, Scope> returnedScopesMap= null;
		if (selectie.isSelectScopes() && !scopeIdSet.isEmpty()) {

			returnedScopesMap = ScopeServiceHelper.getScopes(getScopeService(), scopeIdSet);
		}
		if (selectie.isSelectHoofdgroep() && !hoofdgroepIdSet.isEmpty()) {
			// override 
			Map<String,Benoembaar> dbGroepen= groepDao.getGroepenAlsBenoembaar(
					new ArrayList<String>( hoofdgroepIdSet));
			hoofdgroepMap.putAll(dbGroepen);
		}

		// alle data is opgehaald. Loop nu door de objecten en vul de gewenste gegevens.

		if (selectie.isSelectKlanten() && selectie.isSelectOrganisaties()) {
			for (Entry<String, Klant> klantEntry: klantMap.entrySet()) {
				List<Inschrijving> inschrijvingen= klantEntry.getValue().getInschrijvingen();
				if (inschrijvingen!= null) {
					for (Inschrijving inschrijving: inschrijvingen) {
						OrganisatieDataEnrichmentHelper.enrich( returnedOrganisatieMap,
								inschrijving);
					}
				}
			}
		}

		for (Groep groep: groepen) {
			if (selectie.isSelectKlanten()) {
				if (groep.getLidmaatschappen()!= null) {
					for (Lidmaatschap lidmaatschap: groep.getLidmaatschappen()) {
						if (lidmaatschap.getKlant()!=null && lidmaatschap.getKlant().getId()!= null) {

							String id= lidmaatschap.getKlant().getId();
							lidmaatschap.setKlant(klantMap.get( id));
						}

					}
				}
			}						

			if (selectie.isSelectOrganisaties()) {
				OrganisatieDataEnrichmentHelper.enrich( returnedOrganisatieMap,
						groep);
			}
			if (selectie.isSelectScopes()) {
				ScopeDataEnrichmentHelper.enrichScope( returnedScopesMap,
						groep);
			}
			if (selectie.isSelectHoofdgroep()) {
				if (groep.getHoofdgroep()!= null && groep.getHoofdgroep().getId()!= null) {
					groep.setHoofdgroep( hoofdgroepMap.get( groep.getHoofdgroep().getId()));
				}
			}
		}

	}


	public SCO_ScopeService getScopeService() {
		return scopeService;
	}

	public void setScopeService(SCO_ScopeService scopeService) {
		this.scopeService = scopeService;
	}


}
