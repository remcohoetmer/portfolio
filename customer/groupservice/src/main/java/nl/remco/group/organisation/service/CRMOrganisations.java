package nl.remco.group.organisation.service;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
final class CRMOrganisations {
  private Map<String, CRMOrganisation> organisations = new HashMap<>();

  public Mono<CRMOrganisation> retrieveOrganisation(final String organisationId) {
    CRMOrganisation org = organisations.get(organisationId);
    if (org == null) {
      return Mono.empty();
    }

    return Mono.just(org);
  }

  public CRMOrganisations() {
    add(new CRMOrganisation("8000", "The Floor", "active"));
    add(new CRMOrganisation("8001", "The Wall", "active"));
    add(new CRMOrganisation("8002", "The Shack", "active"));
  }

  private void add(CRMOrganisation org) {
    organisations.put(org.getId(), org);
  }

  public Flux<CRMOrganisation> retrieveOrganisations() {
    return Flux.fromIterable(organisations.values());
  }
}
