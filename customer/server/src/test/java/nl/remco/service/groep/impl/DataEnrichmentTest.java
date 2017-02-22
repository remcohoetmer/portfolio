package nl.remco.service.groep.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import nl.remco.service.algemeen.TestHelper;
import nl.remco.service.common.model.Benoembaar;
import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.web.IDList;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.GroepsMutatieType;
import nl.remco.service.groep.model.Lidmaatschap.Rol;
import nl.remco.service.groep.web.GRP_CreateRequest;
import nl.remco.service.groep.web.GRP_CreateResponse;
import nl.remco.service.groep.web.GRP_GetRequest;
import nl.remco.service.groep.web.GRP_GetResponse;
import nl.remco.service.groep.web.GRP_GroepService;
import nl.remco.service.groep.web.GRP_LidmaatschapCreateUpdate;
import nl.remco.service.groep.web.GRP_Selectie;
import nl.remco.service.klant.model.Inschrijving;
import nl.remco.service.klant.model.Klant;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/testApplicationContext.xml"})	
public class DataEnrichmentTest {
	@Autowired
	private GRP_GroepService groepenService;
	@Autowired
	private TestHelper testHelper;

	@Before
	public void setup() {

		testHelper.initialise();
	}

	public GRP_GroepService getGroepenService() {
		return groepenService;
	}

	public void setGroepenService(
			GRP_GroepService groepenService) {
		this.groepenService = groepenService;
	}

	public TestHelper getTestHelper() {
		return testHelper;
	}

	public void setTestHelper(TestHelper testHelper) {
		this.testHelper = testHelper;
	}

	@Test
	public void testDataEnrichment() {

		String scopeId= testHelper.maakScope( "Corporate");

		Identifiable organisatie= new Identifiable( "8000");

		Identifiable gebruiker1Ref= new Identifiable("testklant1");
		Identifiable organisatieRef= new Identifiable(organisatie.getId());
		Identifiable scopeRef= new Identifiable(scopeId);

		GRP_CreateRequest request= new GRP_CreateRequest();
		request.setNaam( "Dynamische Groep");

		request.setOrganisatie(organisatieRef);
		request.setScope(scopeRef);
		request.setGroepsMutatieType( GroepsMutatieType.MANAGED);
		request.setGroepscode( "Code1");

		List<GRP_LidmaatschapCreateUpdate> lidmaatschappen= new ArrayList<GRP_LidmaatschapCreateUpdate>();
		request.setLidmaatschappen(lidmaatschappen);

		GRP_LidmaatschapCreateUpdate lidmaatschap1= new GRP_LidmaatschapCreateUpdate();

		lidmaatschap1.setKlant( gebruiker1Ref);
		lidmaatschap1.setRol( Rol.GROEPSLID);
		lidmaatschappen.add( lidmaatschap1);

		request.setLidmaatschappen(lidmaatschappen);

		GRP_CreateResponse response= groepenService.create(request);
		String groepId= response.getId();


		// Vraag de groep weer op
		GRP_GetRequest getRequest= new GRP_GetRequest();
		getRequest.setIds( IDList.create( groepId));
		getRequest.setSelectie( new GRP_Selectie());
		getRequest.getSelectie().setSelectLidmaatschappen( true);
		getRequest.getSelectie().setSelectOrganisaties( true);
		getRequest.getSelectie().setSelectKlanten( true);
		getRequest.getSelectie().setSelectScopes( true);

		GRP_GetResponse getResponse= groepenService.get(getRequest);

		Assert.assertEquals( 1, getResponse.getGroepen().size());
		Groep groep= getResponse.getGroepen().get(0);
		Assert.assertEquals( "Bedrijf A", ((Benoembaar)groep.getOrganisatie()).getNaam());
		Assert.assertEquals( "Corporate", ((Benoembaar)groep.getScope()).getNaam());
		
		Assert.assertEquals( 1, groep.getLidmaatschappen().size());
		Klant gebruiker= (Klant)getResponse.getGroepen().get(0).getLidmaatschappen().get(0).getKlant();

		Assert.assertEquals( 1, gebruiker.getInschrijvingen().size());
		Inschrijving inschrijving= gebruiker.getInschrijvingen().get(0);
		Assert.assertEquals( "Bedrijf A", ((Benoembaar)inschrijving.getOrganisatie()).getNaam());
	}

}
