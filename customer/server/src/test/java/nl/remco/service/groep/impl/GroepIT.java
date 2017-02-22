package nl.remco.service.groep.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import nl.remco.service.algemeen.TestHelper;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.web.GRP_GetRequest;
import nl.remco.service.groep.web.GRP_GetRequest.Filter;
import nl.remco.service.groep.web.GRP_GetResponse;
import nl.remco.service.groep.web.GRP_GroepService;
import nl.remco.service.groep.web.GRP_Selectie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/testApplicationContext.xml"})	
public class GroepIT {
	
	@Autowired
	private GRP_GroepService groepService;
	@Autowired
	private TestHelper testHelper;
	

	@Test
	public void testCreateKlant() {
		GRP_GetRequest searchRequest= new GRP_GetRequest();
		GRP_Selectie selectie= new GRP_Selectie();
		searchRequest.setSelectie(selectie);
		selectie.setSelectScopes( true);
		selectie.setSelectOrganisaties( true);

		searchRequest.setFilter( new Filter());
		
		GRP_GetResponse searchResponse= groepService.get(searchRequest);
		Assert.assertEquals( 2, searchResponse.getGroepen().size());
		Groep returnedGroep= searchResponse.getGroepen().get(0);

	}

	public GRP_GroepService getGroepService() {
		return groepService;
	}

	public void setGroepenService(GRP_GroepService groepService) {
		this.groepService = groepService;
	}
}
