package nl.remco.service.scope.web;


import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.common.web.BadRequestException;
import nl.remco.service.common.web.IDList;
import nl.remco.service.scope.web.SCO_GetRequest.Filter;
import nl.remco.service.utils.HTTPServerUtil;
import nl.remco.service.utils.HTTPUtil;
import nl.remco.service.utils.Util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


@Path("/scope")
public class ScopeWebService {

	@Context ServletContext servletContext;
	private SCO_ScopeService getService() {
		ApplicationContext ctx= WebApplicationContextUtils.getWebApplicationContext(servletContext);

		SCO_ScopeService service = ctx.getBean(SCO_ScopeService.class);
		return service;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(SCO_CreateRequest request) {
		SCO_ScopeService service= getService();
		SCO_CreateResponse response= service.create(request);
		return Response.status(Response.Status.CREATED).entity(response).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(SCO_UpdateRequest request, @HeaderParam( "If-Unmodified-Since") String modified) {
		SCO_ScopeService service= getService();

		request.setLaatstgewijzigd( HTTPUtil.parse( modified) );
		int aantal_updates= service.update(request);
		HTTPServerUtil.check(aantal_updates, request);

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id, @HeaderParam( "If-Unmodified-Since") String modified) {
		SCO_ScopeService service= getService();
		Identifiable request= new Identifiable( id);

		request.setLaatstgewijzigd( HTTPUtil.parse( modified) );
		int aantal_updates= service.delete(request);
		HTTPServerUtil.check(aantal_updates, request);

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get( @PathParam("id") String id,
			@QueryParam("select") String select)
	{
		SCO_ScopeService service= getService();
		SCO_GetRequest request= new SCO_GetRequest();
		request.setIds( IDList.create(id));
		SCO_Selectie selectie= new SCO_Selectie();
		request.setSelectie(selectie);
		List<String> selectieList= Util.toStringList(select);
		if (selectieList!= null) {
			for( String field: selectieList){
				switch (field){
				default:
					throw new BadRequestException( "Onbekende select veld:"+ field);
				}
			}
		}

		SCO_GetResponse response= service.get(request);

		ResponseBuilder builder= null;
		if (response.getScopes().size()==1){
			builder= Response.status(HttpServletResponse.SC_OK);
			builder= builder.entity(response.getScopes().get(0));
			builder= builder.lastModified( response.getScopes().get(0).getLaatstgewijzigd());
		} else {
			builder= Response.status(HttpServletResponse.SC_NOT_FOUND);
		}
		return builder.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response search( @QueryParam("naam") String naam, @QueryParam("status") LifeCycleBeheer.Status status)
	{
		SCO_ScopeService service= getService();
		SCO_GetRequest request= new SCO_GetRequest();
		request.setFilter(new Filter());
		request.getFilter().setNaam(naam);
		request.getFilter().setStatus( status);

		SCO_GetResponse response= service.get(request);
		return Response.status(HttpServletResponse.SC_OK).entity(response).build();		
	}
}