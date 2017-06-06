package nl.remco.group.service;

import org.springframework.stereotype.Component;

import nl.remco.group.service.domain.RGroup;
import nl.remco.group.service.domain.Membership;
import nl.remco.group.service.domain.Organisation;
import nl.remco.group.service.domain.Person;
import nl.remco.group.service.domain.Scope;
import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import nl.remco.group.service.dto.OrganisationDTO;
import nl.remco.group.service.dto.PersonDTO;
import nl.remco.group.service.dto.ScopeDTO;

@Component
class Converter {
  GroupDTO convertToDTO(final RGroup model) {
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


  RGroup convertfromDTO(final GroupDTO dto) {
    RGroup group = new RGroup();
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
      group.setMaster(new RGroup(dto.getMaster().getId()));
    }
    return group;
  }
}
