package nl.remco.group.domain;


public class Membership  {

	private String rol;
	private Person persoon;
	
	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public Person getPersoon() {
		return persoon;
	}

	public void setPersoon(Person persoon) {
		this.persoon = persoon;
	}

	@Override
	public String toString()
	{
		return "Lidmaatschap van " + persoon==null?"Onbekende gebruiker":persoon.toString();
	}
}
