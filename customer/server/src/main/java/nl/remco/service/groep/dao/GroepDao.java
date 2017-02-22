package nl.remco.service.groep.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import nl.remco.service.common.model.Benoembaar;
import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.Lidmaatschap;
import nl.remco.service.groep.web.GRP_GetRequest;
import nl.remco.service.groep.web.GRP_KlantMetGroepen;
import nl.remco.service.groep.web.GRP_LidmaatschapCreateUpdate;
import nl.remco.service.groep.web.GRP_SearchForKlantRequest;
import nl.remco.service.groep.web.GRP_Selectie;
import nl.remco.service.groep.web.GRP_UpdateRequest;
import nl.remco.service.utils.HTTPServerUtil;
import nl.remco.service.utils.Util;


public class GroepDao
{

	@Autowired
	private GroepMapper groepMapper;
	@Autowired
	private Mapper mapper;

	public Mapper getMapper() {
		return mapper;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	public GroepMapper getGroepMapper() {
		return groepMapper;
	}

	public void setGroepMapper(GroepMapper groepMapper) {
		this.groepMapper = groepMapper;
	}

	public String insertGroep(Groep groep) {
		groepMapper.insertGroep( groep);
		if (Util.isDefined( groep.getLidmaatschappen())) {
			groepMapper.insertLidmaatschappen( groep);
		}
		if (Util.isDefined( groep.getKenmerken())) {
			groepMapper.insertKenmerken( groep);
		}
		return groep.getId();
	}

	public List<Groep> getGroepen(GRP_GetRequest request) {
		List<Groep> groepen= groepMapper.getGroepen( request);
		if (request.getSelectie()!= null && request.getSelectie().isSelectKenmerken()) {
			addKenmerken( groepen, groepMapper);
		}
		if (request.getSelectie()== null || !request.getSelectie().isSelectLidmaatschappen()) {
			// Een gevolg van de formulering van de SQL query komt er een lege set terug indien er
			// niet om lidmaatschappen is gevraagd. Dit is onjuist: er moet null worden teruggegeven.
			for (Groep groep: groepen){
				groep.setLidmaatschappen( null);
			}
		}
		return groepen;
	}

	public Map<String,Benoembaar> getGroepenAlsBenoembaar( List<String> groepIds)
	{
		GRP_GetRequest request= new GRP_GetRequest();
		request.setIds( groepIds);
		request.setSelectie( new GRP_Selectie());

		List<Groep> groepen= getGroepen(request);
		return groepen.stream().map( GroepDao::convertToBenoembaar)
				.collect(Collectors.toMap(Benoembaar::getId, Function.identity()));
	}
	
	private static Benoembaar convertToBenoembaar(Groep groep)
	{
		Benoembaar ben_groep= new Benoembaar();
		ben_groep.setId( groep.getId());
		ben_groep.setNaam(groep.getNaam());
		ben_groep.setStatus( groep.getStatus());
		return ben_groep;
	}

	private void addKenmerken(List<Groep> groepen,
			GroepMapper groepMapper) {
		if (groepen.isEmpty()) {
			return;
		}

		List<String> ids= new ArrayList<String>();
		Map<String,Groep> groepenMap= new HashMap<String,Groep>();
		for (Groep groep: groepen){
			// add the id to the list for the DB query
			ids.add( groep.getId());
			// put it in the map for easy lookup
			groepenMap.put( groep.getId(), groep);
			// initialise the kenmerken
			groep.setKenmerken( new ArrayList<String>());
		}
		List<KenmerkRecord> kenmerken= groepMapper.getKenmerkRecords( ids);
		for (KenmerkRecord record: kenmerken) {
			groepenMap.get(record.getGroepId()).getKenmerken().add( record.getKenmerk());
		}
	}


	public GRP_KlantMetGroepen searchGroepen(GRP_SearchForKlantRequest searchRequest) {
		return groepMapper.searchGroepenForKlanten( searchRequest);

	}

	private List<Lidmaatschap> convertCreateUpdates( List<GRP_LidmaatschapCreateUpdate> lidmaatschapCreateUpdates, Mapper mapper){

		List<Lidmaatschap> lidmaatschappen= new ArrayList<>();
		if (lidmaatschapCreateUpdates!=null) {
			for (GRP_LidmaatschapCreateUpdate lidmaatschapCreateUpdate:lidmaatschapCreateUpdates ) {
				Lidmaatschap lidmaatschap= mapper.map(lidmaatschapCreateUpdate, Lidmaatschap.class);
				lidmaatschappen.add( lidmaatschap);
			}
		}
		return lidmaatschappen;
	}

	private List<Lidmaatschap> convertDeletes( List<Identifiable> lidmaatschapDeletes){

		List<Lidmaatschap> lidmaatschappen= new ArrayList<Lidmaatschap>();
		if (lidmaatschapDeletes!=null) {
			for (Identifiable lidmaatschapDelete:lidmaatschapDeletes ) {
				Lidmaatschap lidmaatschap= new Lidmaatschap();
				lidmaatschap.setId( lidmaatschapDelete.getId());
				lidmaatschap.setLaatstgewijzigd(lidmaatschapDelete.getLaatstgewijzigd());
				lidmaatschap.setStatus( Status.Verwijderd);
				lidmaatschappen.add( lidmaatschap);
			}
		}
		return lidmaatschappen;
	}

	public int updateGroep(GRP_UpdateRequest request) {

		Groep groep= mapper.map(request, Groep.class);

		int aantal_updates;

		// conversie naar Gebruikersgroep object om te kunnen persisteren.
		//GebruikersgroepMapper gebruikersgroepMapper = sqlSession.getMapper(GebruikersgroepMapper.class);

		if (groep.getNaam()!= null 
				|| groep.getBeschrijving()!= null
				||	groep.getStatus()!= null) {
			aantal_updates= groepMapper.updateGroep( groep);
			HTTPServerUtil.check(aantal_updates, groep);

		}
		// persisteer de inschrijvingen
		if (Util.isDefined( request.getCreateLidmaatschappen())) {
			// maak gebruik van bestaand Gebruikersgroep object (voor de ID)
			groep.setLidmaatschappen( convertCreateUpdates( request.getCreateLidmaatschappen(), mapper));
			groepMapper.insertLidmaatschappen( groep);
		}

		if (Util.isDefined(request.getUpdateLidmaatschappen())) {

			List<Lidmaatschap> updatedLidmaatschappen= convertCreateUpdates( request.getUpdateLidmaatschappen(), mapper);

			for (Lidmaatschap lidmaatschap:updatedLidmaatschappen ) {
				aantal_updates= groepMapper.updateLidmaatschap( lidmaatschap);
				HTTPServerUtil.check(aantal_updates, groep);

			}
		}
		if (Util.isDefined( request.getDeleteLidmaatschappen())) {
			// zet de delete om in een update naar de Verwijderd Status
			List<Lidmaatschap> updatedLidmaatschappen= convertDeletes(request.getDeleteLidmaatschappen());
			for (Lidmaatschap lidmaatschap:updatedLidmaatschappen ) {
				aantal_updates= groepMapper.updateLidmaatschap( lidmaatschap);
				HTTPServerUtil.check(aantal_updates, groep);
			}
		}
		return 1;
	}

	public int deleteGroep( Identifiable groep) {
		return groepMapper.deleteGroep( groep);
	}
}
