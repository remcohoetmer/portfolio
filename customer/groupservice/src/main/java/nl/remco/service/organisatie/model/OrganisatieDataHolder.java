package nl.remco.service.organisatie.model;

import nl.remco.service.common.model.Benoembaar;

public interface OrganisatieDataHolder {
	Benoembaar getOrganisatie();
	void setOrganisatie(Benoembaar organisatie);
}
