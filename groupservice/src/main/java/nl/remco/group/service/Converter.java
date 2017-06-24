package nl.remco.group.service;

import nl.remco.group.service.domain.*;
import nl.remco.group.service.dto.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
class Converter {
  GroupDTO convertToDTO(final Group model) {
    if (model == null) {
      return new GroupDTO();
    }
    GroupDTO dto = new GroupDTO(model.getId());
    dto.setStatus(model.getStatus());
    dto.setName(model.getName());
    dto.setDescription(model.getDescription());
    dto.setCode(model.getCode());
    if (model.getScope() != null) {
      dto.setScope(new ScopeDTO(model.getScope().getId()));
    }
    if (model.getOrganisation() != null) {
      dto.setOrganisation(new OrganisationDTO(model.getOrganisation().getId()));
    }

    for (Membership ms : model.getMemberships()) {
      MembershipDTO mbDTO = new MembershipDTO();
      mbDTO.setPerson(new PersonDTO(ms.getPerson().getId()));
      mbDTO.setRole(ms.getRole());
      dto.getMemberships().add(mbDTO);
    }

    for (String feature : model.getFeatures()) {
      dto.getFeatures().add(feature);
    }
    if (model.getMaster() != null) {
      dto.setMaster(new GroupDTO(model.getMaster().getId()));
    }
    return dto;
  }

  Mono<GroupDTO> convertToDTO_RS(final Group group) {
    return Mono.just(convertToDTO(group));
  }

  Group convertfromDTO(final GroupDTO dto) {
    Group group = new Group();
    group.setId(dto.getId());
    group.setName(dto.getName());
    group.setDescription(dto.getDescription());
    group.setStatus(dto.getStatus());
    group.setCode(dto.getCode());
    if (dto.getScope() != null) {
      group.setScope(new Scope(dto.getScope().getId()));
    }
    if (dto.getOrganisation() != null) {
      group.setOrganisation(new Organisation(dto.getOrganisation().getId()));
    }
    for (MembershipDTO msDTO : dto.getMemberships()) {
      Membership mb = new Membership();
      mb.setPerson(new Person(msDTO.getPerson().getId(), null));
      mb.setRole(msDTO.getRole());
      group.getMemberships().add(mb);
    }
    for (String feature : dto.getFeatures()) {
      group.getFeatures().add(feature);
    }
    if (dto.getMaster() != null) {
      group.setMaster(new Group(dto.getMaster().getId()));
    }
    return group;
  }
}
