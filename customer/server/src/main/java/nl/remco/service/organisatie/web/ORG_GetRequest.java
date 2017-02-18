package nl.remco.service.organisatie.web;

import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.common.web.IDList;

public class ORG_GetRequest extends IDList {
	private ORG_Selectie selectie;
	private Filter filter;

	public static class Filter {
		private String naam;
		private LifeCycleBeheer.Status status;
		
		
		public String getNaam() {
			return naam;
		}
		public void setNaam(String naam) {
			this.naam = naam;
		}
		public LifeCycleBeheer.Status getStatus() {
			return status;
		}
		public void setStatus(LifeCycleBeheer.Status status) {
			this.status = status;
		}
	};

	public Filter getFilter() {
		return filter;
	}
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	public ORG_Selectie getSelectie() {
		return selectie;
	}

	public void setSelectie(ORG_Selectie selectie) {
		this.selectie = selectie;
	}
	
}
