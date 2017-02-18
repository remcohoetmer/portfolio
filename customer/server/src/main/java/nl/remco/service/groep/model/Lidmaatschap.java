package nl.remco.service.groep.model;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;

public class Lidmaatschap extends LifeCycleBeheer {
	public enum Rol { GROEPSLEIDER, GROEPSLID};
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	public Identifiable getGebruiker() {
		return gebruiker;
	}
	public void setGebruiker(Identifiable gebruiker) {
		this.gebruiker = gebruiker;
	}
	private Rol rol;
	private Identifiable gebruiker;
	@Override
	public String toString()
	{
		return "Lidmaatschap van " + gebruiker==null?"Onbekende gebruiker":gebruiker.toString();
	}
}
