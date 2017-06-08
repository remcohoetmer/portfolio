package nl.cerios.rsclient;

import nl.remco.group.service.domain.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ScopeClient {
  @Bean
  WebClient client() {
    return WebClient.create("http://localhost:8082");
  }

  public Flux<Scope> getScopes() {
    return client().get().uri("/api/scope/").exchange().flux()
      .flatMap((ClientResponse clientResponse) -> clientResponse.bodyToFlux(Scope.class));
  }

  public Mono<Scope> getScopeById(final String id) {
    return client().get().uri("/api/scope/"+id).exchange()
      .flatMap((ClientResponse clientResponse) -> clientResponse.bodyToMono(Scope.class));
  }
}

