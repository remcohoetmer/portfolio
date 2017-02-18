/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.remco.crm.customers;

import java.math.BigDecimal;
import java.util.Date;

public class CRMCustomersServicesImp implements CRMCustomersService  {


	@Override
	public SearchCustomersRes searchUsers(
			SearchCustomersReq searchUsers) {
		CustomerSearchProfile searchProfile= searchUsers.getUserProfile();
		SearchCustomersRes response= new SearchCustomersRes();


		if (searchProfile.getKvKNumber()!= null) {
			if (searchProfile.getKvKNumber().longValue()== 8000L) {
				addGebruiker(response, "testgebruiker1", "Test1", "Gebruiker", 8000L);	
				addGebruiker(response, "testgebruiker4", "Test4", "Gebruiker", 8000L);
			}
		} else {
			addGebruiker(response, "testgebruiker1", "Test1", "Gebruiker", 8000L);	
			addGebruiker(response, "testgebruiker2", "Test2", "Gebruiker", null);
			addGebruiker(response, "testgebruiker3", "Test3", "Gebruiker", null);
			addGebruiker(response, "testgebruiker4", "Test4", "Gebruiker", 8000L);
		}
		return response;
	}

	private void addGebruiker(RetrieveCustomersRes response, String id,
			String firstname, String lastname, Long organisatieId) {
		CustomerProfile user= new CustomerProfile();
		user.setUserId(id);
		user.setFirstname( firstname);
		user.setLastname( lastname);
		user.setGender( Gender.MALE);
		user.setDateOfBirth( new Date());
		user.setEMailAddress( "gebruiker@example.org");
		user.setBSN( "123");
		user.setRole( "leerling");
		if (organisatieId!=null){
			user.setKvKNumber( new BigDecimal(organisatieId));
		}
		response.getUserProfile().add( user);
	}


	private void addGebruiker(SearchCustomersRes response, String id,
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

	/* (non-Javadoc)
	 * @see nl.remco.crm.customers.CRMCustomersService#getUserProfile(nl.remco.crm.customers.RetrieveCustomersReq)
	 */
	@Override
	public RetrieveCustomersRes getUserProfile(
			RetrieveCustomersReq getUserProfile) {
		RetrieveCustomersRes response= new RetrieveCustomersRes();

		for (String userId: getUserProfile.getUserId()){
			if (userId.equals("testgebruiker1")) {
				addGebruiker(response, userId, "Test1", "Gebruiker", 8000L);
			} else if (userId.equals("testgebruiker2")) {
				addGebruiker(response, userId, "Test2", "Gebruiker", null);
			} else if (userId.equals("testgebruiker3")) {
				addGebruiker(response, userId, "Test3", "Gebruiker", null);
			} else if (userId.equals("testgebruiker4")) {
				addGebruiker(response, userId, "Test4", "Gebruiker", 8000L);
			} else {
				addGebruiker(response, userId, "Voornaam", "Achternaam"+ getUserProfile.getUserId(), 8000L);
			}
		}
		return response;
	}


}
