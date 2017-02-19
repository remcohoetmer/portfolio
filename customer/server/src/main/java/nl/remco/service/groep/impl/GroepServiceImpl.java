package nl.remco.service.groep.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.ConflictException;
import com.sun.jersey.api.NotFoundException;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.common.web.BadRequestException;
import nl.remco.service.common.web.IDList;
import nl.remco.service.common.web.RequestContext;
import nl.remco.service.groep.dao.GroepDao;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.GroepsMutatieType;
import nl.remco.service.groep.model.Lidmaatschap;
import nl.remco.service.groep.web.GRP_CreateRequest;
import nl.remco.service.groep.web.GRP_CreateResponse;
import nl.remco.service.groep.web.GRP_GetRequest;
import nl.remco.service.groep.web.GRP_GetRequest.Filter;
import nl.remco.service.groep.web.GRP_GetResponse;
import nl.remco.service.groep.web.GRP_GroepService;
import nl.remco.service.groep.web.GRP_KlantMetGroepen;
import nl.remco.service.groep.web.GRP_LidmaatschapCreateUpdate;
import nl.remco.service.groep.web.GRP_LidmaatschapMetGroep;
import nl.remco.service.groep.web.GRP_SearchForKlantRequest;
import nl.remco.service.groep.web.GRP_Selectie;
import nl.remco.service.groep.web.GRP_UpdateRequest;
import nl.remco.service.klant.impl.CRMCustomersDelegate;
import nl.remco.service.klant.model.Inschrijving;
import nl.remco.service.klant.model.Klant;
import nl.remco.service.klant.web.KLA_KlantService;
import nl.remco.service.utils.Util;
/*
 * Implementatie van de Service: naast de aanroep van de DAO worden de volgende business regels gerealiseerd:
 * Business regels:
- Lidmaatschap status: Een gebruiker maximaal 1 keer actief of passief aan een groep (wel 1 keer actief/passief en daarnaast 0 of meer keer verwijderd)
  Hij kan dus niet zowel groepsleider als groepslid zijn.
- Scope van groep: gebruiker moet ingeschreven staan voor de organisatie indien hij wordt toegevoegd aan de groep
- Een verwijderd item kan niet meer actief/passief worden gemaakt.

Let op: Scope van groep mag niet worden veranderd. De inschrijving van de gebruiker wel. Dus de regel is niet:
	de gebruikers aan een groep is *op elk moment* ingeschreven voor de organisatie van de groep.

 */
public class GroepServiceImpl implements GRP_GroepService {

	private RequestContext context;
	@Autowired
	private GroepDao groepDao;
	@Autowired
	private GroepEnricher groepEnricher;
	@Autowired
	private KLA_KlantService klantService;
	@Autowired
	private CRMCustomersDelegate cRMCustomersDelegate;
	@Autowired
	private Mapper mapper;
	
