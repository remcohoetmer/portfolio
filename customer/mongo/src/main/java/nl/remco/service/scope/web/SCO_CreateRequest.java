package nl.remco.service.scope.web;

import nl.remco.service.common.model.LifeCycleBeheer.Status;


public class SCO_CreateRequest {
	private String naam;
	protected Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

}