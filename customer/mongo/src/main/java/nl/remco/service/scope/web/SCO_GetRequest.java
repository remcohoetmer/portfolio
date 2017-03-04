package nl.remco.service.scope.web;

import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.common.web.IDList;

public class SCO_GetRequest extends IDList {
	private SCO_Selectie selectie;
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
	public SCO_Selectie getSelectie() {
		return selectie;
	}

	public void setSelectie(SCO_Selectie selectie) {
		this.selectie = selectie;
	}
	
}
