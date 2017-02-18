package nl.remco.service.organisatie.model;

import nl.remco.service.common.model.Identifiable;

public interface OrganisatieDataHolder {
	Identifiable getOrganisatie();
	void setOrganisatie(Identifiable organisatie);
}
