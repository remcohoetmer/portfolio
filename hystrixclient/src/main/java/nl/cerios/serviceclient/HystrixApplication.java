package nl.cerios.serviceclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@EnableCircuitBreaker
@RestController
@SpringBootApplication
public class HystrixApplication {

  @Autowired
  private ScopeService scopeService;

  @Bean
  public RestTemplate rest(RestTemplateBuilder builder) {
    return builder.build();
  }

  @RequestMapping("/scopes")
  public List<Scope> toRead() {
    return scopeService.getScopes();
  }

  public static void main(String[] args) {
    SpringApplication.run(HystrixApplication.class, args);
  }
}
