package nl.remco.service.algemeen;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.web.GRP_GetRequest;
import nl.remco.service.groep.web.GRP_GetRequest.Filter;
import nl.remco.service.groep.web.GRP_GetResponse;
import nl.remco.service.groep.web.GRP_GroepService;


public class TestHelper {
	private static final Logger LOG = Logger.getLogger(TestHelper.class);
	
	@Autowired
	private GRP_GroepService groepService;


	public void deleteAllGroepen()
	{
		GRP_GetRequest request = new GRP_GetRequest();
		GRP_GetResponse response= getGroepService().get(request);
		List<Groep> groepen= response.getGroepen();

		Filter filter= new Filter();
		request.setFilter(filter);
		filter.setStatus(Status.Verwijderd);
		response= getGroepService().get(request);
		groepen.addAll( response.getGroepen());


		LOG.info("#testgroepen aangemaakt: " + groepen.size());

		for (Groep groep : groepen) {
			groepService.delete(groep);			
		}
	}


	public void initialise()
	{
		deleteAllGroepen();
	}



	public GRP_GroepService getGroepService() {
		return groepService;
	}

	public void setGebruikersgroepService(GRP_GroepService gebruikersgroepService) {
		this.groepService = gebruikersgroepService;
	}



}	
