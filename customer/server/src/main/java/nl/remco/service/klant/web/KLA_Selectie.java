package nl.remco.service.klant.web;

public class KLA_Selectie {

	private boolean selectSleutels;
	private boolean selectOrganisaties;

	public boolean isSelectOrganisaties() {
		return selectOrganisaties;
	}
	public void setSelectOrganisaties(boolean selectOrganisaties) {
		this.selectOrganisaties = selectOrganisaties;
	}
	public boolean isSelectSleutels() {
		return selectSleutels;
	}
	public void setSelectSleutels(boolean selectSleutels) {
		this.selectSleutels = selectSleutels;
	}

}