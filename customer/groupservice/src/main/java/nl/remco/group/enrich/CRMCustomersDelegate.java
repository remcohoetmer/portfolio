package nl.remco.group.enrich;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import nl.remco.group.organisation.service.CRMOrganisation;

@Component
public class CRMCustomersDelegate {
	
	Map<String,CRMPerson> persons= new HashMap<>();
	
	public CompletableFuture<CRMPerson> findPerson( String id) {

		
		return CompletableFuture.supplyAsync( () ->persons.get( id));
	}

	public CRMCustomersDelegate() {
		CRMOrganisation org= new CRMOrganisation( "8000");
		add( new CRMPerson( "person1", "Bob", "de Vries", "bob@outook.com", new Date(), org));
		add( new CRMPerson( "person2", "Karel", "Bakker", "karel@yahoo.com", new Date(), org));
		add( new CRMPerson( "person3", "Hugo", "Dijkstra", "hugo@gmail.com", new Date(), org));
		add( new CRMPerson( "person4", "Greet", "Huus", "greet@kpnmail.nl", new Date(), org));
	}

	private void add(CRMPerson crmPerson) {
		persons.put( crmPerson.getId(), crmPerson);
	}

}
