package nl.remco.group.enrich;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import nl.remco.group.service.dto.OrganisationDTO;
import nl.remco.group.service.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PersonEnricher {
  @Autowired
  private CRMCustomersDelegate crmCustomersDelegate;

  private PersonDTO copyFrom(PersonDTO personDTO, CRMPerson crmPerson) {
    if (crmPerson != null) {
      personDTO.setName(crmPerson.getName());
      personDTO.setSurname(crmPerson.getSurname());
      personDTO.setEmail(crmPerson.getEmail());
      personDTO.setDateofbirth(crmPerson.getDateofbirth());
      if (crmPerson.getOrganisation() != null) {
        personDTO.setOrganisation(new OrganisationDTO(crmPerson.getOrganisation().getId()));

      }
    }
    return personDTO;
  }

  private Mono<PersonDTO> enrichPersons(PersonDTO personDTO) {
    return crmCustomersDelegate
      .findPerson(personDTO.getId())
      .map(crmPerson -> copyFrom(personDTO, crmPerson));
  }

  public Mono<GroupDTO> enrichPersons(final GroupDTO group) {
    Mono<PersonDTO> chain = null;

    for (MembershipDTO membership : group.getMemberships()) {
      Mono<PersonDTO> enrichedPerson = enrichPersons(membership.getPerson());
      if (chain == null) {
        chain = enrichedPerson;
      } else {
        chain = chain.then(enrichedPerson);
      }
    }
    Mono<GroupDTO> result = Mono.just(group);
    return (chain == null) ? result : chain.then(result);

  }

}
