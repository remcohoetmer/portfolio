package nl.remco.service.groep.web;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.groep.model.Lidmaatschap;

public class GRP_SearchForKlantRequest {
	public static class Filter {

		private String klantId;//verplicht
		private Lidmaatschap.Rol rol;
		private Status lidmaatschapStatus;

		private Status groepStatus;
		private Identifiable organisatie;
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
		public String getKlantId() {
			return klantId;
		}
		public void setKlantId(String klantId) {
			this.klantId = klantId;
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
