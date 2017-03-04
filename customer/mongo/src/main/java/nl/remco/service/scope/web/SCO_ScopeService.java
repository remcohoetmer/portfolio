package nl.remco.service.scope.web;

import nl.remco.service.common.model.Identifiable;


public interface SCO_ScopeService {

	SCO_CreateResponse create( SCO_CreateRequest request);
	int update( SCO_UpdateRequest request);
	int delete( Identifiable request);
	SCO_GetResponse get( SCO_GetRequest request);
}
