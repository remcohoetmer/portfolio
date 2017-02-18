package nl.remco.service.groep.web;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.groep.model.Lidmaatschap;

public class GRP_LidmaatschapCreateUpdate extends LifeCycleBeheer {
	private Lidmaatschap.Rol rol;
	private Identifiable gebruiker;
	
	public Lidmaatschap.Rol getRol() {
		return rol;
	}
	public void setRol(Lidmaatschap.Rol rol) {
		this.rol = rol;
	}
	public Identifiable getGebruiker() {
		return gebruiker;
	}
	public void setGebruiker(Identifiable gebruiker) {
		this.gebruiker = gebruiker;
	}

}
