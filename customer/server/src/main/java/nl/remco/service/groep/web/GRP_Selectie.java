package nl.remco.service.groep.web;

public class GRP_Selectie {

	private boolean selectSleutels;
	private boolean selectLidmaatschappen;
	private boolean selectKlanten;
	private boolean selectOrganisaties;
	private boolean selectScopes;
	private boolean selectKenmerken;
	private boolean selectHoofdgroep;

	public boolean isSelectHoofdgroep() {
		return selectHoofdgroep;
	}
	public void setSelectHoofdgroep(boolean selectHoofdgroep) {
		this.selectHoofdgroep = selectHoofdgroep;
	}
	public boolean isSelectOrganisaties() {
		return selectOrganisaties;
	}
	public void setSelectOrganisaties(boolean selectOrganisaties) {
		this.selectOrganisaties = selectOrganisaties;
	}
	public boolean isSelectScopes() {
		return selectScopes;
	}
	public void setSelectScopes(boolean selectScopes) {
		this.selectScopes = selectScopes;
	}
	public boolean isSelectLidmaatschappen() {
		return selectLidmaatschappen;
	}
	public void setSelectLidmaatschappen(boolean selectLidmaatschappen) {
		this.selectLidmaatschappen = selectLidmaatschappen;
	}
	public boolean isSelectKlanten() {
		return selectKlanten;
	}
	public void setSelectKlanten(boolean selectKlanten) {
		this.selectKlanten = selectKlanten;
	}

	public boolean isSelectKenmerken() {
		return selectKenmerken;
	}
	public void setSelectKenmerken(boolean selectKenmerken) {
		this.selectKenmerken = selectKenmerken;
	}
	public boolean isSelectSleutels() {
		return selectSleutels;
	}
	public void setSelectSleutels(boolean selectSleutels) {
		this.selectSleutels = selectSleutels;
	}

}