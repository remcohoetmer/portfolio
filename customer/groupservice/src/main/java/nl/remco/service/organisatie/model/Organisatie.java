package nl.remco.service.organisatie.model;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;

public class Organisatie extends LifeCycleBeheer {
	private String naam;
	private String straatnaam;
	private String nummer;
	private String postcode;
	private String plaatsnaam;
	private String kvkNummer;
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


	public String getStraatnaam() {
		return straatnaam;
	}

	public void setStraatnaam(String straatnaam) {
		this.straatnaam = straatnaam;
	}

	public String getNummer() {
		return nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPlaatsnaam() {
		return plaatsnaam;
	}

	public void setPlaatsnaam(String plaatsnaam) {
		this.plaatsnaam = plaatsnaam;
	}

	public String getKvkNummer() {
		return kvkNummer;
	}

	public void setKvkNummer(String kvkNummer) {
		this.kvkNummer = kvkNummer;
	}

	@Override
	public String toString() {
		return "Organisatie {id: " + id 
				+ ", naam:" + naam
				+ "}";
	}



}
