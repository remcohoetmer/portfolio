package nl.remco.service.scope.web;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;


public class SCO_UpdateRequest extends Identifiable {

	private String naam;
	private LifeCycleBeheer.Status status;
	
	public String getNaam() {
		return naam;
	}
	public void setNaam(String naam) {
		this.naam = naam;
	}
	public LifeCycleBeheer.Status getStatus() {
		return status;
	}
	public void setStatus(LifeCycleBeheer.Status status) {
		this.status = status;
	}
		
}