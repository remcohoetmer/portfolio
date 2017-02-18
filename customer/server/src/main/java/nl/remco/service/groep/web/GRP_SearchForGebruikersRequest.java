package nl.remco.service.groep.web;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.groep.model.Lidmaatschap;

public class GRP_SearchForGebruikersRequest {
	public static class Filter {

		private String gebruikerID;//verplicht
		private Lidmaatschap.Rol rol;
		private Status lidmaatschapStatus;

		private Status groepStatus;
		private Identifiable organisatie;
		private Identifiable locatie;
		private Identifiable scope;
		

		public Lidmaatschap.Rol getRol() {
			return rol;
		}
		public void setRol(Lidmaatschap.Rol rol) {
			this.rol = rol;
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
		public Identifiable getScope() {
			return scope;
		}
		public void setScope(Identifiable scope) {
			this.scope = scope;
		}
		public Status getLidmaatschapStatus() {
			return lidmaatschapStatus;
		}
		public void setLidmaatschapStatus(Status lidmaatschapStatus) {
			this.lidmaatschapStatus = lidmaatschapStatus;
		}
		public Status getGroepStatus() {
			return groepStatus;
		}
		public void setGroepStatus(Status groepStatus) {
			this.groepStatus = groepStatus;
		}
		public String getGebruikerID() {
			return gebruikerID;
		}
		public void setGebruikerID(String gebruikerID) {
			this.gebruikerID = gebruikerID;
		}
	};
	public static class Selectie {
	};
	public Filter getFilter() {
		return filter;
	}
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	public Selectie getSelectie() {
		return selectie;
	}
	public void setSelectie(Selectie selectie) {
		this.selectie = selectie;
	}
	public Filter filter;
	public Selectie selectie;

}
