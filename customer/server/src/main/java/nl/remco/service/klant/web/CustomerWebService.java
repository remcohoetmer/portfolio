package nl.remco.service.klant.web;


import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
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

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.common.web.BadRequestException;
import nl.remco.service.klant.model.Geslacht;
import nl.remco.service.klant.model.Inschrijving;
import nl.remco.service.klant.web.KLA_GetRequest.Filter;
import nl.remco.service.utils.Util;


@Path("/customer")
public class CustomerWebService {
	@Context ServletContext servletContext;

	private KLA_KlantService getService() {
		ApplicationContext ctx= WebApplicationContextUtils.getWebApplicationContext(servletContext);

		KLA_KlantService service = ctx.getBean(KLA_KlantService.class);
		return service;
	}

	@GET
	@Path("/{ids}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get( @PathParam("ids") String ids,
			@QueryParam("select") String select
			)
	{
		KLA_GetRequest request= new KLA_GetRequest();
		request.setIds( Util.toStringList( ids));
		try {
			parseSelect(select, request);
		} catch (BadRequestException bre) {
			return Response.status(Response.Status.NOT_FOUND).
				    entity(bre.getMessage()).type("text/plain").build();
		}
		return Response.status(Response.Status.OK).entity( getService().get(request)).build();
	}


	private void parseSelect(String select, KLA_GetRequest request) {
		KLA_Selectie selectie= new KLA_Selectie();
		request.setSelectie(selectie);
		List<String> selectieList= Util.toStringList(select);
		if (selectieList!= null) {
			for( String field: selectieList){
				switch (field){
				case "sleutels":selectie.setSelectSleutels(true); break;
				case "organisaties":selectie.setSelectOrganisaties( true); break;
				default:
					throw new BadRequestException( "Onbekende select veld:"+ field);
				}
			}
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response search( 
			@QueryParam("select") String select,
			@QueryParam("voornaam") String voornaam,
			@QueryParam("achternaam") String achternaam,
			@QueryParam("emailAdres") String emailAdres,
			@QueryParam("geslacht") Geslacht geslacht,
			@QueryParam("status") LifeCycleBeheer.Status status,
			@QueryParam("geboortedatum") Date geboortedatum,
			@QueryParam("product") String product,
			@QueryParam("geplandePeriode") String geplandePeriode,
			@QueryParam("organisatieId") String organisatieId,
			@QueryParam("locatieId") String locatieId,
			@QueryParam("rol") Inschrijving.Rol rol,
			@QueryParam("aangemaaktDoorId") String aangemaaktDoorId)
	{
		KLA_GetRequest request= new KLA_GetRequest();
		parseSelect(select, request);
		Filter filter= new Filter();
		request.setFilter(filter);
		filter.setVoornaam(voornaam);
		filter.setAchternaam(achternaam);
		filter.setEmailAdres(emailAdres);
		filter.setGeslacht(geslacht);
		filter.setStatus(status);
		filter.setGeboortedatum(geboortedatum);
		filter.setProduct(product);
		filter.setJaargroep(geplandePeriode);
		if (organisatieId!= null) {
			filter.setOrganisatie( new Identifiable(organisatieId));
		}
		if (locatieId!= null) {
			filter.setLocatie( new Identifiable(locatieId));
		}
		filter.setRol(rol);
		if (aangemaaktDoorId!= null) {
			filter.setAangemaaktDoor( new Identifiable(aangemaaktDoorId));
		}

		KLA_GetResponse response= getService().get(request);
		return Response.status(Response.Status.OK).entity(response).build();		
	}


}