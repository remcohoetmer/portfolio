package nl.remco.service.common.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nl.remco.service.scope.model.Scope;
import nl.remco.service.scope.web.SCO_GetRequest;
import nl.remco.service.scope.web.SCO_GetResponse;
import nl.remco.service.scope.web.SCO_ScopeService;
import nl.remco.service.scope.web.SCO_Selectie;

public class ScopeServiceHelper {
	public static Map<String, Scope> getScopes(SCO_ScopeService scopeService, Set<String> scopeIdSet
			) {
		SCO_GetRequest request= new SCO_GetRequest();
		request.setSelectie(new SCO_Selectie());

		request.setIds( new ArrayList<String>(scopeIdSet));
		SCO_GetResponse scopeResponse= scopeService.get(request);

		// zet de scope in een map om ze snel te vinden
		Map<String, Scope> returnedScopeMap= new HashMap<String, Scope>();
		for( Scope leerobject: scopeResponse.getScopes()) {
			returnedScopeMap.put(leerobject.getId(), leerobject);
		}
		return returnedScopeMap;
	}
}
