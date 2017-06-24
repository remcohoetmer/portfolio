package nl.remco.group.organisation.service;

import nl.remco.group.service.dto.OrganisationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class OrganisationServiceImpl implements OrganisationService {
  @Autowired
  OrganisationEnricher organisationEnricher;

  @Override
  public Flux<OrganisationDTO> find() {
    return organisationEnricher.getOrganisations();
  }

}
