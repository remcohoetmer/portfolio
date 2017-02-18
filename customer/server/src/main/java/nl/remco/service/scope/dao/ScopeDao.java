package nl.remco.service.scope.dao;

import java.util.List;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.scope.model.Scope;
import nl.remco.service.scope.web.SCO_GetRequest;

import org.springframework.beans.factory.annotation.Autowired;


public class ScopeDao
{
	@Autowired
	private ScopeMapper groepscopeMapper;

	public ScopeMapper getGroepscopeMapper() {
		return groepscopeMapper;
	}

	public void setGroepscopeMapper(ScopeMapper groepscopeMapper) {
		this.groepscopeMapper = groepscopeMapper;
	}

	public int insertScope(Scope groepscope) {
		return groepscopeMapper.insertScope( groepscope);
	}

	public List<Scope> getScopes(SCO_GetRequest request) {
		return groepscopeMapper.getScopes( request);
	}

	public int updateScope(Scope groepscope) {
		return groepscopeMapper.updateScope( groepscope);
	}

	public int deleteScope( Identifiable groepscope)
	{
		return groepscopeMapper.deleteScope( groepscope);
	}
}
