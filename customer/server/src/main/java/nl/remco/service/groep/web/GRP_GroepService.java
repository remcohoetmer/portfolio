package nl.remco.service.groep.web;

import nl.remco.service.common.model.Identifiable;

public interface GRP_GroepService {

	GRP_CreateResponse create( GRP_CreateRequest request);
	int update( GRP_UpdateRequest request);
	int delete( Identifiable groep);
	GRP_GetResponse get( GRP_GetRequest request);
	GRP_KlantMetGroepen searchForKlanten( GRP_SearchForKlantRequest request);
}
