package nl.remco.service.groep.web;

import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.Lidmaatschap;

public class GRP_LidmaatschapMetGroep extends LifeCycleBeheer {

	private Lidmaatschap.Rol rol;
	private Groep groep;
	
	public Lidmaatschap.Rol getRol() {
		return rol;
	}
	public void setRol(Lidmaatschap.Rol rol) {
		this.rol = rol;
	}
	public Groep getGroep() {
		return groep;
	}
	public void setGroep(Groep groep) {
		this.groep = groep;
	}
}
