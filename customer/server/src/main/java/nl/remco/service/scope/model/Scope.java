package nl.remco.service.scope.model;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;

public class Scope extends LifeCycleBeheer {
	private String naam;
	private Identifiable aangemaaktDoor;
	
	public Identifiable getAangemaaktDoor() {
		return aangemaaktDoor;
	}

	public void setAangemaaktDoor(Identifiable aangemaaktDoor) {
		this.aangemaaktDoor = aangemaaktDoor;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}
}
