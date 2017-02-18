package nl.remco.service.groep.web;

import java.util.List;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.common.model.LifeCycleBeheer;


public class GRP_UpdateRequest extends Identifiable {
	private List<GRP_LidmaatschapCreateUpdate> createLidmaatschappen;
	private List<GRP_LidmaatschapCreateUpdate> updateLidmaatschappen;
	private List<Identifiable> deleteLidmaatschappen;
	private String naam;
	private String beschrijving;
	private LifeCycleBeheer.Status status;
	
	public List<GRP_LidmaatschapCreateUpdate> getCreateLidmaatschappen() {
		return createLidmaatschappen;
	}
	public void setCreateLidmaatschappen(
			List<GRP_LidmaatschapCreateUpdate> createLidmaatschappen) {
		this.createLidmaatschappen = createLidmaatschappen;
	}
	public List<GRP_LidmaatschapCreateUpdate> getUpdateLidmaatschappen() {
		return updateLidmaatschappen;
	}
	public void setUpdateLidmaatschappen(
			List<GRP_LidmaatschapCreateUpdate> updateLidmaatschappen) {
		this.updateLidmaatschappen = updateLidmaatschappen;
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
	public List<Identifiable> getDeleteLidmaatschappen() {
		return deleteLidmaatschappen;
	}
	public void setDeleteLidmaatschappen(List<Identifiable> deleteLidmaatschappen) {
		this.deleteLidmaatschappen = deleteLidmaatschappen;
	}
	
}