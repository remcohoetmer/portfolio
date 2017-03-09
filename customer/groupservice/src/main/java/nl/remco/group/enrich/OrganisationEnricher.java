package nl.remco.group.enrich;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

	public CompletableFuture<List<GroupDTO>> enrichOrganisations(final List<GroupDTO> groups) {
		List<CompletableFuture<GroupDTO>> list= new ArrayList<>();
		for (GroupDTO group: groups) {
			list.add( enrichOrganisations(group));
		}
		return CompletableFuture.allOf(list.stream().toArray(CompletableFuture[]::new))
				.thenApply(dummy->{ return groups;});
	}
}
