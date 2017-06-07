package nl.remco.group.service.serviceclients;

import nl.remco.group.service.domain.Scope;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication

public class ScopeClient {
  @Bean
  WebClient client() {
    return WebClient.create("http://localhost:8082");
  }

  @Bean
  CommandLineRunner rsclient(WebClient client) {
    return args -> {
      client.get().uri("/api/scope/").exchange().flux()
        .flatMap(clientResponse -> clientResponse.bodyToFlux(Scope.class));
    };
  }

  public Mono<Scope> getScopeById(final String id) {
    Flux<Scope> y= client().get().uri("/api/scope/").exchange().flux()
      .flatMap((ClientResponse clientResponse) -> clientResponse.bodyToFlux(Scope.class));
    return y.next();
  }


}

