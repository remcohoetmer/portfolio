package nl.remco.service.organisatie.web;


import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.common.web.BadRequestException;
import nl.remco.service.organisatie.web.ORG_GetRequest.Filter;
import nl.remco.service.utils.Util;


@Path("/organisatie")
public class OrganisatieWebService {

	@Context ServletContext servletContext;
	
	private ORG_OrganisatieService getService() {
        ApplicationContext ctx= WebApplicationContextUtils.getWebApplicationContext(servletContext);
        
        ORG_OrganisatieService service = ctx.getBean(ORG_OrganisatieService.class);
        return service;
	}

	@GET
	@Path("/{ids}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get( @PathParam("ids") String ids,
			@QueryParam("select") String select)
	{
		ORG_OrganisatieService service= getService();
		ORG_GetRequest request= new ORG_GetRequest();
		request.setIds( Util.toStringList( ids));
		processSelect(select, request);

		ORG_GetResponse response= service.get(request);

		return Response.status(HttpServletResponse.SC_OK).entity(response).build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response search( @QueryParam("naam") String naam,
			@QueryParam("status") LifeCycleBeheer.Status status,
			@QueryParam("select") String select)
	{
		ORG_OrganisatieService service= getService();
		ORG_GetRequest request= new ORG_GetRequest();
		request.setFilter(new Filter());
		request.getFilter().setNaam(naam);
		request.getFilter().setStatus( status);
		processSelect(select, request);
		
		ORG_GetResponse response= service.get(request);
		return Response.status(HttpServletResponse.SC_OK).entity(response).build();		
	}
	
	/*
	 * vertaling HTTP selectie velden naar Java bean setting
	 */
	private void processSelect(String select, ORG_GetRequest request) {
		ORG_Selectie selectie= new ORG_Selectie();
		request.setSelectie(selectie);
		List<String> selectieList= Util.toStringList(select);
		if (selectieList!= null) {
			for( String field: selectieList){
				switch (field){
				case "sleutels": selectie.setSelectSleutels( true);
				default:
					throw new BadRequestException( "Onbekende select veld: "+ field);
				}
			}
		}
	}
}