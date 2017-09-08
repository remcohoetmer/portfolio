package nl.cerios.serviceclient;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScopeService {
  private static final Logger logger = LoggerFactory.getLogger(ScopeService.class);

  private final RestTemplate restTemplate;

  public ScopeService(RestTemplate rest) {
    this.restTemplate = rest;
  }

  @HystrixCommand(fallbackMethod = "reliableScopes", ignoreExceptions = {org.springframework.web.client.ResourceAccessException.class})
  public List<Scope> getScopes() {
    logger.info("getScopes(): invoke");
    URI uri = URI.create("http://localhost:8082/api/scope");
    List<Scope> scopes = this.restTemplate.getForObject(uri, List.class);

    logger.info("getScopes " + scopes.size());
    return scopes;
  }


  public List<Scope> reliableScopes(Throwable t) {
    logger.info("getScopes(): fallback");
    t.printStackTrace();
    return new ArrayList();
  }

  @HystrixCommand(fallbackMethod = "reliableScopeById")
  public Scope getScopeById(String id) {
    URI uri = URI.create("http://localhost:8082/api/scope/" + id);
    return this.restTemplate.getForObject(uri, Scope.class);

  }

  public Scope reliableScopeById(String id, Throwable t) {
    return new Scope(id, "");

  }
}
