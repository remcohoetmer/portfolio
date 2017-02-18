package nl.remco.service.common.model;

import nl.remco.service.common.model.LifeCycleBeheer.Status;

public class Benoembaar extends Identifiable {
	private String naam;
	private Status status;

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
