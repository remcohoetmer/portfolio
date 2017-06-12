package nl.cerios.rsclient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RsclientApplicationTests {
  private static final Logger log = LoggerFactory.getLogger(GroupIT.class);

  @Bean
  WebClient client() {
    return WebClient.create("http://localhost:8082");
  }

  @Test
  public void testWrongService() {
//    /flatMap(this::print).
    wrongService().subscribe(clientResponse -> {
      log.info("Status " + clientResponse.statusCode());
    }, o -> {
      o.printStackTrace();
    });
  }

  private Mono<ClientResponse> wrongService() {

    Mono<ClientResponse> chain = client().get().uri("/nonexisting/").exchange();
    return chain;
  }

  private Mono<ClientResponse> print(final ClientResponse clientResponse) {
    return Mono.fromCompletionStage(CompletableFuture.supplyAsync(() -> {
      log.info("Status "+clientResponse.statusCode());
      return clientResponse;
    }));
  }
}
