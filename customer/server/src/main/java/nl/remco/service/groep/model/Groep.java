package nl.remco.service.groep.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import nl.remco.service.common.model.Benoembaar;
import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.organisatie.model.OrganisatieDataHolder;


public class Groep extends LifeCycleBeheer implements OrganisatieDataHolder {

	private List<Lidmaatschap> lidmaatschappen;

	private String naam;
	private String beschrijving;
	private String groepscode;
	private String product;
	private String geplandePeriode;
	private Identifiable scope;
	private Benoembaar organisatie;
	private List<String> kenmerken;
	private Identifiable hoofdgroep;
	private GroepsMutatieType groepsMutatieType;
	private Identifiable aangemaaktDoor;

	public GroepsMutatieType getGroepsMutatieType() {
		return groepsMutatieType;
	}
	public void setGroepsMutatieType(GroepsMutatieType groepsMutatieType) {
		this.groepsMutatieType = groepsMutatieType;
	}

	public String getGroepscode() {
		return groepscode;
	}
	public void setGroepscode(String groepscode) {
		this.groepscode = groepscode;
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
	public Identifiable getScope() {
		return scope;
	}
	public void setScope(Identifiable scope) {
		this.scope = scope;
	}
	public Benoembaar getOrganisatie() {
		return organisatie;
	}
	public void setOrganisatie(Benoembaar organisatie) {
		this.organisatie = organisatie;
	}

	public List<String> getKenmerken() {
		return kenmerken;
	}
	public void setKenmerken(List<String> kenmerken) {
		this.kenmerken = kenmerken;
	}


	public List<Lidmaatschap> getLidmaatschappen() {
		if (lidmaatschappen==null) {
			lidmaatschappen= new ArrayList<Lidmaatschap>();
		}
		return lidmaatschappen;
	}
	public void setLidmaatschappen(List<Lidmaatschap> lidmaatschappen) {
		this.lidmaatschappen = lidmaatschappen;
	}
	public String getNaam() {
		return naam;
	}
	public void setNaam(String naam) {
		this.naam = naam;
	}
	public String getBeschrijving() {
		return beschrijving;
	}
	public void setBeschrijving(String beschrijving) {
		this.beschrijving = beschrijving;
	}
	public Identifiable getHoofdgroep() {
		return hoofdgroep;
	}
	public void setHoofdgroep(Identifiable hoofdgroep) {
		this.hoofdgroep = hoofdgroep;
	}
	@JsonIgnore
	public Identifiable getAangemaaktDoor() {
		return aangemaaktDoor;
	}
	public void setAangemaaktDoor(Identifiable aangemaaktDoor) {
		this.aangemaaktDoor = aangemaaktDoor;
	}

	@Override
	public String toString()
	{
		return "Groep " + naam;
	}

}