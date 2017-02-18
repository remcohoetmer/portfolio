package nl.remco.service.groep.web;

import java.util.List;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.common.web.IDList;

public class GRP_GetRequest extends IDList  {
	private GRP_Selectie selectie;
	private Filter filter;

	public static class Filter {
		private String naam;//patterns mogelijk: begint met "Groep__"
		private String beschrijving;// patterns mogelijk: begint met "Groep_"
		private LifeCycleBeheer.Status status;
		private LifeCycleBeheer.Status statusLidmaatschap;
		private String groepscode;
		private Identifiable organisatie;
		private Identifiable locatie;
		private Identifiable scope;
		private String product;
		private String geplandePeriode;
		private List<String> kenmerken;// lijst met kenmerken die de groep heeft (disjunctief)

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
		public LifeCycleBeheer.Status getStatusLidmaatschap() {
			return statusLidmaatschap;
		}
		public void setStatusLidmaatschap(LifeCycleBeheer.Status statusLidmaatschap) {
			this.statusLidmaatschap = statusLidmaatschap;
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
		public LifeCycleBeheer.Status getStatus() {
			return status;
		}
		public void setStatus(LifeCycleBeheer.Status status) {
			this.status = status;
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
		public List<String> getKenmerken() {
			return kenmerken;
		}
		public void setKenmerken(List<String> kenmerken) {
			this.kenmerken = kenmerken;
		}
	};
	public Filter getFilter() {
		return filter;
	}
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	public GRP_Selectie getSelectie() {
		return selectie;
	}

	public void setSelectie(GRP_Selectie selectie) {
		this.selectie = selectie;
	}
}
