package nl.remco.service.scope.impl;

import java.util.List;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.common.web.BadRequestException;
import nl.remco.service.common.web.IDList;
import nl.remco.service.common.web.RequestContext;
import nl.remco.service.scope.dao.ScopeDao;
import nl.remco.service.scope.model.Scope;
import nl.remco.service.scope.web.SCO_CreateRequest;
import nl.remco.service.scope.web.SCO_CreateResponse;
import nl.remco.service.scope.web.SCO_GetRequest;
import nl.remco.service.scope.web.SCO_GetResponse;
import nl.remco.service.scope.web.SCO_ScopeService;
import nl.remco.service.scope.web.SCO_Selectie;
import nl.remco.service.scope.web.SCO_UpdateRequest;
import nl.remco.service.scope.web.SCO_GetRequest.Filter;
import nl.remco.service.utils.Util;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.NotFoundException;

public class ScopeServiceImpl implements SCO_ScopeService {
	
	private RequestContext context;
	@Autowired
	private ScopeDao scopeDao;
	@Autowired
	private Mapper mapper;

	public ScopeServiceImpl() {
		RequestContext requestContext= new RequestContext();
		requestContext.setGebruiker( new Identifiable("9000000"));
		this.context= requestContext;
	}

	
	public SCO_CreateResponse create( SCO_CreateRequest request)
	{
		Scope scope= mapper.map(request, Scope.class);
		if (!Util.isDefined(scope.getNaam())) {
			throw new BadRequestException("Naam is leeg");
		}
		scope.setAangemaaktDoor( context.getGebruiker());
		if (scope.getStatus()==null) {
			scope.setStatus( Status.Actief);
		}
		
		scopeDao.insertScope(scope);
		SCO_CreateResponse response= new SCO_CreateResponse();
		response.setId( scope.getId());
		return response;
	}

	
	public int update( SCO_UpdateRequest request)
	{
		if (request.getId()==null) {
			throw new BadRequestException( "Update request vereist Id");
		}		
		if (request.getNaam()!= null && request.getNaam().trim().length()==0) {
			throw new BadRequestException("Naam is leeg");
		}
		
		checkUpdateData(request);

		Scope scope= mapper.map(request, Scope.class);
		
		return scopeDao.updateScope( scope);
	}

	private void checkUpdateData(SCO_UpdateRequest request) {
		SCO_GetRequest getRequest= new SCO_GetRequest();
		getRequest.setIds( IDList.create( request.getId()));
		List<Scope> scopes= scopeDao.getScopes( getRequest);
		if (scopes.size()!= 1) {
			throw new NotFoundException("Object niet gevonden");
		}
		Scope groepscope= scopes.get(0);
		if (groepscope.getStatus() == Status.Verwijderd) {
			throw new BadRequestException( "Een verwijderd object kan niet worden gewijzigd");
		}
	}

	public int delete( Identifiable request)
	{
		return scopeDao.deleteScope( request);
	}

	public SCO_GetResponse get( SCO_GetRequest request)
	{
		if (request.getSelectie() == null) {
			request.setSelectie( new SCO_Selectie()); 
		}
		if ( request.getIds()!= null && request.getIds().isEmpty()) {
			throw new BadRequestException( "Lijst met IDs is leeg");
		}
		Filter filter= request.getFilter();
		if (filter!= null) {
			if (filter.getNaam()!=null) {
				filter.setNaam(filter.getNaam().replace( '*','%'));
			}
		}
		SCO_GetResponse response= new SCO_GetResponse();
		List<Scope> scopes= scopeDao.getScopes( request);
		response.setScopes( scopes);
		return response;
	}


	public ScopeDao getScopeDao() {
		return scopeDao;
	}


	public void setScopeDao(ScopeDao groepscopeDao) {
		this.scopeDao = groepscopeDao;
	}
	public Mapper getMapper() {
		return mapper;
	}


	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}
}
