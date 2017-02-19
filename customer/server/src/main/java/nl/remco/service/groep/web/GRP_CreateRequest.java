package nl.remco.service.groep.web;

import java.util.List;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.groep.model.GroepsMutatieType;


public class GRP_CreateRequest {
	private List<GRP_LidmaatschapCreateUpdate> lidmaatschappen;

	private String naam;
	private String beschrijving;
	private Status status;
	private String groepscode;
	private String product;
	private String geplandePeriode;
	private Identifiable scope;
	private Identifiable organisatie;
	
	private List<String> kenmerken;
	private Identifiable hoofdgroep;

	private GroepsMutatieType groepsMutatieType;

	public List<GRP_LidmaatschapCreateUpdate> getLidmaatschappen() {
		return lidmaatschappen;
	}

	public void setLidmaatschappen(
			List<GRP_LidmaatschapCreateUpdate> lidmaatschappen) {
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

	public Identifiable getOrganisatie() {
		return organisatie;
	}

	public void setOrganisatie(Identifiable organisatie) {
		this.organisatie = organisatie;
	}

	public List<String> getKenmerken() {
		return kenmerken;
	}

	public void setKenmerken(List<String> kenmerken) {
		this.kenmerken = kenmerken;
	}

	public Identifiable getHoofdgroep() {
		return hoofdgroep;
	}

	public void setHoofdgroep(Identifiable hoofdgroep) {
		this.hoofdgroep = hoofdgroep;
	}

	public GroepsMutatieType getGroepsMutatieType() {
		return groepsMutatieType;
	}

	public void setGroepsMutatieType(GroepsMutatieType groepsMutatieType) {
		this.groepsMutatieType = groepsMutatieType;
	}
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getGroepscode() {
		return groepscode;
	}

	public void setGroepscode(String groepscode) {
		this.groepscode = groepscode;
	}

	// eigenaar wordt afgeleid
	
}