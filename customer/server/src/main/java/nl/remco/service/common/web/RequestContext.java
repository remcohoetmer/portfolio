package nl.remco.service.common.web;

import nl.remco.service.common.model.Identifiable;

final public class RequestContext {
	private Identifiable klant;

	public Identifiable getKlant() {
		return klant;
	}

	public void setKlant(Identifiable klant) {
		this.klant = klant;
	}
}
