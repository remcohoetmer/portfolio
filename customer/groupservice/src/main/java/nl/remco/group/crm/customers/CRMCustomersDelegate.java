package nl.remco.group.crm.customers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import nl.remco.group.service.dto.OrganisationDTO;
import nl.remco.group.service.dto.PersonDTO;

@Component
public class CRMCustomersDelegate {
	
	Map<String,PersonDTO> persons= new HashMap<>();
	
	public CompletableFuture<PersonDTO> findPerson( String id) {

		
		return CompletableFuture.supplyAsync( () ->persons.get( id));
	}

	public CRMCustomersDelegate() {
		OrganisationDTO org= new OrganisationDTO( "8000");
		add( new PersonDTO( "person1", "Bob", "de Vries", "bob@outook.com", new Date(), org));
		add( new PersonDTO( "person2", "Karel", "Bakker", "karel@yahoo.com", new Date(), org));
		add( new PersonDTO( "person3", "Hugo", "Dijkstra", "hugo@gmail.com", new Date(), org));
	}

	private void add(PersonDTO personDTO) {
		persons.put( personDTO.getId(), personDTO);
	}

}
