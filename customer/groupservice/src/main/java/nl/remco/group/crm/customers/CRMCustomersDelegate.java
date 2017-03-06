package nl.remco.group.crm.customers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.remco.group.service.dto.PersonDTO;
import nl.remco.service.common.web.BadRequestException;
import nl.remco.service.klant.model.Geslacht;
import nl.remco.service.klant.web.KLA_GetRequest;
import nl.remco.service.klant.web.KLA_GetRequest.Filter;

@Component
public class CRMCustomersDelegate {
	@Autowired
	private CRMCustomersService cRMCustomersServices;

	public CompletableFuture<PersonDTO> findPerson( String id) {

		RetrieveCustomersReq getCustomerProfile= new RetrieveCustomersReq();
		getCustomerProfile.getUserIdList().add(id);
		RetrieveCustomersRes getUserCustomerResponse= getCRMCustomersServices().getCustomerProfile(getCustomerProfile);
		if (1!=getUserCustomerResponse.getCustomerProfiles().size()) {
			throw new BadRequestException( "Customer IDs niet geldig bij CRM");
		}
		
		return CompletableFuture.supplyAsync( () ->convert( getUserCustomerResponse.getCustomerProfiles().get(0)));
	}


	public List<PersonDTO> get(KLA_GetRequest request) {

		return searchKlanten(request);

	}

	private List<PersonDTO> searchKlanten(KLA_GetRequest request) {
		Filter filter= request.getFilter();
		SearchCustomersReq searchCustomers= new SearchCustomersReq();
		CustomerProfile customerSearchProfile= new CustomerProfile();
		customerSearchProfile.setFirstname(filter.getVoornaam());
		customerSearchProfile.setLastname( filter.getAchternaam());
		customerSearchProfile.setDateOfBirth(  filter.getGeboortedatum());
		customerSearchProfile.setEMailAddress( filter.getEmailAdres());

		if (filter.getOrganisatie()!= null && filter.getOrganisatie().getId() != null) {
			customerSearchProfile.setOrganisationNumber( new BigDecimal( filter.getOrganisatie().getId()));
		}

		searchCustomers.setCustomerProfile( customerSearchProfile);
		SearchCustomersRes searchUsersResponse= getCRMCustomersServices().searchCustomers(searchCustomers);


		List<PersonDTO> klanten= new ArrayList<PersonDTO>();
		for (CustomerProfile userProfile: searchUsersResponse.getCustomerProfiles()) {

			convert( klanten, userProfile);
		}
		return klanten;

	}

	private void convert(List<PersonDTO> persons, CustomerProfile customerProfile) {
		if (customerProfile.getUserId()!=null) {
			persons.add(convert( customerProfile));
		}
	}
	static PersonDTO convert(CustomerProfile customerProfile) {
		PersonDTO klant= new PersonDTO();

		klant.setId( customerProfile.getUserId());
		klant.setName( customerProfile.getLastname());
		/*
		klant.setVoornaam( customerProfile.getFirstname());
		klant.setAchternaam(  customerProfile.getLastname());
		klant.setVoorvoegselAchternaam( customerProfile.getMiddlename());
		klant.setVoorletters("");

		klant.setGeboortedatum( customerProfile.getDateOfBirth());
		klant.setGeslacht( convert( customerProfile.getGender()));
		klant.setEmailAdres( customerProfile.getEMailAddress());

		if (customerProfile.getOrganisationNumber()!= null) {
			Inschrijving inschrijving= new Inschrijving();
			Organisatie organisatie= new Organisatie();
			organisatie.setId(customerProfile.getOrganisationNumber().toString());
			inschrijving.setOrganisatie(organisatie);

			klant.setInschrijvingen( new ArrayList<Inschrijving>());
			klant.getInschrijvingen().add(inschrijving);
		}*/
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
