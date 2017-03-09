package nl.remco.group.enrich;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import nl.remco.group.service.dto.OrganisationDTO;
import nl.remco.group.service.dto.PersonDTO;

@Component
public class PersonEnricher {
	@Autowired
	private CRMCustomersDelegate crmCustomersDelegate;

	private PersonDTO copyFrom(PersonDTO personDTO, CRMPerson crmPerson) {
		personDTO.setName( crmPerson.getName());
		personDTO.setSurname( crmPerson.getSurname());
		personDTO.setEmail( crmPerson.getEmail());
		personDTO.setDateofbirth( crmPerson.getDateofbirth());
		personDTO.setOrganisation( new OrganisationDTO( crmPerson.getOrganisation().getId()));
		return personDTO;
	}

	private CompletableFuture<PersonDTO> enrichPersons( PersonDTO personDTO)
	{
		return crmCustomersDelegate
				.findPerson( personDTO.getId())
				.thenApply( crmPerson -> copyFrom( personDTO, crmPerson));
	}

	public CompletableFuture<GroupDTO> enrichPersons(final GroupDTO group) {
		List<CompletableFuture<PersonDTO>> list= new ArrayList<>();
		for (MembershipDTO membership: group.getMemberships()) {
			list.add( enrichPersons( membership.getPerson()));

		}
		return CompletableFuture.allOf(list.stream().toArray(CompletableFuture[]::new))
				.thenApply(dummy->{ return group;});
	}

	public List<CompletableFuture<PersonDTO>> enrichPersons(final List<GroupDTO> groups) {
		
		return groups.stream()
				.flatMap( group->group.getMemberships().stream())
				.map(membership-> membership.getPerson())
				.map(this::enrichPersons)
				.collect( Collectors.toList());

	}
}
