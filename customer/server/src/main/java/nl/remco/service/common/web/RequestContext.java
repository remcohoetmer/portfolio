package nl.remco.service.common.web;

import nl.remco.service.common.model.Identifiable;

final public class RequestContext {
	private Identifiable gebruiker;

	public Identifiable getKlant() {
		return gebruiker;
	}

	public void setKlant(Identifiable gebruiker) {
		this.gebruiker = gebruiker;
	}
}
