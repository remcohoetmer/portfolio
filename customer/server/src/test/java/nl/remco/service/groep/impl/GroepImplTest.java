package nl.remco.service.groep.impl;

import java.util.ArrayList;
import java.util.List;

import nl.remco.service.algemeen.TestHelper;
import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.common.web.IDList;
import nl.remco.service.common.web.RequestContext;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.GroepsMutatieType;
import nl.remco.service.groep.model.Lidmaatschap;
import nl.remco.service.groep.model.Lidmaatschap.Rol;
import nl.remco.service.groep.web.GRP_CreateRequest;
import nl.remco.service.groep.web.GRP_CreateResponse;
import nl.remco.service.groep.web.GRP_KlantMetGroepen;
import nl.remco.service.groep.web.GRP_GroepService;
import nl.remco.service.groep.web.GRP_GetRequest;
import nl.remco.service.groep.web.GRP_GetResponse;
import nl.remco.service.groep.web.GRP_LidmaatschapCreateUpdate;
import nl.remco.service.groep.web.GRP_LidmaatschapMetGroep;
import nl.remco.service.groep.web.GRP_SearchForKlantRequest;
import nl.remco.service.groep.web.GRP_Selectie;
import nl.remco.service.groep.web.GRP_GetRequest.Filter;
import nl.remco.service.utils.Util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/testApplicationContext.xml"})	
public class GroepImplTest {
	
	@Autowired
	private GRP_GroepService groepService;
	@Autowired
	private TestHelper testHelper;
	
	@Before
	public void setup() {
		RequestContext context= new RequestContext();
		context.setKlant(new Identifiable("0"));
		testHelper.initialise();
	}
	final static String geplandePeriode= "april-mei 2017";

