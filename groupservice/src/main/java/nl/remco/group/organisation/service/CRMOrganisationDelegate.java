package nl.remco.group.organisation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


@Component
final class CRMOrganisationDelegate {
  private static final Logger log = Logger.getLogger(CRMOrganisationDelegate.class.getName());

  @Autowired
  CRMOrganisations crmOrganisations;
  private ConcurrentHashMap<String, Mono<CRMOrganisation>> cache =
    new ConcurrentHashMap<>();


  public Mono<CRMOrganisation> getOrganisation(final String organisationId) {
    Mono<CRMOrganisation> f = cache.get(organisationId);
    if (f == null) {
      Mono<CRMOrganisation> futuretask = crmOrganisations.retrieveOrganisation(organisationId);
      Mono<CRMOrganisation> futuretaskWinner = cache.putIfAbsent(organisationId, futuretask);
      if (futuretaskWinner == null) {
        // the new task is into the cache
        return futuretask;
      } else {
        // there was already a task in the cash, the newly created tasks is ignored
        return futuretaskWinner;
      }
    }
    log.info("Get from organisation cache id=" + organisationId);
    return f;
  }

  public Flux<CRMOrganisation> getOrganisations() {
    return crmOrganisations.retrieveOrganisations();
  }

}
