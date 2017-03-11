package nl.remco.group.enrich;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
final class CRMOrganisationDelegate {
	private static final Logger LOG = Logger.getLogger(CRMOrganisationDelegate.class.getName());

	@Autowired
	CRMOrganisations crmOrganisations;
	private ConcurrentHashMap<String, CompletableFuture<CRMOrganisation>> cache=
			new ConcurrentHashMap<>();


	public CompletableFuture<CRMOrganisation> getOrganisation( final String organisationId)
	{
		CompletableFuture<CRMOrganisation> f = cache.get(organisationId);
		if (f == null) {
			// problem: if we create a CompletableFuture, the thread starts automatically
			// solution: we create a trigger task on which the data retrieval is dependent
			CompletableFuture<Void> trigger = new CompletableFuture<>();
			CompletableFuture<CRMOrganisation> futuretask= trigger
					.thenCompose(dummy->crmOrganisations.retrieveOrganisation(organisationId));
			CompletableFuture<CRMOrganisation> futuretask2= cache.putIfAbsent(organisationId, futuretask);
			if (futuretask2==null) {
				// the new task is put into the cache
				// start futuretask
				trigger.complete( null);//
				return futuretask;
			} else {
				// there was already a task in the cash, the newly created tasks must be cancelled
				trigger.cancel(true);
				futuretask.cancel(true);
				return futuretask2;
			}
		}
		LOG.info("Get from organisation cache id="+ organisationId );
		return f;
	}


}
