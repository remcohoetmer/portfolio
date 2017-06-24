package nl.remco.group.organisation.service;

import nl.remco.group.service.dto.OrganisationDTO;
import reactor.core.publisher.Flux;

public interface OrganisationService {
  Flux<OrganisationDTO> find();
}
