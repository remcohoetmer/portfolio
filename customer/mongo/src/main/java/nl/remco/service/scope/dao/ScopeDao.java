package nl.remco.service.scope.dao;

import java.util.List;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.scope.model.Scope;
import nl.remco.service.scope.web.SCO_GetRequest;

import org.springframework.beans.factory.annotation.Autowired;


public class ScopeDao
{
	@Autowired
	private ScopeMapper scopeMapper;


	public int insertScope(Scope groepscope) {
		return getScopeMapper().insertScope( groepscope);
	}

	public List<Scope> getScopes(SCO_GetRequest request) {
		return getScopeMapper().getScopes( request);
	}

	public int updateScope(Scope groepscope) {
		return getScopeMapper().updateScope( groepscope);
	}

	public int deleteScope( Identifiable groepscope)
	{
		return getScopeMapper().deleteScope( groepscope);
	}

	public ScopeMapper getScopeMapper() {
		return scopeMapper;
	}

	public void setScopeMapper(ScopeMapper scopeMapper) {
		this.scopeMapper = scopeMapper;
	}
}
