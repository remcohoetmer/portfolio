package nl.remco.service.scope.impl;

import nl.remco.service.algemeen.TestHelper;
import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.common.web.IDList;
import nl.remco.service.scope.model.Scope;
import nl.remco.service.scope.web.SCO_CreateRequest;
import nl.remco.service.scope.web.SCO_CreateResponse;
import nl.remco.service.scope.web.SCO_GetRequest;
import nl.remco.service.scope.web.SCO_GetResponse;
import nl.remco.service.scope.web.SCO_ScopeService;
import nl.remco.service.scope.web.SCO_Selectie;
import nl.remco.service.scope.web.SCO_UpdateRequest;
import nl.remco.service.scope.web.SCO_GetRequest.Filter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/testApplicationContext.xml"})	
public class ScopeServiceImplTest {
	@Autowired
	private SCO_ScopeService scopeService;
	@Autowired
	private TestHelper testHelper;
	
	private Scope verwijderde;


	@Test
	public void testScope() {

		testHelper.deleteAllScopes();

		SCO_CreateRequest request1 = new SCO_CreateRequest();
		request1.setNaam( "Geo");

		SCO_CreateResponse response1= getScopeService().create(request1);
		Assert.assertNotNull( response1.getId());
		@SuppressWarnings("unused")
		String geoId= response1.getId();

		SCO_CreateRequest request2 = new SCO_CreateRequest();
		request2.setNaam( "Schooltas");


		SCO_CreateResponse response2= getScopeService().create(request2);
		String schooltasId= response2.getId();
		Assert.assertNotNull( schooltasId);


		SCO_GetRequest searchRequestGeo= new SCO_GetRequest();
		Filter filter= new Filter();
		filter.setNaam( "Geo");
		searchRequestGeo.setFilter(filter);

		SCO_Selectie selectie= new SCO_Selectie();
		searchRequestGeo.setSelectie(selectie);

		SCO_GetResponse response3= scopeService.get(searchRequestGeo);
		Assert.assertEquals( 1, response3.getScopes().size());

		SCO_GetRequest schooltasRequest= new SCO_GetRequest();
		schooltasRequest.setIds( IDList.create( schooltasId));
		Identifiable schooltas= scopeService.get( schooltasRequest).getScopes().get(0);
		
		/*
		 * Update test: 
		 * 1: verandering naam

		 */
		SCO_UpdateRequest updateRequest= new SCO_UpdateRequest();
		updateRequest.setId( schooltas.getId());
		updateRequest.setLaatstgewijzigd( schooltas.getLaatstgewijzigd());
		updateRequest.setNaam( "Rugzak");
		getScopeService().update(  updateRequest);

		SCO_GetRequest requestGetSchooltas= new SCO_GetRequest();
		requestGetSchooltas.setIds( IDList.create( schooltasId));
		SCO_GetResponse response5= getScopeService().get( requestGetSchooltas);
		Assert.assertEquals( 1, response5.getScopes().size());
		Assert.assertEquals( "Rugzak", response5.getScopes().get(0).getNaam());


		/*
		 * 	Schooltas wordt logisch verwijderd	
		 */

		schooltas= scopeService.get( schooltasRequest).getScopes().get(0);
		SCO_UpdateRequest logischVerwijderRequest= new SCO_UpdateRequest();
		logischVerwijderRequest.setId( schooltas.getId());
		logischVerwijderRequest.setLaatstgewijzigd( schooltas.getLaatstgewijzigd());
		logischVerwijderRequest.setStatus( Status.Verwijderd);

		getScopeService().update(  logischVerwijderRequest);

		// je kan schooltas nog wel vinden op ID, niet meer met zoeken
		SCO_GetResponse response6= getScopeService().get( requestGetSchooltas);
		Assert.assertEquals( 1, response6.getScopes().size());

		verwijderde = response6.getScopes().get(0);
		Assert.assertEquals( Status.Verwijderd, verwijderde.getStatus());

		SCO_GetRequest searchRequest= new SCO_GetRequest();
		searchRequest.setFilter( new Filter());
		
		SCO_GetResponse response7= getScopeService().get(searchRequest);
		Assert.assertEquals( 1, response7.getScopes().size());

		/*
		 * 	Schooltas wordt fysiek verwijderd
		 */
		schooltas= scopeService.get( schooltasRequest).getScopes().get(0);
		getScopeService().delete( schooltas);

		// je kan hem nu ook niet meer op ID vinden
		SCO_GetResponse organisatiesResponse= getScopeService().get( requestGetSchooltas);

		Assert.assertEquals(0, organisatiesResponse.getScopes().size());
	}


	public SCO_ScopeService getScopeService() {
		return scopeService;
	}

	public void setScopeService(SCO_ScopeService scopeService) {
		this.scopeService = scopeService;
	}
}
