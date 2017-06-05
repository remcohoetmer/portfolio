package nl.remco.group.enrich;

import nl.remco.group.service.domain.Scope;
import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.ScopeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class ScopeEnricher {
  private static final Logger log = LoggerFactory.getLogger(ScopeEnricher.class);

  public Mono<GroupDTO> enrichScopes(final GroupDTO groupDTO) {
    AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
    ScopeDTO scopeDTO = groupDTO.getScope();
    CompletableFuture<GroupDTO> cf = new CompletableFuture<>();

    if (scopeDTO != null && scopeDTO.getId() != null && !scopeDTO.getId().isEmpty()) {
      String url = "http://localhost:8081/api/scope/" + scopeDTO.getId();
      try {
        asyncRestTemplate.getForEntity(url, Scope.class).addCallback(
          re -> {
            Scope scope = re.getBody();
            scopeDTO.setName(scope.getName());
            cf.complete(groupDTO);
          },
          error -> {
            System.err.println("result" + error);
            scopeDTO.setName("--Unknown--");
            cf.complete(groupDTO);
          });

      } catch (RestClientException e) {
        log.error("HTTP failed" + url, e);
        cf.complete(groupDTO);
      }
    } else {
      cf.complete(groupDTO);
    }
    return Mono.fromCompletionStage(cf);
  }


  public Flux<GroupDTO> enrichScopes(final Flux<GroupDTO> groups) {
    return groups.flatMap(this::enrichScopes);
  }

}
