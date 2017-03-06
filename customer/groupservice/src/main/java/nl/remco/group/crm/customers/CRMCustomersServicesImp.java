package nl.remco.group.crm.customers;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class CRMCustomersServicesImp implements CRMCustomersService  {


	@Override
	public SearchCustomersRes searchCustomers(
			SearchCustomersReq searchUsers) {
		CustomerProfile searchProfile= searchUsers.getCustomerProfile();
		SearchCustomersRes response= new SearchCustomersRes();


		if (searchProfile.getOrganisationNumber()!= null) {
			if (searchProfile.getOrganisationNumber().longValue()== 8000L) {
				addKlant(response, "testklant1", "Test1", "klant", 8000L);	
				addKlant(response, "testklant4", "Test4", "klant", 8000L);
			}
		} else {
			addKlant(response, "testklant1", "Test1", "klant", 8000L);	
			addKlant(response, "testklant2", "Test2", "klant", null);
			addKlant(response, "testklant3", "Test3", "klant", null);
			addKlant(response, "testklant4", "Test4", "klant", 8000L);
		}
		return response;
	}

	private void addKlant(RetrieveCustomersRes response, String id,
			String firstname, String lastname, Long organisatieId) {
		CustomerProfile user= new CustomerProfile();
		user.setUserId(id);
		user.setFirstname( firstname);
		user.setLastname( lastname);
		user.setGender( Gender.MALE);
		user.setDateOfBirth( new Date());
		user.setEMailAddress( "klant@example.org");
		user.setRole( "leerling");
		if (organisatieId!=null){
			user.setOrganisationNumber( new BigDecimal(organisatieId));
		}
		response.getCustomerProfiles().add( user);
	}


	private void addKlant(SearchCustomersRes response, String id,
			String firstname, String lastname, Long organisatieId) {
		CustomerProfile user= new CustomerProfile();
		user.setUserId(id);
		user.setFirstname( firstname);
		user.setLastname( lastname);
		user.setEMailAddress( "mail@m.c");
		user.setGender(Gender.FEMALE);
		user.setBSN( "BSN123");
		
		if (organisatieId!=null){
			user.setOrganisationNumber( new BigDecimal(organisatieId));
		} else {
			user.setOrganisationNumber( new BigDecimal(0));
		}
		response.getCustomerProfiles().add( user);
	}

	@Override
	public RetrieveCustomersRes getCustomerProfile(
			RetrieveCustomersReq getUserProfile) {
		RetrieveCustomersRes response= new RetrieveCustomersRes();

		for (String userId: getUserProfile.getUserIdList()){
			if (userId.equals("person1")) {
				addKlant(response, userId, "Test1", "klant", 8000L);
			} else if (userId.equals("person2")) {
				addKlant(response, userId, "Test2", "klant", 8001L);
			} else if (userId.equals("person3")) {
				addKlant(response, userId, "Test3", "klant", 8001L);
			} else if (userId.equals("person4")) {
				addKlant(response, userId, "Test4", "klant", 8000L);
			} else {
				addKlant(response, userId, "Voornaam", "Achternaam"+ getUserProfile.getUserIdList(), 8000L);
			}
		}
		return response;
	}


}
