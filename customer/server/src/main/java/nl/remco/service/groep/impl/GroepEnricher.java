package nl.remco.service.groep.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nl.remco.service.common.helpers.OrganisatieDataEnrichmentHelper;
import nl.remco.service.common.helpers.ScopeDataEnrichmentHelper;
import nl.remco.service.common.helpers.ScopeServiceHelper;
import nl.remco.service.common.model.Benoembaar;
import nl.remco.service.groep.dao.GroepDao;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.Lidmaatschap;
import nl.remco.service.groep.web.GRP_GetRequest;
import nl.remco.service.groep.web.GRP_Selectie;
import nl.remco.service.klant.impl.CRMCustomersDelegate;
import nl.remco.service.klant.model.Klant;
import nl.remco.service.klant.model.Inschrijving;
import nl.remco.service.organisatie.impl.CRMOrganisationsDelegate;
import nl.remco.service.organisatie.model.Organisatie;
import nl.remco.service.scope.model.Scope;
import nl.remco.service.scope.web.SCO_ScopeService;

import org.springframework.beans.factory.annotation.Autowired;

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
	 * Organisaties (incl. locaties), scopes en Gebruikers.
	 * Gebruikers refereren ook naar Organisatie: De eventuele verrijkig van Klanten met Organisatiegegevens wordt gedelegeerd naar de KlantenService
	 * Verrijking wordt gestuurd door een filter
	 */
	public void execute(List<Groep> groepen,
			GRP_Selectie selectie,
			GroepDao groepDao) {
		Set<String> klantIdSet= new HashSet<String>();
		Set<String> organisatieIdSet= new HashSet<String>();
		Set<String> groepscopeIdSet= new HashSet<String>();
		Set<String> hoofdgroepIdSet= new HashSet<String>();

		// zet alle Ids van de gebruiker/organisatie/locatie een lijst
		// --> dit is een set zodat automatisch duplicates worden ge�limineerd
		for (Groep groep: groepen) {
			if (groep.getLidmaatschappen()!= null) {
				for (Lidmaatschap lidmaatschap: groep.getLidmaatschappen()) {
					if (lidmaatschap.getKlant()!=null) {
						if (lidmaatschap.getKlant().getId()!= null) {
							klantIdSet.add( lidmaatschap.getKlant().getId());
						}
					}
				}
			}
			if (groep.getOrganisatie()!=null
					&& groep.getOrganisatie().getId()!= null) {
				organisatieIdSet.add( groep.getOrganisatie().getId());
			}
			if (groep.getScope()!=null
					&& groep.getScope().getId()!= null) {
				groepscopeIdSet.add( groep.getScope().getId());
			}
			if (groep.getHoofdgroep()!=null
					&& groep.getHoofdgroep().getId()!= null) {
				hoofdgroepIdSet.add( groep.getHoofdgroep().getId());
			}
		}

		// zet de klanten in een hashmap om ze snel te vinden
		Map<String, Klant> klantMap= new HashMap<String, Klant>();
		Map<String, Benoembaar> hoofdgroepMap= new HashMap<String, Benoembaar>();

		// initialiseer default (omwille van robuustheid)

		for (String id: klantIdSet) {
			Klant gebruiker= new Klant();
			gebruiker.setId( id);
			gebruiker.setAchternaam("Onbekend");
			klantMap.put( id, gebruiker);
		}	

		for (String id: hoofdgroepIdSet) {
			Benoembaar gebruikersgroep= new Benoembaar();
			gebruikersgroep.setId( id);
			gebruikersgroep.setNaam("Onbekend");
			hoofdgroepMap.put( id, gebruikersgroep);
		}	


		if (selectie.isSelectKlanten() && !klantIdSet.isEmpty()) {
			cRMCustomersDelegate.getKlanten(
					klantIdSet,
					klantMap);
			if (selectie.isSelectOrganisaties()) {
				addOrganisatieIds( organisatieIdSet, klantMap);
			}
		}


		Map<String, Organisatie> returnedOrganisatieMap= null;
		if (selectie.isSelectOrganisaties() && !organisatieIdSet.isEmpty()) {
			// calls naar OrganisatieService: de betreffende organisaties inclusief alle locaties
			returnedOrganisatieMap = cRMOrganisationsDelegate.getOrganisaties( organisatieIdSet);
		}
		Map<String, Scope> returnedScopesMap= null;
		if (selectie.isSelectScopes() && !groepscopeIdSet.isEmpty()) {

			returnedScopesMap = ScopeServiceHelper.getScopes(getScopeService(), groepscopeIdSet);
		}
		if (selectie.isSelectHoofdgroep() && !hoofdgroepIdSet.isEmpty()) {
			// converteer de set naar een list om ze als Dao te kunnen zoeken
			GRP_GetRequest request= new GRP_GetRequest();

			request.setIds(new ArrayList<String>( hoofdgroepIdSet));
			request.setSelectie( new GRP_Selectie());
			// Dao aanroep
			List<Groep> hoofdgroepen= groepDao.getGroepen(request);
			// zet de geretourneerde data in de map: niet de objecten zelf want dan wordt teveel teruggegeven
			for (Groep hoofdgroep: hoofdgroepen) {
				Benoembaar enrichedgroep= new Benoembaar();
				enrichedgroep.setId( hoofdgroep.getId());
				enrichedgroep.setNaam(hoofdgroep.getNaam());
				enrichedgroep.setStatus( hoofdgroep.getStatus());
				hoofdgroepMap.put( enrichedgroep.getId(), enrichedgroep);
			}
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

	private void addOrganisatieIds(Set<String> organisatieIdSet,
			Map<String, Klant> gebruikerMap) {
		for(Entry<String, Klant> gebruikerEntry: gebruikerMap.entrySet()) {
			if (gebruikerEntry.getValue().getInschrijvingen()!= null) {
				for (Inschrijving inschrijving: gebruikerEntry.getValue().getInschrijvingen()) {
					if (inschrijving.getOrganisatie()!= null) {
						organisatieIdSet.add( inschrijving.getOrganisatie().getId());
					}
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
