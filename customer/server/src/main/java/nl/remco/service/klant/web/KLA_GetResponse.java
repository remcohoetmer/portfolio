package nl.remco.service.klant.web;

import java.util.List;

import nl.remco.service.klant.model.Klant;

public class KLA_GetResponse {
	private List<Klant> klanten;

	public List<Klant> getKlanten() {
		return klanten;
	}

	public void setKlanten(List<Klant> klanten) {
		this.klanten = klanten;
	}
}
