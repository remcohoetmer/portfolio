package nl.remco.group.enrich;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.ScopeDTO;
import nl.remco.group.service.serviceclients.ScopeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ScopeEnricher {
  private static final Logger LOGGER = LoggerFactory.getLogger(ScopeEnricher.class);
  @Autowired
  private ScopeClient scopeClient;

  public Mono<GroupDTO> enrichScopes(final GroupDTO groupDTO) {
    Mono<GroupDTO> chain = Mono.just(groupDTO);

    ScopeDTO scopeDTO = groupDTO.getScope();

    if (scopeDTO != null && scopeDTO.getId() != null && !scopeDTO.getId().isEmpty()) {
      chain = chain
        .then(scopeClient.getScopeById(scopeDTO.getId()))
        .map(scope -> {
          scopeDTO.setName(scope.getName());
          return groupDTO;
        });
    }
    return chain;
  }
}
