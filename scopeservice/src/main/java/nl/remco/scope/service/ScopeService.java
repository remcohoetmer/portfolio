package nl.remco.scope.service;

import nl.remco.scope.service.dto.ScopeDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


interface ScopeService {
  Mono<ScopeDTO> create(ScopeDTO scope);

  Mono<ScopeDTO> delete(String id);

  Flux<ScopeDTO> findAll(String status);

  Mono<ScopeDTO> findById(String id);

  Mono<ScopeDTO> update(ScopeDTO scope);

  void initialise();
}
