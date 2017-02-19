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

	public List<Klant> getKlanten( Set<String> klantIds) {

		RetrieveCustomersReq getCustomerProfile= new RetrieveCustomersReq();
		getCustomerProfile.getUserId().addAll(klantIds);
		RetrieveCustomersRes getUserCustomerResponse= getCRMCustomersServices().getCustomerProfile(getCustomerProfile);
		if (klantIds.size()!=getUserCustomerResponse.getUserProfile().size()) {
			throw new BadRequestException( "Customer IDs niet geldig bij CRM");
		}
		List<Klant> klanten = new ArrayList<Klant>();
		for (CustomerProfile user: getUserCustomerResponse.getUserProfile()) {
			convert( klanten, user);
		}

		return klanten;
	}


	public Map<String, Klant> getKlanten(
			Set<String> klantIdSet,
			Map<String, Klant> klantrMap) {

		// Service aanroep
		List<Klant> klanten = getKlanten( klantIdSet);

		if (klantrMap == null) {
			klantrMap = new HashMap<String, Klant>();
		}

		for (Klant gebruiker : klanten) {
			klantrMap.put(gebruiker.getId(), gebruiker);
		}
		return klantrMap;
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
		SearchCustomersReq searchCustomers= new SearchCustomersReq();
		CustomerSearchProfile customerSearchProfile= new CustomerSearchProfile();
		customerSearchProfile.setFirstname(filter.getVoornaam());
		customerSearchProfile.setLastname( filter.getAchternaam());
		customerSearchProfile.setDateOfBirth(  filter.getGeboortedatum());
		customerSearchProfile.setEMailAddress( filter.getEmailAdres());

		if (filter.getOrganisatie()!= null && filter.getOrganisatie().getId() != null) {
			customerSearchProfile.setKvKNumber( new BigDecimal( filter.getOrganisatie().getId()));
		}

		searchCustomers.setCustomerProfile( customerSearchProfile);
		SearchCustomersRes searchUsersResponse= getCRMCustomersServices().searchCustomers(searchCustomers);

		//klantMap.putAll(klanten.stream().collect( Collectors.toMap( Klant::getId, Function.identity())));
		
		List<Klant> klanten= new ArrayList<Klant>();
		for (CustomerProfileSummary userProfile: searchUsersResponse.getUserProfileLite()) {

			convert( klanten, userProfile);
		}
		return klanten;

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
	private void convert(List<Klant> klanten, CustomerProfile customerProfile) {
		if (customerProfile.getUserId()!=null) {
			Klant klant= new Klant();
			klanten.add(klant);

			klant.setId( customerProfile.getUserId());
			klant.setVoornaam( customerProfile.getFirstname());
			klant.setAchternaam(  customerProfile.getLastname());
			klant.setVoorvoegselAchternaam( customerProfile.getMiddlename());

			klant.setGeboortedatum( customerProfile.getDateOfBirth());
			klant.setGeslacht( convert( customerProfile.getGender()));
			klant.setEmailAdres( customerProfile.getEMailAddress());

			if (customerProfile.getKvKNumber()!= null) {
				Inschrijving inschrijving= new Inschrijving();
				Identifiable organisatie= new Identifiable();
				organisatie.setId(customerProfile.getKvKNumber().toString());
				inschrijving.setOrganisatie(organisatie);
				
				klant.setInschrijvingen( new ArrayList<Inschrijving>());
				klant.getInschrijvingen().add(inschrijving);
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
