package nl.remco.service.klant.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import nl.remco.service.klant.model.Geslacht;
import nl.remco.service.klant.model.Inschrijving;
import nl.remco.service.klant.model.Klant;
import nl.remco.service.klant.web.KLA_GetRequest;
import nl.remco.service.klant.web.KLA_GetRequest.Filter;

public class CRMCustomersDelegate {
	@Autowired
	private CRMCustomersService cRMCustomersServices;

	public List<Klant> getKlanten( Set<String> klantIds) {

		RetrieveCustomersReq getCustomerProfile= new RetrieveCustomersReq();
		getCustomerProfile.getUserIdList().addAll(klantIds);
		RetrieveCustomersRes getUserCustomerResponse= getCRMCustomersServices().getCustomerProfile(getCustomerProfile);
		if (klantIds.size()!=getUserCustomerResponse.getCustomerProfiles().size()) {
			throw new BadRequestException( "Customer IDs niet geldig bij CRM");
		}
		
		return getUserCustomerResponse.getCustomerProfiles()
				.stream()
				.map(CRMCustomersDelegate::convert)
				.collect( Collectors.toList());
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


		List<Klant> klanten= new ArrayList<Klant>();
		for (CustomerProfileSummary userProfile: searchUsersResponse.getCustomerProfileSummary()) {

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
	static Klant convert(CustomerProfile customerProfile) {
		Klant klant= new Klant();

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
		return klant;
	}

	private static Geslacht convert(Gender gender) {
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
