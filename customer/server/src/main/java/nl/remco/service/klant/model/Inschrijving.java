package nl.remco.service.klant.model;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.organisatie.model.OrganisatieDataHolder;

public class Inschrijving implements OrganisatieDataHolder {
	
	public enum Rol{ Medewerker, Manager};

	private String id;
	private Identifiable organisatie;
	private String product;
	private String geplandePeriode;
	private Rol rol;

	
	public Identifiable getOrganisatie() {
		return organisatie;
	}
	public void setOrganisatie(Identifiable organisatie) {
		this.organisatie = organisatie;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getGeplandePeriode() {
		return geplandePeriode;
	}
	public void setGeplandePeriode(String geplandePeriode) {
		this.geplandePeriode = geplandePeriode;
	}
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
			return "Inschrijving {id: " + id + ", leerrichting:" + product
					+ ", jaargroep:" + geplandePeriode
					+ ", organisatie:" + organisatie
					+ "}";
	}
}
