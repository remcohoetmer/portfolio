package nl.cerios.serviceclient;

import nl.remco.scope.service.dto.ScopeDTO;
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
  public List<ScopeDTO> getScopes() {
    return scopeService.getScopes();
  }

  @RequestMapping("/scope")
  public ScopeDTO getScopeById() {
    List<ScopeDTO> scopes= scopeService.getScopes();
    if (!scopes.isEmpty()) {
      return scopeService.getScopeById(scopes.get(0).getId());
    }
    return new ScopeDTO();
  }

  public static void main(String[] args) {
    SpringApplication.run(HystrixApplication.class, args);
  }
}