	@Test
	public void testCreateKlant() {
		GRP_CreateRequest request= new GRP_CreateRequest();

		request.setNaam( "Groepie");
		request.setBeschrijving( "Beschrijving");
		request.setGeplandePeriode(geplandePeriode);

		Identifiable organisatie=  new Identifiable("8000");

		request.setScope(new Identifiable("1123"));
		request.setOrganisatie(organisatie);

		request.setGroepsMutatieType( GroepsMutatieType.MANAGED);
		List<String> kenmerken= new ArrayList<String>();
		kenmerken.add( "KK1");
		kenmerken.add( "KK2");
		request.setKenmerken(kenmerken);

		List<GRP_LidmaatschapCreateUpdate> lidmaatschappen= new ArrayList<GRP_LidmaatschapCreateUpdate>();
		GRP_LidmaatschapCreateUpdate lidmaatschap= new GRP_LidmaatschapCreateUpdate();
		Identifiable gebruiker= new Identifiable("klant1");
		lidmaatschap.setKlant( gebruiker);

		lidmaatschap.setRol( Rol.GROEPSLID);
		lidmaatschappen.add( lidmaatschap);
		request.setLidmaatschappen(lidmaatschappen);

		request.setProduct( "Fiets");
		request.setGroepscode( "Code2");

		GRP_CreateResponse response= groepService.create(request);
		String hoofdgroepId= response.getId();
		Assert.assertNotNull( hoofdgroepId);


		GRP_CreateRequest subCreateRequest= new GRP_CreateRequest();
		subCreateRequest.setNaam( "mail");
		subCreateRequest.setBeschrijving( "Marketing Mailgroep");
		subCreateRequest.setGeplandePeriode("juni217");
		subCreateRequest.setScope(new Identifiable("1123"));

		subCreateRequest.setGroepsMutatieType( GroepsMutatieType.SELFSERVICE);

		subCreateRequest.setHoofdgroep(new Identifiable(hoofdgroepId));

		subCreateRequest.setOrganisatie(organisatie);

		List<GRP_LidmaatschapCreateUpdate> lidmaatschappenZelfRegistratie= new ArrayList<GRP_LidmaatschapCreateUpdate>();
		GRP_LidmaatschapCreateUpdate lidmaatschapSelfService= new GRP_LidmaatschapCreateUpdate();
		Identifiable gebruikerSS= new Identifiable("testklant4");
		lidmaatschapSelfService.setKlant( gebruikerSS);
		lidmaatschapSelfService.setRol( Rol.GROEPSLEIDER);
		lidmaatschappenZelfRegistratie.add( lidmaatschapSelfService);
		subCreateRequest.setLidmaatschappen(lidmaatschappenZelfRegistratie);
		subCreateRequest.setProduct( "Banaan");
		subCreateRequest.setGroepscode( "Codesub");

		GRP_CreateResponse subCreateResponse= groepService.create(subCreateRequest);
		String subgroepId= subCreateResponse.getId();
		Assert.assertNotNull( subgroepId);

		GRP_GetRequest getRequest= new GRP_GetRequest();
		getRequest.setIds( IDList.create( hoofdgroepId));
		GRP_GetResponse getResponse= groepService.get(getRequest);
		Assert.assertEquals( 1, getResponse.getGroepen().size());

		GRP_GetRequest searchRequest= new GRP_GetRequest();
		GRP_Selectie selectie= new GRP_Selectie();
		searchRequest.setSelectie(selectie);
		selectie.setSelectLidmaatschappen( true);
		selectie.setSelectKenmerken( true);

		GRP_GetResponse searchResponse= groepService.get(searchRequest);
		Assert.assertEquals( 2, searchResponse.getGroepen().size());
		searchRequest.setFilter( new Filter());
		searchRequest.getFilter().setGeplandePeriode(geplandePeriode);
		searchResponse= groepService.get(searchRequest);
		Assert.assertEquals( 1, searchResponse.getGroepen().size());
		Groep returnedGroep= searchResponse.getGroepen().get(0);

		Assert.assertEquals( 2, returnedGroep.getKenmerken().size());
		Assert.assertEquals( "KK1", returnedGroep.getKenmerken().get(0));

		Assert.assertEquals( 1, returnedGroep.getLidmaatschappen().size());
		Lidmaatschap returnedLidmaatschap= returnedGroep.getLidmaatschappen().get(0);
		Assert.assertEquals( "klant1", returnedLidmaatschap.getKlant().getId());
		Assert.assertEquals( Rol.GROEPSLID, returnedLidmaatschap.getRol());
		Assert.assertEquals( Status.Actief, returnedLidmaatschap.getStatus());

		// zoeken op kenmerken
		searchRequest= new GRP_GetRequest();
		searchRequest.setFilter( new Filter());
		List<String> searchKenmerken= new ArrayList<String>();
		searchKenmerken.add( "KK1");
		searchKenmerken.add( "KK3");
		searchRequest.getFilter().setKenmerken( searchKenmerken);
		searchResponse= groepService.get(searchRequest);
		Assert.assertEquals( 1, searchResponse.getGroepen().size());

		GRP_SearchForKlantRequest searchForKlantenRequest= new GRP_SearchForKlantRequest();

		searchForKlantenRequest.setFilter( new GRP_SearchForKlantRequest.Filter());
		searchForKlantenRequest.getFilter().setKlantId( "klant1");

		GRP_KlantMetGroepen searchForKlantenResponse= groepService.searchForKlanten(searchForKlantenRequest);
		List<GRP_LidmaatschapMetGroep> gebrLidmaatschappen= searchForKlantenResponse.getLidmaatschappen();
		Assert.assertTrue( Util.isDefined( gebrLidmaatschappen));
		Groep gebrGroep= gebrLidmaatschappen.get(0).getGroep();
		Assert.assertNotNull( gebrGroep);
		Assert.assertNotNull( gebrLidmaatschappen.get(0).getId());
		Assert.assertNotNull( gebrLidmaatschappen.get(0).getLaatstgewijzigd());

	}

	public GRP_GroepService getGroepService() {
		return groepService;
	}

	public void setGroepenService(GRP_GroepService groepService) {
		this.groepService = groepService;
	}
}
