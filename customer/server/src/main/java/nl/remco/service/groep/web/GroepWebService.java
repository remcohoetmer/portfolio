package nl.remco.service.groep.web;


import java.util.ArrayList;
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
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.common.web.BadRequestException;
import nl.remco.service.common.web.IDList;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.Lidmaatschap;
import nl.remco.service.groep.web.GRP_GetRequest.Filter;
import nl.remco.service.utils.HTTPServerUtil;
import nl.remco.service.utils.HTTPUtil;
import nl.remco.service.utils.Util;


@Path("/group")
public class GroepWebService {

	@Context ServletContext servletContext;

	private GRP_GroepService getService() {
		ApplicationContext ctx= WebApplicationContextUtils.getWebApplicationContext(servletContext);
		GRP_GroepService service= ctx.getBean(GRP_GroepService.class);
		return service;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(GRP_CreateRequest request) {
		GRP_GroepService service= getService();
		GRP_CreateResponse response= service.create(request);
		return Response.status(Response.Status.CREATED).entity(response).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response update(@PathParam("id") String id,
			GRP_UpdateRequest request, @HeaderParam( "If-Unmodified-Since") String modified) {
		request.setId(id);
		request.setLaatstgewijzigd( HTTPUtil.parse(modified));
		GRP_GroepService service= getService();

		int aantal_updates= service.update(request);
		HTTPServerUtil.check(aantal_updates, request);


		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id, @HeaderParam( "If-Unmodified-Since") String modified) {
		GRP_GroepService service= getService();
		Identifiable groep= new Identifiable( id);
		groep.setLaatstgewijzigd(HTTPUtil.parse(modified));

		int aantal_updates= service.delete( groep);
		HTTPServerUtil.check(aantal_updates, groep);


		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void get( @PathParam("id") String id,
			@QueryParam("select") String select,
			@Suspended final AsyncResponse asyncResponse)
	{
		GRP_GroepService service= getService();
		GRP_GetRequest request= new GRP_GetRequest();
		request.setIds( IDList.create(id));
		parseSelectie(select, request);

		GRP_GetResponse response= service.get(request);
		if (response.getGroepen().size()!= 1) {
			asyncResponse.resume( new BadRequestException( "Group ID invalid"));
		}

		Groep groep= response.getGroepen().get(0);
		ResponseBuilder builder= Response.status(HttpServletResponse.SC_OK).entity(groep);
		builder.lastModified(groep.getLaatstgewijzigd());

		asyncResponse.resume( builder.build());

	}

	private void parseSelectie(String select, GRP_GetRequest request) {
		GRP_Selectie selectie= new GRP_Selectie();
		request.setSelectie(selectie);
		List<String> selectieList= Util.toStringList(select);
		if (selectieList!= null) {
			for( String field: selectieList){
				switch (field){
				case "sleutels":selectie.setSelectSleutels(true); break;
				case "lidmaatschappen":selectie.setSelectLidmaatschappen(true); break;
				case "klanten":selectie.setSelectKlanten(true); break;
				case "organisaties":selectie.setSelectOrganisaties(true); break;
				case "scopes":selectie.setSelectScopes(true); break;
				case "kenmerken":selectie.setSelectKenmerken(true); break;
				case "hoofdgroep":selectie.setSelectHoofdgroep(true); break;
				default:
					throw new BadRequestException( "Onbekende select veld:"+ field);
				}
			}
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public void search(
			@QueryParam("select") String select,
			@QueryParam("naam") String naam,
			@QueryParam("beschrijving") String beschrijving,
			@QueryParam("status") LifeCycleBeheer.Status status,
			@QueryParam("statusLidmaatschap") LifeCycleBeheer.Status statusLidmaatschap,
			@QueryParam("groepscode") String groepscode,
			@QueryParam("organisatieId")  String organisatieId,
			@QueryParam("locatieId")  String locatieId,
			@QueryParam("scopeId") String scopeId,
			@QueryParam("product")  String product,
			@QueryParam("geplandePeriode")  String geplandePeriode,
			@QueryParam("kenmerk")  String kenmerk,
			@Suspended final AsyncResponse asyncResponse)
	{
		GRP_GroepService service= getService();
		GRP_GetRequest request= new GRP_GetRequest();
		parseSelectie(select, request);
		Filter filter= new Filter();
		request.setFilter(filter);
		filter.setNaam( naam);
		filter.setBeschrijving(beschrijving);
		filter.setStatus(status);
		filter.setStatusLidmaatschap( statusLidmaatschap);
		filter.setGroepscode(groepscode);
		if (organisatieId!= null) {
			filter.setOrganisatie( new Identifiable(organisatieId));
		}
		if (locatieId!= null) {
			filter.setLocatie( new Identifiable(locatieId));
		}
		if (scopeId!= null) {
			filter.setScope( new Identifiable(scopeId));
		}
		filter.setProduct(product);
		filter.setGeplandePeriode(geplandePeriode);
		if (kenmerk!= null){
			List<String> kenmerken= new ArrayList<String>();
			kenmerken.add( kenmerk);
			filter.setKenmerken(kenmerken);
		}
		GRP_GetResponse response= service.get(request);
		asyncResponse.resume( response);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/klant/{id}")
	public Response searchForGebruikers( 
			@PathParam("id") String klantId,
			@QueryParam("rol") Lidmaatschap.Rol rol,
			@QueryParam("lidmaatschapStatus") Status lidmaatschapStatus,
			@QueryParam("groepStatus") Status groepStatus,
			@QueryParam("organisatieId")  String organisatieId,
			@QueryParam("scopeId") String scopeId
			)
	{
		GRP_GroepService service= getService();
		GRP_SearchForKlantRequest request= new GRP_SearchForKlantRequest();
		GRP_SearchForKlantRequest.Filter filter= new GRP_SearchForKlantRequest.Filter();
		request.setFilter(filter);
		filter.setKlantId(klantId);
		filter.setRol(rol);
		filter.setLidmaatschapStatus(lidmaatschapStatus);
		filter.setGroepStatus(groepStatus);
		if (organisatieId!= null) {
			filter.setOrganisatie( new Identifiable(organisatieId));
		}

		if (scopeId!= null) {
			filter.setScope( new Identifiable(scopeId));
		}
		GRP_KlantMetGroepen response= service.searchForKlanten(request);
		return Response.status(HttpServletResponse.SC_OK).entity(response).build();		
	}
}