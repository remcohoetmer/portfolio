package nl.remco.service.groep.web;

import java.util.List;

import nl.remco.service.common.model.Identifiable;

public class GRP_KlantMetGroepen extends Identifiable {
	
    private List<GRP_LidmaatschapMetGroep> lidmaatschappen;

	public List<GRP_LidmaatschapMetGroep> getLidmaatschappen() {
		return lidmaatschappen;
	}

	public void setLidmaatschappen(List<GRP_LidmaatschapMetGroep > lidmaatschappen) {
		this.lidmaatschappen = lidmaatschappen;
	}
}
