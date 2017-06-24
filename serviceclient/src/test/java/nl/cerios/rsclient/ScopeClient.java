package nl.cerios.rsclient;

import nl.remco.group.service.domain.Scope;
import nl.remco.scope.service.ScopeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component

public class ScopeClient {
  @Autowired
  WebClient webClient;

  Flux<Scope> getScopes() {
    final String uri = "/api/scope/";
    return webClient.get().uri(uri).exchange().flux()
      .flatMap(response -> GroupClient.check(uri, response))
      .flatMap(clientResponse->clientResponse.bodyToFlux(Scope.class));
  }

  Mono<Scope> getScopeById(final String id) {
    final String uri = "/api/scope/";
    return webClient.get().uri(uri + id).exchange()
      .flatMap(response -> GroupClient.check(uri, response))
      .flatMap(clientResponse-> clientResponse.bodyToMono(Scope.class));
  }

  Mono<ScopeDTO> insertScope() {
    ScopeDTO group = new ScopeDTO();
    group.setName("Cyber");
    group.setStatus("Actief");
    final String uri = "/api/scope";
    Mono<ScopeDTO> savedScope = webClient.post().uri(uri).syncBody(group).exchange()
      .flatMap(response -> GroupClient.check(uri, response))
      .flatMap(clientResponse -> clientResponse.bodyToMono(ScopeDTO.class));
    return savedScope;
  }
}
