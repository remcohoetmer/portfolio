package nl.remco.group.organisation.service;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.OrganisationDTO;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


@Component
final class CRMOrganisationDelegate {
  private static final Logger log = Logger.getLogger(CRMOrganisationDelegate.class.getName());

//  @Autowired
  CRMOrganisations crmOrganisations= new CRMOrganisations();
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

  public static void main(String[] args) {
    Mono.just(10).subscribe(System.out::println);
    Subscriber<Object> subscriber = new Subscriber<Object>() {
      @Override
      public void onSubscribe(Subscription s) {
        s.request(100000L);
      }

      @Override
      public void onNext(Object o) {
        log.info("onNext" + o);
      }

      @Override
      public void onError(Throwable t) {
        log.info("onNext" + t);
      }

      @Override
      public void onComplete() {
        log.info("onComplete");

      }
    };
    OrganisationDTO orgDTO = new OrganisationDTO();
    GroupDTO groupDTO= new GroupDTO();
    CRMOrganisationDelegate delegate= new CRMOrganisationDelegate();
    delegate.getOrganisation("8001").subscribe(subscriber);
    delegate.getOrganisation("8003").subscribe(subscriber);
    delegate.getOrganisation("8003").map(crmOrganisation -> {
      return groupDTO;
    }).subscribe(subscriber);

  }
}
