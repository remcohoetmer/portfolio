package nl.remco.service.algemeen;

import java.util.List;

import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.web.GRP_GroepService;
import nl.remco.service.groep.web.GRP_GetRequest;
import nl.remco.service.groep.web.GRP_GetResponse;
import nl.remco.service.groep.web.GRP_GetRequest.Filter;
import nl.remco.service.scope.model.Scope;
import nl.remco.service.scope.web.SCO_CreateRequest;
import nl.remco.service.scope.web.SCO_CreateResponse;
import nl.remco.service.scope.web.SCO_GetRequest;
import nl.remco.service.scope.web.SCO_GetResponse;
import nl.remco.service.scope.web.SCO_ScopeService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class TestHelper {
	private static final Logger LOG = Logger.getLogger(TestHelper.class);
	
	@Autowired
	private GRP_GroepService groepService;
	@Autowired
	private SCO_ScopeService groepscopeService;

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


	public void deleteAllScopes() {
		SCO_GetRequest request = new SCO_GetRequest();
		SCO_GetResponse response= getScopeService().get(request);
		List<Scope> groepscopes= response.getScopes();

		request.setFilter( new nl.remco.service.scope.web.SCO_GetRequest.Filter());
		request.getFilter().setStatus(Status.Verwijderd);
		response= getScopeService().get(request);
		groepscopes.addAll( response.getScopes());

		LOG.info("#test scopes aangemaakt: " + groepscopes.size());
		for (Scope scope : groepscopes) {
			getScopeService().delete( scope);
		}
	}

	public void initialise()
	{
		deleteAllScopes();
		deleteAllGroepen();
	}


	public String maakScope(String naam) {
		SCO_CreateRequest request= new SCO_CreateRequest();
		request.setNaam( naam);
		SCO_CreateResponse response= getScopeService().create(request);
		return response.getId();
	}

	public GRP_GroepService getGroepService() {
		return groepService;
	}

	public void setGebruikersgroepService(GRP_GroepService gebruikersgroepService) {
		this.groepService = gebruikersgroepService;
	}

	public SCO_ScopeService getScopeService() {
		return groepscopeService;
	}

	public void setGroepscopeService(SCO_ScopeService groepscopeService) {
		this.groepscopeService = groepscopeService;
	}

}	
