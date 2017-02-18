package nl.remco.service.gebruikersgroep.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.remco.service.algemeen.TestHelper;
import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.common.web.BadRequestException;
import nl.remco.service.common.web.IDList;
import nl.remco.service.groep.model.GroepsMutatieType;
import nl.remco.service.groep.model.Lidmaatschap.Rol;
import nl.remco.service.groep.web.GRP_CreateRequest;
import nl.remco.service.groep.web.GRP_CreateResponse;
import nl.remco.service.groep.web.GRP_GroepService;
import nl.remco.service.groep.web.GRP_GetRequest;
import nl.remco.service.groep.web.GRP_GetResponse;
import nl.remco.service.groep.web.GRP_LidmaatschapCreateUpdate;
import nl.remco.service.groep.web.GRP_Selectie;
import nl.remco.service.groep.web.GRP_UpdateRequest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.jersey.api.ConflictException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/testApplicationContext.xml"})	
public class AddLedenTest {
	@Autowired
	private GRP_GroepService groepenService;
	@Autowired
	private TestHelper testHelper;

	@Before
	public  void setup() {
		getTestHelper().initialise();
	}

	@After
	public void teardown() {
	}

	@Test
	public void test() {
	}
	
		//@Test
	public void testCreateGebruiker() {
		Identifiable organisatie= new Identifiable("8000");
		
		Identifiable gebruiker1Ref= new Identifiable( "testgebruiker1");
		Identifiable gebruiker2Ref= new Identifiable( "testgebruiker2");
		Identifiable gebruiker3Ref= new Identifiable( "testgebruiker3");

		GRP_CreateRequest request= new GRP_CreateRequest();
		request.setNaam( "Dynamische Groep");
		request.setOrganisatie(organisatie);

		request.setGroepsMutatieType( GroepsMutatieType.MANAGED);

		List<GRP_LidmaatschapCreateUpdate> lidmaatschappen= new ArrayList<GRP_LidmaatschapCreateUpdate>();
		request.setLidmaatschappen(lidmaatschappen);

		GRP_LidmaatschapCreateUpdate lidmaatschap1= new GRP_LidmaatschapCreateUpdate();
		lidmaatschap1.setKlant( gebruiker1Ref);
		lidmaatschap1.setRol( Rol.GROEPSLID);
		lidmaatschappen.add( lidmaatschap1);

		GRP_LidmaatschapCreateUpdate lidmaatschap2= new GRP_LidmaatschapCreateUpdate();
		lidmaatschap2.setKlant( gebruiker2Ref);
		lidmaatschap2.setRol( Rol.GROEPSLID);
		lidmaatschappen.add( lidmaatschap2);

		GRP_LidmaatschapCreateUpdate lidmaatschap3= new GRP_LidmaatschapCreateUpdate();
		lidmaatschap3.setKlant( gebruiker3Ref);
		lidmaatschap3.setRol( Rol.GROEPSLID);

		try {
			groepenService.create(request);
			Assert.fail( "Gebruiker 2 mag niet worden toegevoegd want hij zit niet in de goed organisatie");
		} catch (BadRequestException e){
			Assert.assertTrue( ((String) e.getResponse().getEntity()).contains("Test2"));
		}

		lidmaatschappen= new ArrayList<GRP_LidmaatschapCreateUpdate>();
		request.setLidmaatschappen(lidmaatschappen);
		lidmaatschappen.add( lidmaatschap1);
		lidmaatschappen.add( lidmaatschap3);
		try {
			groepenService.create(request);
			Assert.fail( "Gebruiker 3 mag niet worden toegevoegd");
		} catch (BadRequestException e){
			System.err.println(((String) e.getResponse().getEntity()));
			Assert.assertTrue( ((String) e.getResponse().getEntity()).contains("Test3"));
		}
		lidmaatschappen= new ArrayList<GRP_LidmaatschapCreateUpdate>();
		request.setLidmaatschappen(lidmaatschappen);
		lidmaatschappen.add( lidmaatschap1);

		GRP_CreateResponse response= groepenService.create(request);
		String gebruikersgroepId= response.getId();

		// probeer nu gebruiker 2 via de update functie
		GRP_UpdateRequest updateRequest= new GRP_UpdateRequest();
		updateRequest.setId( gebruikersgroepId);
		List<GRP_LidmaatschapCreateUpdate> createdLidmaatschappen= new ArrayList<GRP_LidmaatschapCreateUpdate>();
		updateRequest.setCreateLidmaatschappen( createdLidmaatschappen);

		createdLidmaatschappen.add(lidmaatschap2);

		try {
			groepenService.update( updateRequest);
			Assert.fail( "Gebruiker 2 mag niet worden toegevoegd.");
		} catch (BadRequestException e){
			Assert.assertTrue( ((String) e.getResponse().getEntity()).contains("Test2"));
		}

		// gebruiker 1 mag ook niet worden toegevoegd want hij maakt al deel uit
		createdLidmaatschappen= new ArrayList<GRP_LidmaatschapCreateUpdate>();
		updateRequest.setCreateLidmaatschappen( createdLidmaatschappen);
		createdLidmaatschappen.add( lidmaatschap1);
		try {
			groepenService.update( updateRequest);
			Assert.fail( "Gebruiker 1 mag niet worden toegevoegd");
		} catch (ConflictException e){
			Assert.assertTrue( ((String) e.getResponse().getEntity()).contains("Test1"));
		} 


		
		// Vraag de groep weer op
		GRP_GetRequest getRequest= new GRP_GetRequest();
		getRequest.setIds( IDList.create( gebruikersgroepId));
		getRequest.setSelectie( new GRP_Selectie());
		getRequest.getSelectie().setSelectLidmaatschappen( true);
		GRP_GetResponse getResponse= groepenService.get(getRequest);
		Assert.assertEquals( 1, getResponse.getGroepen().size());
		Assert.assertEquals( 1, getResponse.getGroepen().get(0).getLidmaatschappen().size());
		String lidmaatschapsId= getResponse.getGroepen().get(0).getLidmaatschappen().get(0).getId();
		Date lidmaatschapsIdModified= getResponse.getGroepen().get(0).getLidmaatschappen().get(0).getLaatstgewijzigd();

		// zet de lidmaatschap op Passief
		groepenService.update(maakUpdateLidmaatschapRequest(
				gebruikersgroepId, lidmaatschapsId,lidmaatschapsIdModified, Status.Passief));

		// controleer mutatie
		getResponse= groepenService.get(getRequest);
		Assert.assertEquals( 1, getResponse.getGroepen().size());
		Assert.assertEquals( 1, getResponse.getGroepen().get(0).getLidmaatschappen().size());
		Assert.assertEquals( Status.Passief,
				getResponse.getGroepen().get(0).getLidmaatschappen().get(0).getStatus());
		
		// nu verwijderen we het lidmaatschap
		
		lidmaatschapsIdModified= getResponse.getGroepen().get(0).getLidmaatschappen().get(0).getLaatstgewijzigd();
		groepenService.update(maakDeleteLidmaatschapRequest(
				gebruikersgroepId, lidmaatschapsId, lidmaatschapsIdModified));

		// Vraag de groep weer op, nu zijn er geen (actieve of passieve) leden
		getResponse= groepenService.get(getRequest);
		Assert.assertEquals( 1, getResponse.getGroepen().size());
		Assert.assertEquals( 0, getResponse.getGroepen().get(0).getLidmaatschappen().size());

		// nu gaat de update request wel goed want het eerdere lidmaatschap was verwijderd
		groepenService.update( updateRequest);
	}


