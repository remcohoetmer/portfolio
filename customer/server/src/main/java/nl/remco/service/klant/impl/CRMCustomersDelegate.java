package nl.remco.service.klant.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import nl.remco.crm.customers.CRMCustomersService;
import nl.remco.crm.customers.CustomerProfile;
import nl.remco.crm.customers.CustomerProfileSummary;
import nl.remco.crm.customers.CustomerSearchProfile;
import nl.remco.crm.customers.Gender;
import nl.remco.crm.customers.RetrieveCustomersReq;
import nl.remco.crm.customers.RetrieveCustomersRes;
import nl.remco.crm.customers.SearchCustomersReq;
import nl.remco.crm.customers.SearchCustomersRes;
import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.web.BadRequestException;
import nl.remco.service.klant.model.Klant;
import nl.remco.service.klant.model.Geslacht;
import nl.remco.service.klant.model.Inschrijving;
import nl.remco.service.klant.web.KLA_GetRequest;
import nl.remco.service.klant.web.KLA_GetRequest.Filter;

public class CRMCustomersDelegate {
	@Autowired
	private CRMCustomersService cRMCustomersServices;

	public List<Klant> getKlanten( Set<String> gebruikerIds) {

		RetrieveCustomersReq getUserProfile= new RetrieveCustomersReq();
		getUserProfile.getUserId().addAll(gebruikerIds);
		RetrieveCustomersRes getUserProfileResponse= getCRMCustomersServices().getUserProfile(getUserProfile);
		if (gebruikerIds.size()!=getUserProfileResponse.getUserProfile().size()) {
			throw new BadRequestException( "Gebruiker IDs niet geldig bij ATOL");
		}
		List<Klant> gebruikers = new ArrayList<Klant>();
		for (CustomerProfile user: getUserProfileResponse.getUserProfile()) {
			convert( gebruikers, user);
		}

		return gebruikers;
	}


	public Map<String, Klant> getGebruikers(
			Set<String> gebruikerIdSet,
			Map<String, Klant> gebruikerMap) {

		// Service aanroep
		List<Klant> gebruikers = getKlanten( gebruikerIdSet);

		if (gebruikerMap == null) {
			gebruikerMap = new HashMap<String, Klant>();
		}

		for (Klant gebruiker : gebruikers) {
			gebruikerMap.put(gebruiker.getId(), gebruiker);
		}
		return gebruikerMap;
	}

	public List<Klant> get(KLA_GetRequest request) {

		if (request.getIds()!= null) {
			return getKlanten(new HashSet<String>( request.getIds()));
		} else {
			return searchKlanten(request);
		}

	}

	private List<Klant> searchKlanten(KLA_GetRequest request) {
		Filter filter= request.getFilter();
		SearchCustomersReq searchUsers= new SearchCustomersReq();
		CustomerSearchProfile userSearchProfile= new CustomerSearchProfile();
		userSearchProfile.setFirstname(filter.getVoornaam());
		userSearchProfile.setLastname( filter.getAchternaam());
		userSearchProfile.setDateOfBirth(  filter.getGeboortedatum());
		userSearchProfile.setEMailAddress( filter.getEmailAdres());

		if (filter.getOrganisatie()!= null && filter.getOrganisatie().getId() != null) {
			userSearchProfile.setKvKNumber( new BigDecimal( filter.getOrganisatie().getId()));
		}

		searchUsers.setUserProfile( userSearchProfile);
		SearchCustomersRes searchUsersResponse= getCRMCustomersServices().searchUsers(searchUsers);

		List<Klant> gebruikers= new ArrayList<Klant>();
		for (CustomerProfileSummary userProfile: searchUsersResponse.getUserProfileLite()) {

			convert( gebruikers, userProfile);
		}
		return gebruikers;

	}

	private void convert(List<Klant> gebruikers, CustomerProfileSummary userProfile) {
		if (userProfile.getUserId()!=null) {
			Klant gebruiker= new Klant();
			gebruikers.add(gebruiker);

			gebruiker.setId( userProfile.getUserId());
			gebruiker.setVoornaam( userProfile.getFirstname());
			gebruiker.setAchternaam(  userProfile.getLastname());
			gebruiker.setVoorvoegselAchternaam( userProfile.getMiddlename());


			if (userProfile.getKvKNumber()!= null) {
				Inschrijving inschrijving= new Inschrijving();
				Identifiable organisatie= new Identifiable();
				organisatie.setId(userProfile.getKvKNumber().toString());
				inschrijving.setOrganisatie(organisatie);
			}

		}
	}
	private void convert(List<Klant> gebruikers, CustomerProfile userProfile) {
		if (userProfile.getUserId()!=null) {
			Klant gebruiker= new Klant();
			gebruikers.add(gebruiker);

			gebruiker.setId( userProfile.getUserId());
			gebruiker.setVoornaam( userProfile.getFirstname());
			gebruiker.setAchternaam(  userProfile.getLastname());
			gebruiker.setVoorvoegselAchternaam( userProfile.getMiddlename());

			gebruiker.setGeboortedatum( userProfile.getDateOfBirth());
			gebruiker.setGeslacht( convert( userProfile.getGender()));
			gebruiker.setEmailAdres( userProfile.getEMailAddress());

			if (userProfile.getKvKNumber()!= null) {
				Inschrijving inschrijving= new Inschrijving();
				Identifiable organisatie= new Identifiable();
				organisatie.setId(userProfile.getKvKNumber().toString());
				inschrijving.setOrganisatie(organisatie);
				
				gebruiker.setInschrijvingen( new ArrayList<Inschrijving>());
				gebruiker.getInschrijvingen().add(inschrijving);
			}

		}
	}

	private Geslacht convert(Gender gender) {
		if (gender!=null) {
			switch (gender){
			case MALE: return Geslacht.Man;
			case FEMALE: return Geslacht.Vrouw;
			}
		}
		return null;
	}


	public CRMCustomersService getCRMCustomersServices() {
		return cRMCustomersServices;
	}


	public void setCRMCustomersServices(CRMCustomersService cRMCustomersServices) {
		this.cRMCustomersServices = cRMCustomersServices;
	}
}
