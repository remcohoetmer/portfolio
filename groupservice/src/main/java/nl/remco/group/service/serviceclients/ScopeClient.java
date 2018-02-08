package nl.remco.group.service.serviceclients;

import nl.remco.group.service.domain.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ScopeClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(ScopeClient.class);

  @Value("${scopeservice.url}")
  private String scopeServiceURL;

  @Bean
  WebClient client() {
    LOGGER.info("scopeServiceURL: " + scopeServiceURL);
    return WebClient.create(scopeServiceURL);
  }

  public Flux<Scope> getScopes() {
    return client().get().uri("/api/scope/").exchange().flux()
      .flatMap((ClientResponse clientResponse) -> clientResponse.bodyToFlux(Scope.class));
  }

  public Mono<Scope> getScopeById(final String id) {
    LOGGER.info("Get scope: " + id);
    return client().get().uri("/api/scope/" + id).exchange()
      .flatMap((ClientResponse clientResponse) -> clientResponse.bodyToMono(Scope.class));
  }
}

