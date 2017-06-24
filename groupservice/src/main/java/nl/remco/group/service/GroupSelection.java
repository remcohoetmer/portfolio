package nl.remco.group.service;

public class GroupSelection {
	private boolean selectPersons;
	private boolean selectOrganisations;
	private boolean selectScopes;
	private boolean selectMasters;

	public boolean isSelectPersons() {
		return selectPersons;
	}
	public void setSelectPersons() {
		this.selectPersons = true;
	}
	public boolean isSelectOrganisations() {
		return selectOrganisations;
	}
	public void setSelectOrganisations() {
		this.selectOrganisations = true;
	}
	public boolean isSelectMasters() {
		return selectMasters;
	}
	public void setSelectMasters() {
		this.selectMasters = true;
	}

	public boolean isSelectScopes() {
		return selectScopes;
	}
	public void setSelectScopes() {
		this.selectScopes = true;
	}

}