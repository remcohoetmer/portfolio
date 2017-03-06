package nl.remco.group.service;

public class GRP_Selection {

	private boolean selectPersons;
	private boolean selectOrganisations;
	private boolean selectScopes;
	private boolean selectMaster;

	
	public boolean isSelectPersons() {
		return selectPersons;
	}
	public void setSelectPersons(boolean selectPersons) {
		this.selectPersons = selectPersons;
	}
	public boolean isSelectOrganisations() {
		return selectOrganisations;
	}
	public void setSelectOrganisations(boolean selectOrganisations) {
		this.selectOrganisations = selectOrganisations;
	}
	public boolean isSelectMaster() {
		return selectMaster;
	}
	public void setSelectMaster(boolean selectMaster) {
		this.selectMaster = selectMaster;
	}

	public boolean isSelectScopes() {
		return selectScopes;
	}
	public void setSelectScopes(boolean selectScopes) {
		this.selectScopes = selectScopes;
	}

}