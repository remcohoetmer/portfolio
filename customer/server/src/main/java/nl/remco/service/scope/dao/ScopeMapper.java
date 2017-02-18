package nl.remco.service.scope.dao;
import java.util.List;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.scope.model.Scope;
import nl.remco.service.scope.web.SCO_GetRequest;
public interface ScopeMapper {

	 List<Scope> getScopes(SCO_GetRequest request);
	 int insertScope( Scope scope);
	 int updateScope( Scope scope);
	 int deleteScope( Identifiable scope);

}