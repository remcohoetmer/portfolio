package nl.remco.scope.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@PropertySource("application.properties")
@Service
final class MongoDBScopeService implements ScopeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBScopeService.class);

    private final ScopeRepository repository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MongoDBScopeService(ScopeRepository repository) {
        this.repository = repository;
    }

    @Override
    public ScopeDTO create(ScopeDTO scope) {
        LOGGER.info("Creating a new scope entry with information: {}", scope);

        Scope persisted = Scope.getBuilder()
                .status(scope.getStatus())
                .name(scope.getName())
                .build();

        persisted = repository.save(persisted);
        LOGGER.info("Created a new scope entry with information: {}", persisted);

        return convertToDTO(persisted);
    }

    @Override
    public ScopeDTO delete(String id) {
        LOGGER.info("Deleting a scope entry with id: {}", id);

        Scope deleted = findScopeById(id);
        repository.delete(deleted);

        LOGGER.info("Deleted scope entry with informtation: {}", deleted);

        return convertToDTO(deleted);
    }

    @Override
    public List<ScopeDTO> findAll() {
        LOGGER.info("Finding all scope entries.");

/*
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("Inactive"));
        List<Scope> scopeEntries= mongoTemplate.find(query, Scope.class);
*/
        List<Scope> scopeEntries = repository.findAll();

        LOGGER.info("Found {} scope entries", scopeEntries.size());
     
        
        
        return convertToDTOs(scopeEntries);
    }

    private List<ScopeDTO> convertToDTOs(List<Scope> models) {
        return models.stream()
                .map(this::convertToDTO)
                .collect(toList());
    }

    @Override
    public ScopeDTO findById(String id) {
        LOGGER.info("Finding scope entry with id: {}", id);

        Scope found = findScopeById(id);

        LOGGER.info("Found scope entry: {}", found);

        return convertToDTO(found);
    }

    @Override
    public ScopeDTO update(ScopeDTO scope) {
        LOGGER.info("Updating scope entry with information: {}", scope);

        Scope updated = findScopeById(scope.getId());
        updated.update(scope.getStatus(), scope.getName());
        updated = repository.save(updated);

        LOGGER.info("Updated scope entry with information: {}", updated);

        return convertToDTO(updated);
    }

    private Scope findScopeById(String id) {
        Optional<Scope> result = repository.findOne(id);
        return result.orElseThrow(() -> new ScopeNotFoundException(id));

    }

    private ScopeDTO convertToDTO(Scope model) {
        ScopeDTO dto = new ScopeDTO();

        dto.setId(model.getId());
        dto.setStatus(model.getStatus());
        dto.setName(model.getName());

        return dto;
    }
}
