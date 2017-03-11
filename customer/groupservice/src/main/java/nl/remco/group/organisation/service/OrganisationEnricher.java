package nl.remco.group.organisation.service;

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
						convertDTO(orgDTO,  crmOrganisation);
						return groupDTO;	
					});
		}
		return CompletableFuture.completedFuture(groupDTO);
	}

	public List<CompletableFuture<GroupDTO>> enrichOrganisations(final List<GroupDTO> groups) {
		return groups.stream().map( this::enrichOrganisations).collect( Collectors.toList());
	}
	
	public CompletableFuture<List<OrganisationDTO>> getOrganisations( )
	{
		return crmOrganisationDelegate
				.getOrganisations()
				.thenApply( list -> list.stream().map( this::convertDTO).collect(Collectors.toList()));
	}
	
	private OrganisationDTO convertDTO( OrganisationDTO organisationDTO, final CRMOrganisation crmOrganisation)
	{
		organisationDTO.setId(crmOrganisation.getId());
		organisationDTO.setName(crmOrganisation.getName());
		organisationDTO.setStatus(crmOrganisation.getStatus());
		return organisationDTO;
	}

	private OrganisationDTO convertDTO( final CRMOrganisation crmOrganisation)
	{
		return convertDTO( new OrganisationDTO(), crmOrganisation);
	}
}
