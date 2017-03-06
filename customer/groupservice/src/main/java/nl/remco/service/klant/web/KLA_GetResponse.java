package nl.remco.service.klant.web;

import java.util.List;

import nl.remco.service.klant.model.Person;

public class KLA_GetResponse {
	private List<Person> klanten;

	public List<Person> getKlanten() {
		return klanten;
	}

	public void setKlanten(List<Person> klanten) {
		this.klanten = klanten;
	}
}
