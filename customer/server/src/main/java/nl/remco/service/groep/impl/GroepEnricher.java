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
	private SCO_ScopeService groepscopeService;

	@Autowired
	private CRMCustomersDelegate aTOLGebruikerDelegate;

	@Autowired
	private CRMOrganisationsDelegate aTOLOrganisatieDelegate;

	public CRMOrganisationsDelegate getaTOLOrganisatieDelegate() {
		return aTOLOrganisatieDelegate;
	}

	public void setaTOLOrganisatieDelegate(
			CRMOrganisationsDelegate aTOLOrganisatieDelegate) {
		this.aTOLOrganisatieDelegate = aTOLOrganisatieDelegate;
	}

	public CRMCustomersDelegate getaTOLGebruikerDelegate() {
		return aTOLGebruikerDelegate;
	}

	public void setaTOLGebruikerDelegate(CRMCustomersDelegate aTOLGebruikerDelegate) {
		this.aTOLGebruikerDelegate = aTOLGebruikerDelegate;
	}

	/*
	 * Verrijkt gebruikersgroepen met data van externe services
	 * Organisaties (incl. locaties), Groepscopes en Gebruikers.
	 * Gebruikers refereren ook naar Organisatie: De eventuele verrijkig van Gebruikers met Organisatiegegevens wordt gedelegeerd naar de GebruikersService
	 * Verrijking wordt gestuurd door een filter
	 */
	public void execute(List<Groep> gebruikersgroepen,
			GRP_Selectie selectie,
			GroepDao gebruikersgroepDao) {
		Set<String> gebruikerIdSet= new HashSet<String>();
		Set<String> organisatieIdSet= new HashSet<String>();
		Set<String> groepscopeIdSet= new HashSet<String>();
		Set<String> hoofdgroepIdSet= new HashSet<String>();

		// zet alle Ids van de gebruiker/organisatie/locatie een lijst
		// --> dit is een set zodat automatisch duplicates worden ge�limineerd
		for (Groep gebruikersgroep: gebruikersgroepen) {
			if (gebruikersgroep.getLidmaatschappen()!= null) {
				for (Lidmaatschap lidmaatschap: gebruikersgroep.getLidmaatschappen()) {
					if (lidmaatschap.getGebruiker()!=null) {
						if (lidmaatschap.getGebruiker().getId()!= null) {
							gebruikerIdSet.add( lidmaatschap.getGebruiker().getId());
						}
					}
				}
			}
			if (gebruikersgroep.getOrganisatie()!=null
					&& gebruikersgroep.getOrganisatie().getId()!= null) {
				organisatieIdSet.add( gebruikersgroep.getOrganisatie().getId());
			}
			if (gebruikersgroep.getScope()!=null
					&& gebruikersgroep.getScope().getId()!= null) {
				groepscopeIdSet.add( gebruikersgroep.getScope().getId());
			}
			if (gebruikersgroep.getHoofdgroep()!=null
					&& gebruikersgroep.getHoofdgroep().getId()!= null) {
				hoofdgroepIdSet.add( gebruikersgroep.getHoofdgroep().getId());
			}
		}

		// zet de gebruikers in een hashmap om ze snel te vinden
		Map<String, Klant> gebruikerMap= new HashMap<String, Klant>();
		Map<String, Benoembaar> hoofdgroepMap= new HashMap<String, Benoembaar>();

		// initialiseer default (omwille van robuustheid)

		for (String id: gebruikerIdSet) {
			Klant gebruiker= new Klant();
			gebruiker.setId( id);
			gebruiker.setAchternaam("Onbekend");
			gebruikerMap.put( id, gebruiker);
		}	

		for (String id: hoofdgroepIdSet) {
			Benoembaar gebruikersgroep= new Benoembaar();
			gebruikersgroep.setId( id);
			gebruikersgroep.setNaam("Onbekend");
			hoofdgroepMap.put( id, gebruikersgroep);
		}	


		if (selectie.isSelectGebruikers() && !gebruikerIdSet.isEmpty()) {
			aTOLGebruikerDelegate.getGebruikers(
					gebruikerIdSet,
					gebruikerMap);
			if (selectie.isSelectOrganisaties()) {
				addOrganisatieIds( organisatieIdSet, gebruikerMap);
			}
		}


		Map<String, Organisatie> returnedOrganisatieMap= null;
		if (selectie.isSelectOrganisaties() && !organisatieIdSet.isEmpty()) {
			// calls naar OrganisatieService: de betreffende organisaties inclusief alle locaties
			returnedOrganisatieMap = aTOLOrganisatieDelegate.getOrganisaties( organisatieIdSet);
		}
		Map<String, Scope> returnedGroepscopesMap= null;
		if (selectie.isSelectScopes() && !groepscopeIdSet.isEmpty()) {

			returnedGroepscopesMap = ScopeServiceHelper.getScopes(getGroepscopeService(), groepscopeIdSet);
		}
		if (selectie.isSelectHoofdgroep() && !hoofdgroepIdSet.isEmpty()) {
			// converteer de set naar een list om ze als Dao te kunnen zoeken
			GRP_GetRequest request= new GRP_GetRequest();

			request.setIds(new ArrayList<String>( hoofdgroepIdSet));
			request.setSelectie( new GRP_Selectie());
			// Dao aanroep
			List<Groep> hoofdgroepen= gebruikersgroepDao.getGebruikersgroepen(request);
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

		if (selectie.isSelectGebruikers() && selectie.isSelectOrganisaties()) {
			for (Entry<String, Klant> gebruikerEntry: gebruikerMap.entrySet()) {
				List<Inschrijving> inschrijvingen= gebruikerEntry.getValue().getInschrijvingen();
				if (inschrijvingen!= null) {
					for (Inschrijving inschrijving: inschrijvingen) {
						OrganisatieDataEnrichmentHelper.enrich( returnedOrganisatieMap,
								inschrijving);
					}
				}
			}
		}

		for (Groep gebruikersgroep: gebruikersgroepen) {
			if (selectie.isSelectGebruikers()) {
				if (gebruikersgroep.getLidmaatschappen()!= null) {
					for (Lidmaatschap lidmaatschap: gebruikersgroep.getLidmaatschappen()) {
						if (lidmaatschap.getGebruiker()!=null && lidmaatschap.getGebruiker().getId()!= null) {

							String id= lidmaatschap.getGebruiker().getId();
							lidmaatschap.setGebruiker(gebruikerMap.get( id));
						}

					}
				}
			}						

			if (selectie.isSelectOrganisaties()) {
				OrganisatieDataEnrichmentHelper.enrich( returnedOrganisatieMap,
						gebruikersgroep);
			}
			if (selectie.isSelectScopes()) {
				ScopeDataEnrichmentHelper.enrichScope( returnedGroepscopesMap,
						gebruikersgroep);
			}
			if (selectie.isSelectHoofdgroep()) {
				if (gebruikersgroep.getHoofdgroep()!= null && gebruikersgroep.getHoofdgroep().getId()!= null) {
					gebruikersgroep.setHoofdgroep( hoofdgroepMap.get( gebruikersgroep.getHoofdgroep().getId()));
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

	public SCO_ScopeService getGroepscopeService() {
		return groepscopeService;
	}

	public void setGroepscopeService(SCO_ScopeService groepscopeService) {
		this.groepscopeService = groepscopeService;
	}


}
