package nl.remco.scope.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static java.util.stream.Collectors.toList;

@PropertySource("application.properties")
@Service
final class MongoDBScopeService implements ScopeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBScopeService.class);

  private final ScopeRepository repository;
  @Autowired
  ReactiveMongoTemplate mongoTemplate;

  @Autowired
  MongoDBScopeService(ScopeRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<ScopeDTO> create(ScopeDTO scope) {
    LOGGER.info("Creating a new scope entry with information: {}", scope);

    Scope persisted = Scope.getBuilder()
      .withStatus(scope.getStatus())
      .withName(scope.getName())
      .build();

    return repository.save(persisted).map(savedScope -> convertToDTO(savedScope));
  }

  @Override
  public Mono<ScopeDTO> delete(String id) {
    LOGGER.info("Deleting a scope entry with id: {}", id);

    return findScopeById(id)
      .flatMap(scope -> repository.delete(scope)
        .then(Mono.just(convertToDTO(scope))));
  }

  @Override
  public Flux<ScopeDTO> findAll(String status) {
    LOGGER.info("Finding all scope entries.");
    Query query = new Query();
    if (status != null && !status.isEmpty()) {
      query.addCriteria(Criteria.where("status").is(status));
    }
    Flux<Scope> scopeEntries = mongoTemplate.find(query, Scope.class);

    return scopeEntries.map(this::convertToDTO);
  }

  private List<ScopeDTO> convertToDTOs(List<Scope> models) {
    return models.stream()
      .map(this::convertToDTO)
      .collect(toList());
  }

  @Override
  public Mono<ScopeDTO> findById(String id) {
    LOGGER.info("Finding scope entry with id: {}", id);

    return findScopeById(id).map(this::convertToDTO);
  }

  @Override
  public Mono<ScopeDTO> update(ScopeDTO scope) {
    LOGGER.info("Updating scope entry with information: {}", scope);

    return findScopeById(scope.getId())
      .flatMap(foundScope -> {
        foundScope.update(scope.getStatus(), scope.getName());
        return repository.save(foundScope).map(this::convertToDTO);
      });
  }


  private Mono<Scope> findScopeById(String id) {
    Mono<Scope> result = repository.findById(id);

    return result.switchIfEmpty(Mono.error(new ScopeNotFoundException(id)));
  }

  private ScopeDTO convertToDTO(Scope model) {
    ScopeDTO dto = new ScopeDTO();

    dto.setId(model.getId());
    dto.setStatus(model.getStatus());
    dto.setName(model.getName());
    return dto;
  }

  @Override
  public void initialise() {
    final List<Scope> SCOPES =
      Arrays.asList(
        Scope.getBuilder().withName("Wijk").withStatus("Active").build(),
        Scope.getBuilder().withName("School").withStatus("Active").build(),
        Scope.getBuilder().withName("Sport").withStatus("Inactive").build(),
        Scope.getBuilder().withName("Politiek").withStatus("Active").build());

    final Mono<Void> initializeCollections =
      mongoTemplate
        .dropCollection(Scope.class)
        .then(mongoTemplate.createCollection(Scope.class))
        .then();

    final Mono<Void> initializeData =
      mongoTemplate
        .insert(SCOPES, Scope.class)
        .log(Scope.class.getName(), Level.INFO)
        .then();

    initializeCollections.then(initializeData).block();

  }

}