	private GRP_UpdateRequest maakUpdateLidmaatschapRequest(
			String gebruikersgroepId, String lidmaatschapsId, Date lidmaatschapsIdModified, Status status) {
		GRP_UpdateRequest updateRequest= new GRP_UpdateRequest();
		updateRequest.setId( gebruikersgroepId);
		List<GRP_LidmaatschapCreateUpdate> updatedLidmaatschappen= new ArrayList<GRP_LidmaatschapCreateUpdate>();
		updateRequest.setUpdateLidmaatschappen( updatedLidmaatschappen);

		GRP_LidmaatschapCreateUpdate lidmaatschapsStatusUpdate= new GRP_LidmaatschapCreateUpdate();
		lidmaatschapsStatusUpdate.setId( lidmaatschapsId);
		lidmaatschapsStatusUpdate.setLaatstgewijzigd(lidmaatschapsIdModified);
		
		lidmaatschapsStatusUpdate.setStatus( status);
		updatedLidmaatschappen.add(lidmaatschapsStatusUpdate);
		return updateRequest;
	}
	private GRP_UpdateRequest maakDeleteLidmaatschapRequest(
			String gebruikersgroepId, String lidmaatschapsId, Date lidmaatschapsIdModified) {
		GRP_UpdateRequest updateRequest= new GRP_UpdateRequest();
		updateRequest.setId( gebruikersgroepId);
		List<Identifiable> deletedLidmaatschappen= new ArrayList<Identifiable>();
		updateRequest.setDeleteLidmaatschappen( deletedLidmaatschappen);

		Identifiable lidmaatschapsDelete= new Identifiable();
		lidmaatschapsDelete.setId( lidmaatschapsId);
		lidmaatschapsDelete.setLaatstgewijzigd(lidmaatschapsIdModified);
		deletedLidmaatschappen.add(lidmaatschapsDelete);
		return updateRequest;
	}

	public GRP_GroepService getGebruikersgroepenService() {
		return groepenService;
	}

	public void setGebruikersgroepenService(GRP_GroepService gebruikersgroepenService) {
		this.groepenService = gebruikersgroepenService;
	}

	public TestHelper getTestHelper() {
		return testHelper;
	}

	public void setTestHelper(TestHelper testHelper) {
		this.testHelper = testHelper;
	}


}
