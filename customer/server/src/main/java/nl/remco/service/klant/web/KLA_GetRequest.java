package nl.remco.service.klant.web;

import java.util.Date;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.common.web.IDList;
import nl.remco.service.klant.model.Geslacht;
import nl.remco.service.klant.model.Inschrijving;

public class KLA_GetRequest extends IDList {
	private KLA_Selectie selectie;
	private Filter filter;

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}


	
	public KLA_Selectie getSelectie() {
		return selectie;
	}

	public void setSelectie(KLA_Selectie selectie) {
		this.selectie = selectie;
	}
	


	public static class Filter {
		private String voornaam;
		private String achternaam;
		private String emailAdres;
		private Geslacht geslacht;
		private LifeCycleBeheer.Status status;
		private Date geboortedatum;
		private String product;
		private String geplandePeriode;
		private Identifiable organisatie;
		private Identifiable locatie;
		private Inschrijving.Rol rol;
		private Identifiable aangemaaktDoor;
		
		public String getProduct() {
			return product;
		}
		public void setProduct(String product) {
			this.product = product;
		}
		public String getJaargroep() {
			return geplandePeriode;
		}
		public void setJaargroep(String jaargroep) {
			this.geplandePeriode = jaargroep;
		}
		public Inschrijving.Rol getRol() {
			return rol;
		}
		public void setRol(Inschrijving.Rol rol) {
			this.rol = rol;
		}
		public String getEmailAdres() {
			return emailAdres;
		}
		public void setEmailAdres(String emailAdres) {
			this.emailAdres = emailAdres;
		}
		public LifeCycleBeheer.Status getStatus() {
			return status;
		}
		public void setStatus(LifeCycleBeheer.Status status) {
			this.status = status;
		}
		public String getVoornaam() {
			return voornaam;
		}
		public void setVoornaam(String voornaam) {
			this.voornaam = voornaam;
		}
		public String getAchternaam() {
			return achternaam;
		}
		public void setAchternaam(String achternaam) {
			this.achternaam = achternaam;
		}
		public Geslacht getGeslacht() {
			return geslacht;
		}
		public void setGeslacht(Geslacht geslacht) {
			this.geslacht = geslacht;
		}
		public Date getGeboortedatum() {
			return geboortedatum;
		}
		public void setGeboortedatum(Date geboortedatum) {
			this.geboortedatum = geboortedatum;
		}
		public Identifiable getOrganisatie() {
			return organisatie;
		}
		public void setOrganisatie(Identifiable organisatie) {
			this.organisatie = organisatie;
		}
		public Identifiable getLocatie() {
			return locatie;
		}
		public void setLocatie(Identifiable locatie) {
			this.locatie = locatie;
		}
		public Identifiable getAangemaaktDoor() {
			return aangemaaktDoor;
		}
		public void setAangemaaktDoor(Identifiable aangemaaktDoor) {
			this.aangemaaktDoor = aangemaaktDoor;
		}
	}
	public KLA_GetRequest(){
		filter= new Filter();
		selectie= new KLA_Selectie();
	}
	
}
