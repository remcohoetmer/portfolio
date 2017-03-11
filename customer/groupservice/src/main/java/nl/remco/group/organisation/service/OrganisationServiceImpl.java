package nl.remco.group.organisation.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.remco.group.service.dto.OrganisationDTO;
@Service
public class OrganisationServiceImpl implements OrganisationService {
	@Autowired
	OrganisationEnricher organisationEnricher;
	@Override
	public CompletableFuture<List<OrganisationDTO>> find() {
		return organisationEnricher.getOrganisations();
	}

}
