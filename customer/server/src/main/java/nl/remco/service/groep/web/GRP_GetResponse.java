package nl.remco.service.groep.web;

import java.util.List;

import nl.remco.service.groep.model.Groep;

public class GRP_GetResponse {
	private List<Groep> groepen;

	public List<Groep> getGroepen() {
		return groepen;
	}

	public void setGroepen(List<Groep> groepen) {
		this.groepen = groepen;
	}


}
