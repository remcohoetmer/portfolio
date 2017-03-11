package nl.remco.group.organisation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;



@Component
final class CRMOrganisations {
	private Map<String,CRMOrganisation> organisations= new HashMap<>();
	public CompletableFuture<CRMOrganisation> retrieveOrganisation( final String organisationId)
	{
		return CompletableFuture.supplyAsync( () ->organisations.get( organisationId));
	}
	
	public CRMOrganisations() {

		add( new CRMOrganisation( "8000", "The Floor", "active"));
		add( new CRMOrganisation( "8001", "The Wall", "active"));
		add( new CRMOrganisation( "8002", "The Shack", "active"));
	}

	private void add(CRMOrganisation org) {
		organisations.put( org.getId(), org);
	}

	public CompletableFuture<List<CRMOrganisation>> retrieveOrganisations() {
		return CompletableFuture.supplyAsync( () ->organisations.values().stream().collect(Collectors.toList()));
	}
}
