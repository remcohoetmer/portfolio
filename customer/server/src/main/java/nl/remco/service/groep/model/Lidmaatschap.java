package nl.remco.service.groep.model;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;

public class Lidmaatschap extends LifeCycleBeheer {
	public enum Rol { GROEPSLEIDER, GROEPSLID};
	private Rol rol;
	private Identifiable klant;
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	public Identifiable getKlant() {
		return klant;
	}
	public void setKlant(Identifiable klant) {
		this.klant = klant;
	}
	@Override
	public String toString()
	{
		return "Lidmaatschap van " + klant==null?"Onbekende gebruiker":klant.toString();
	}
}
