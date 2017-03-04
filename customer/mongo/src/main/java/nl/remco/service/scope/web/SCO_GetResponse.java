package nl.remco.service.scope.web;

import java.util.List;

import nl.remco.service.scope.model.Scope;

public class SCO_GetResponse {
	private List<Scope> groepscopes;

	public List<Scope> getScopes() {
		return groepscopes;
	}

	public void setScopes(List<Scope> groepscopes) {
		this.groepscopes = groepscopes;
	}
}
