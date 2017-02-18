package nl.remco.service.common.web;

import nl.remco.service.common.model.Identifiable;

final public class RequestContext {
	private Identifiable gebruiker;

	public Identifiable getGebruiker() {
		return gebruiker;
	}

	public void setGebruiker(Identifiable gebruiker) {
		this.gebruiker = gebruiker;
	}
}
