package nl.remco.crm.customers;

import java.math.BigDecimal;
import java.util.Date;

public class CRMCustomersServicesImp implements CRMCustomersService  {


	@Override
	public SearchCustomersRes searchCustomers(
			SearchCustomersReq searchUsers) {
		CustomerSearchProfile searchProfile= searchUsers.getCustomerProfile();
		SearchCustomersRes response= new SearchCustomersRes();


		if (searchProfile.getKvKNumber()!= null) {
			if (searchProfile.getKvKNumber().longValue()== 8000L) {
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
		user.setBSN( "123");
		user.setRole( "leerling");
		if (organisatieId!=null){
			user.setKvKNumber( new BigDecimal(organisatieId));
		}
		response.getUserProfile().add( user);
	}


	private void addKlant(SearchCustomersRes response, String id,
			String firstname, String lastname, Long organisatieId) {
		CustomerProfileSummary user= new CustomerProfileSummary();
		user.setUserId(id);
		user.setFirstname( firstname);
		user.setLastname( lastname);
		user.setBSN( "BSN123");
		
		if (organisatieId!=null){
			user.setKvKNumber( new BigDecimal(organisatieId));
		} else {
			user.setKvKNumber( new BigDecimal(0));
		}
		response.getUserProfileLite().add( user);
	}

	@Override
	public RetrieveCustomersRes getCustomerProfile(
			RetrieveCustomersReq getUserProfile) {
		RetrieveCustomersRes response= new RetrieveCustomersRes();

		for (String userId: getUserProfile.getUserId()){
			if (userId.equals("testklant1")) {
				addKlant(response, userId, "Test1", "klant", 8000L);
			} else if (userId.equals("testklant2")) {
				addKlant(response, userId, "Test2", "klant", null);
			} else if (userId.equals("testklant3")) {
				addKlant(response, userId, "Test3", "klant", null);
			} else if (userId.equals("testklant4")) {
				addKlant(response, userId, "Test4", "klant", 8000L);
			} else {
				addKlant(response, userId, "Voornaam", "Achternaam"+ getUserProfile.getUserId(), 8000L);
			}
		}
		return response;
	}


}
