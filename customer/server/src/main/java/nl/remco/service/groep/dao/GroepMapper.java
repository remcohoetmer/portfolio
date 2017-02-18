package nl.remco.service.groep.dao;
import java.util.List;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.Lidmaatschap;
import nl.remco.service.groep.web.GRP_KlantMetGroepen;
import nl.remco.service.groep.web.GRP_GetRequest;
import nl.remco.service.groep.web.GRP_SearchForKlantRequest;
public interface GroepMapper {

	List<Groep> getGroepen(GRP_GetRequest request);
	List<KenmerkRecord> getKenmerkRecords(List<String> ids);

	GRP_KlantMetGroepen searchGroepenForGebruikers(GRP_SearchForKlantRequest zoekRequest);

	int insertGroep(Groep groep);
	int updateGroep(Groep groep);
	int deleteGroep( Identifiable groep);

	int insertLidmaatschappen( Groep groep);
	int updateLidmaatschap( Lidmaatschap lidmaatschap);
	int deleteLidmaatschap( Identifiable groep);

	int insertKenmerken( Groep groep);

}