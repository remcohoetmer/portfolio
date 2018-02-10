package nl.cerios.rsclient;

import nl.remco.scope.service.dto.ScopeDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ScopeClient {

  @Bean
  WebClient client() {
    return WebClient.create("http://localhost:8081");
  }

  Flux<ScopeDTO> getScopes() {
    final String uri = "/api/scope/";
    return client().get().uri(uri).exchange().flux()
      .flatMap(response -> GroupClient.check(uri, response))
      .flatMap(clientResponse->clientResponse.bodyToFlux(ScopeDTO.class));
  }

  Mono<ScopeDTO> getScopeById(final String id) {
    final String uri = "/api/scope/";
    return client().get().uri(uri + id).exchange()
      .flatMap(response -> GroupClient.check(uri, response))
      .flatMap(clientResponse-> clientResponse.bodyToMono(ScopeDTO.class));
  }

  Mono<ScopeDTO> insertScope() {
    ScopeDTO group = new ScopeDTO();
    group.setName("Cyber");
    group.setStatus("Actief");
    final String uri = "/api/scope";
    Mono<ScopeDTO> savedScope = client().post().uri(uri).syncBody(group).exchange()
      .flatMap(response -> GroupClient.check(uri, response))
      .flatMap(clientResponse -> clientResponse.bodyToMono(ScopeDTO.class));
    return savedScope;
  }
}

