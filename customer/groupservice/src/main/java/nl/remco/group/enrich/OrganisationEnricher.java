package nl.remco.group.enrich;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.OrganisationDTO;

@Component
public class OrganisationEnricher {
	private static final Logger log = LoggerFactory.getLogger(OrganisationEnricher.class);
	@Autowired
	CRMOrganisationDelegate crmOrganisationDelegate;

	public CompletableFuture<GroupDTO> enrichOrganisations(GroupDTO groupDTO) {

		OrganisationDTO orgDTO= groupDTO.getOrganisation();
		if (orgDTO!=null && orgDTO.getId()!= null && !orgDTO.getId().isEmpty()) {

			return crmOrganisationDelegate.getOrganisation(orgDTO.getId())
					.thenApply( crmOrganisation-> {
						orgDTO.setName( crmOrganisation.getName());
						orgDTO.setStatus( crmOrganisation.getStatus());
						return groupDTO;	
					});
		}
		return CompletableFuture.completedFuture(groupDTO);
	}

	public List<CompletableFuture<GroupDTO>> enrichOrganisations(final List<GroupDTO> groups) {
		return groups.stream().map( this::enrichOrganisations).collect( Collectors.toList());
	}
}