	public Mapper getMapper() {
		return mapper;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	public CRMCustomersDelegate getCRMCustomersDelegate() {
		return cRMCustomersDelegate;
	}

	public void setCRMCustomersDelegate(CRMCustomersDelegate cRMCustomersDelegate) {
		this.cRMCustomersDelegate = cRMCustomersDelegate;
	}

	public GroepServiceImpl() {
		RequestContext requestContext= new RequestContext();
		requestContext.setKlant( new Identifiable("9000000"));
		this.context= requestContext;
	}

	private Groep loadGroep( String id, boolean selectLidmaatschappen)
	{
		GRP_GetRequest request= new GRP_GetRequest();
		request.setIds( IDList.create(id));
		request.setSelectie( new GRP_Selectie());
		request.getSelectie().setSelectLidmaatschappen(selectLidmaatschappen);
		List<Groep> groepen= getGroepDao().getGroepen( request);
		if (groepen.size() ==1) {
			return groepen.get(0);
		}
		return null;
	}

	private Groep loadGroep( String id)
	{
		GRP_GetRequest request= new GRP_GetRequest();
		request.setIds( IDList.create(id));
		request.setSelectie( new GRP_Selectie());
		List<Groep> groepen= groepDao.getGroepen( request);
		if (groepen.size()!= 1) {
			throw new BadRequestException( "Gebruikersgroep ID niet geldig");
		}
		return groepen.get(0);
	}

	private void valideerGroep( Groep groep)
	{
		if (groep.getStatus()== Status.Verwijderd) {
			throw new BadRequestException( "Groep is reeds verwijderd. id="+ groep.getId());
		}
	}


	private boolean isExistGebruikersgroep( String id)
	{
		return loadGroep(id, false)!= null;
	}

	@Override
	public GRP_CreateResponse create(GRP_CreateRequest request){
		if (request.getLidmaatschappen()!=null) {
			for (GRP_LidmaatschapCreateUpdate lidmaatschap: request.getLidmaatschappen()) {
				checkLidmaatschapSyntax(lidmaatschap, true);
			}
		}

		Groep groep= mapper.map(request, Groep.class);
		groep.setAangemaaktDoor( context.getKlant());
		if (groep.getStatus()==null) {
			groep.setStatus( Status.Actief);
		}
		if (groep.getGroepsMutatieType()==null) {
			throw new BadRequestException( "Groepsmutatietype is verplicht");
		}
		if (!Util.isDefined( groep.getNaam())) {
			throw new BadRequestException( "Groepsnaam is verplicht");
		}
		if (!Util.isDefined( groep.getOrganisatie())) {
			throw new BadRequestException( "Organisatie is verplicht");
		}

		if (groep.getGroepsMutatieType()== GroepsMutatieType.SELFSERVICE) {
			groep.setGroepscode( "AAA123");//TODO: genereer groepscode
		}
		if (request.getScope() != null&& request.getScope().getId()== null) {
			throw new BadRequestException( "Scope ID leeg");
		}
		if (request.getOrganisatie() != null&& request.getOrganisatie().getId()== null) {
			throw new BadRequestException( "Organisatie ID leeg");
		}

		if (request.getHoofdgroep() != null) {
			String hoofdgroepId= request.getHoofdgroep().getId();
			if (hoofdgroepId== null) {
				throw new BadRequestException( "Hoofdgroep ID leeg");
			}
			if (!isExistGebruikersgroep(hoofdgroepId)) {
				throw new NotFoundException( "Hoofdgroep bestaat niet");
			}
		}

		if (request.getLidmaatschappen()!= null) {
			checkCreateLidmaatschapData( request.getLidmaatschappen(), request.getOrganisatie(), null);
		}

		getGroepDao().insertGroep( groep);
		GRP_CreateResponse response= new GRP_CreateResponse();
		response.setId( groep.getId());
		response.setGroepscode( groep.getGroepscode());
		return response;
	}

	private void checkLidmaatschapSyntax(GRP_LidmaatschapCreateUpdate lidmaatschap, boolean isCreate) 
	{
		// bij een creatie verzeker dat de ID wordt gegenereerd door de DB
		// set de default waarde voor de status
		if (isCreate) {
			lidmaatschap.setId( null);
			if (lidmaatschap.getStatus()==null) {
				lidmaatschap.setStatus( Status.Actief);
			}
			if (lidmaatschap.getRol()== null) {
				throw new BadRequestException( "Lidmaatschapsrol is verplicht");
			}
			if (lidmaatschap.getKlant()== null || lidmaatschap.getKlant().getId()==null) {
				throw new BadRequestException( "Persoon is verplicht bij groepencreatie");
			}
		} else {
			if (lidmaatschap.getStatus() == Status.Verwijderd) {
				throw new BadRequestException( "Een update mag niet verwijderen");
			}

			if (lidmaatschap.getId()== null) {
				throw new BadRequestException( "Lidmaatschaps ID is verplicht");
			}
		}

	}

	@Override
	public int update(GRP_UpdateRequest request) 
	{
		// Syntactische check
		if (request.getId()==null) {
			throw new BadRequestException( "ID is leeg");
		}

		if (request.getNaam()!= null && request.getNaam().isEmpty()) {
			throw new BadRequestException( "Gewijzigde naam is leeg");
		}

		if (request.getCreateLidmaatschappen()!= null) {
			for ( GRP_LidmaatschapCreateUpdate lidmaatschap: request.getCreateLidmaatschappen()) {
				checkLidmaatschapSyntax(lidmaatschap, true);
			}
		}
		if (request.getUpdateLidmaatschappen()!= null) {
			for ( GRP_LidmaatschapCreateUpdate lidmaatschap: request.getUpdateLidmaatschappen()) {
				checkLidmaatschapSyntax(lidmaatschap, false);
			}
		}
		// Check op de data
		Groep groep= loadGroep( request.getId(), true);
		if (groep==null) {
			throw new NotFoundException( "Gebruikersgroep niet gevonden");
		}
		if (groep.getStatus() == Status.Verwijderd) {
			throw new BadRequestException( "Een verwijderd object kan niet worden gewijzigd");
		}
		HashSet<String> currentKlantIds= new HashSet<String>();
		for (Lidmaatschap lidmaatschap: groep.getLidmaatschappen()) {
			currentKlantIds.add( lidmaatschap.getKlant().getId());
		}

		if (request.getCreateLidmaatschappen()!= null) {
			checkCreateLidmaatschapData(request.getCreateLidmaatschappen(),
					groep.getOrganisatie(), currentKlantIds);
		}
		if (request.getUpdateLidmaatschappen()!= null) {
			checkUpdateLidmaatschapData(
					request.getUpdateLidmaatschappen(), groep);
		}
		return groepDao.updateGroep(request);
	}

	/*
	 * Checkt bij nieuwe of bij bestaande groepen de scope voor de leden
	 * Verder of een lid reeds bestaat
	 * 
	 * organisatie: de gescopede organisatie in geval van nieuwe Groep.
	 * In geval van een bestaande wordt de gescopede organisatie uit de database opgehaald.
	 * locatie: de gescopede locatie. Gelijk als organisatie.
	 */

	private void checkCreateLidmaatschapData(
			List<GRP_LidmaatschapCreateUpdate> createdLidmaatschappen, Identifiable organisatieGroep, 
			Set<String> currentKlantIds) {

		// collect gebruiker Ids
		Set<String> gebruikerIds= new HashSet<String>();
		for ( GRP_LidmaatschapCreateUpdate lidmaatschap: createdLidmaatschappen) {
			gebruikerIds.add( lidmaatschap.getKlant().getId());
		}
		if (gebruikerIds.isEmpty()) {
			return;
		}
		
		List<Klant> gebruikers= cRMCustomersDelegate.getKlanten( gebruikerIds);

		for (Klant gebruiker: gebruikers) {
			if (currentKlantIds!= null && currentKlantIds.contains(gebruiker.getId())) {
				throw new ConflictException( gebruiker.shortString() + " is reeds lid van de groep" );
			}
			if (organisatieGroep!= null) {
				checkGebruikerOrganisatie( gebruiker, organisatieGroep);
			}
		}
	}

	/*
	 * Check de geldigheid van de updateLidmaatschappen -> ze moeten verwijzen naar een bestaand lidmaatschap
	 * Dus foutmelding bij o.a.: lidmaatschap behorend bij een andere groep, of van een verwijderd lidmaatschapsrecord.
	 */
	private void checkUpdateLidmaatschapData(
			List<GRP_LidmaatschapCreateUpdate> updateLidmaatschappen, Groep currentGroep) {
		// verzamelen eerst de huidige lidmaatschap Ids
		Set<String> lidmaatschapIds= new HashSet<String>();
		for (Lidmaatschap lidmaatschap: currentGroep.getLidmaatschappen()) {
			lidmaatschapIds.add( lidmaatschap.getId());
		}
		for (GRP_LidmaatschapCreateUpdate updateLidmaatschap: updateLidmaatschappen) {
			if (!lidmaatschapIds.contains( updateLidmaatschap.getId())) {
				throw new NotFoundException("UpdateLidmaatschap ID="+ updateLidmaatschap.getId()+  " niet gevonden");
			}
		}
	}

	private void checkGebruikerOrganisatie(Klant klant,
			Identifiable organisatie) {
		for (Inschrijving inschrijving: klant.getInschrijvingen()) {
			if (organisatie.equals (inschrijving.getOrganisatie())) {
					return; //Klant is lid van de organisatie en mag dus bij de groep
			}
			// ga door om te kijken voor een andere inschrijving
		}
		String melding= klant.shortString() +  " is niet ingeschreven bij de organisatie ";
		melding += "van de groep en mag daarom niet worden toegevoegd";
		throw new BadRequestException( melding);
	}

	@Override
	public int delete( Identifiable gebruikersgroep) {
		if (!Util.isDefined(gebruikersgroep.getId())) {
			throw new BadRequestException( "ID is leeg");
		}

		return groepDao.deleteGroep(gebruikersgroep);
	}

	@Override
	public GRP_GetResponse get(GRP_GetRequest request) {
		if (request.getIds() != null && request.getIds().isEmpty()) {
			request.setIds( null);
		}

		if (request.getSelectie()==null) {
			request.setSelectie(new GRP_Selectie());
		}

		Filter filter= request.getFilter();
		if (filter!= null) {
			if (filter.getNaam()!=null) {
				filter.setNaam(filter.getNaam().replace( '*','%'));
			}
			if (filter.getBeschrijving()!=null) {
				filter.setBeschrijving(filter.getBeschrijving().replace( '*','%'));
			}
			if (filter.getKenmerken()!= null) {
				// als er geen kenmerken zijn opgegeven, beschouw het dan als geen constraint
				// (anders komt er altijd een lege lijst groepen terug).
				if (filter.getKenmerken().isEmpty()) {
					filter.setKenmerken(null);
				}
			}
		}
		List<Groep> gebruikersgroepen= getGroepDao().getGroepen( request);
		GRP_Selectie selectie= request.getSelectie();
		if (selectie.isSelectKlanten()
				|| selectie.isSelectOrganisaties()
				|| selectie.isSelectScopes()
				|| selectie.isSelectHoofdgroep()) {
			groepEnricher.execute( gebruikersgroepen, request.getSelectie(),getGroepDao());
		}

		GRP_GetResponse response= new GRP_GetResponse();
		response.setGroepen(gebruikersgroepen);
		return response;
	}

	@Override
	public GRP_KlantMetGroepen searchForKlanten(
			GRP_SearchForKlantRequest request) {
		if (request.getFilter()== null) {
			request.setFilter( new GRP_SearchForKlantRequest.Filter());			
		}
		if (request.getSelectie() == null) {
			request.setSelectie( new GRP_SearchForKlantRequest.Selectie());			
		}
		GRP_SearchForKlantRequest.Filter filter= request.getFilter();

		if (!Util.isDefined( filter.getKlantId())) {
			throw new BadRequestException( "Gebruiker ID is vereist");
		}

		if (filter.getScope() != null&& filter.getScope().getId()== null) {
			throw new BadRequestException( "Scope ID leeg");
		}
		if (filter.getOrganisatie() != null&& filter.getOrganisatie().getId()== null) {
			throw new BadRequestException( "Organisatie ID leeg");
		}

		GRP_KlantMetGroepen groepen= getGroepDao().searchGroepen( request);
		if (groepen == null) {
			// valide response: er zijn geen groepen gevonden
			// maar zorg wel dat het vereiste object worden geretourneerd (lege huls)
			groepen= new GRP_KlantMetGroepen();
			groepen.setId( filter.getKlantId());
			groepen.setLidmaatschappen( new ArrayList<GRP_LidmaatschapMetGroep>());
		}
		return groepen;
	}

	public GroepDao getGroepDao() {
		return groepDao;
	}

	public void setGroepDao(GroepDao dao) {
		this.groepDao = dao;
	}

	public GroepEnricher getDataEnricher() {
		return groepEnricher;
	}

	public void setDataEnricher(GroepEnricher dataEnricher) {
		this.groepEnricher = dataEnricher;
	}



}
