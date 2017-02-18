package nl.remco.service.organisatie.web;

import java.util.List;

import nl.remco.service.organisatie.model.Organisatie;

public class ORG_GetResponse {
	private List<Organisatie> organisaties;

	public List<Organisatie> getOrganisaties() {
		return organisaties;
	}

	public void setOrganisaties(List<Organisatie> organisaties) {
		this.organisaties = organisaties;
	}


}
