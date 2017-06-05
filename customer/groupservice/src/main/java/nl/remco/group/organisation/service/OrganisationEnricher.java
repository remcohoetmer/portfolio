package nl.remco.group.organisation.service;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.OrganisationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OrganisationEnricher {
  private static final Logger log = LoggerFactory.getLogger(OrganisationEnricher.class);
  @Autowired
  CRMOrganisationDelegate crmOrganisationDelegate;

  public Mono<GroupDTO> enrichOrganisations(GroupDTO groupDTO) {

    OrganisationDTO orgDTO = groupDTO.getOrganisation();
    if (orgDTO != null && orgDTO.getId() != null && !orgDTO.getId().isEmpty()) {

      return crmOrganisationDelegate.getOrganisation(orgDTO.getId())
        .map(crmOrganisation -> {
          convertDTO(orgDTO, crmOrganisation);
          return groupDTO;
        });
    }
    return Mono.just(groupDTO);
  }


  public Flux<OrganisationDTO> getOrganisations() {
    return crmOrganisationDelegate
      .getOrganisations()
      .map(this::convertDTO);
  }

  private OrganisationDTO convertDTO(OrganisationDTO organisationDTO, final CRMOrganisation crmOrganisation) {
    if (crmOrganisation != null) {
      organisationDTO.setId(crmOrganisation.getId());
      organisationDTO.setName(crmOrganisation.getName());
      organisationDTO.setStatus(crmOrganisation.getStatus());
    }
    return organisationDTO;
  }

  private OrganisationDTO convertDTO(final CRMOrganisation crmOrganisation) {
    return convertDTO(new OrganisationDTO(), crmOrganisation);
  }
}
