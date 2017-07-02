package nl.remco.employee.service;

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
final class MongoDBEmployeeService implements EmployeeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBEmployeeService.class);

  private final EmployeeRepository repository;
  @Autowired
  ReactiveMongoTemplate mongoTemplate;

  @Autowired
  MongoDBEmployeeService(EmployeeRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<EmployeeDTO> create(EmployeeDTO scope) {
    LOGGER.info("Creating a new employee entry with information: {}", scope);


    return repository.save(scope).map(savedEmployee -> convertToDTO(savedEmployee));
  }

  @Override
  public Mono<EmployeeDTO> delete(String id) {
    LOGGER.info("Deleting a employee entry with id: {}", id);

    return findScopeById(id)
      .flatMap(employee -> repository.delete(employee)
        .then(Mono.just(convertToDTO(employee))));
  }

  @Override
  public Flux<EmployeeDTO> findAll(String status) {
    LOGGER.info("Finding all employee entries.");
    Query query = new Query();
    if (status != null && !status.isEmpty()) {
      query.addCriteria(Criteria.where("status").is(status));
    }
    Flux<Employee> scopeEntries = mongoTemplate.find(query, Employee.class);

    return scopeEntries.map(this::convertToDTO);
  }

  private List<EmployeeDTO> convertToDTOs(List<Employee> models) {
    return models.stream()
      .map(this::convertToDTO)
      .collect(toList());
  }

  @Override
  public Mono<EmployeeDTO> findById(String id) {
    LOGGER.info("Finding employee entry with id: {}", id);

    return findScopeById(id).map(this::convertToDTO);
  }

  @Override
  public Mono<EmployeeDTO> update(EmployeeDTO scope) {
    LOGGER.info("Updating employee entry with information: {}", scope);

    return findScopeById(scope.getId())
      .flatMap(foundEmployee -> {
        return repository.save(foundEmployee).map(this::convertToDTO);
      });
  }


  private Mono<Employee> findScopeById(String id) {
    Mono<Employee> result = repository.findById(id);

    return result.switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)));
  }

  private EmployeeDTO convertToDTO(Employee model) {
    EmployeeDTO dto = new EmployeeDTO();

    dto.setId(model.getId());
    dto.setFirstName(model.getFirstName());
    dto.setLastName(model.getLastName());
    dto.setDescription(model.getDescription());
    return dto;
  }

  @Override
  public Mono<Void> initialise() {
    Employee e1 = new Employee();
    Employee e2 = new Employee();
    Employee e3 = new Employee();
    Employee e4 = new Employee();
    e1.setFirstName("Remco");
    e2.setFirstName("Frank");
    e3.setFirstName("Anne");
    e4.setFirstName("Irene");
    e1.setLastName("Hoetmer");
    e2.setLastName("Hoetmer");
    e3.setLastName("Oude Egberink");
    e4.setLastName("Oude Egberink");
    e1.setDescription("des");
    e2.setDescription("des");
    e3.setDescription("des");
    e4.setDescription("des");
    final List<Employee> Employees = Arrays.asList(e1, e2, e3, e4);

    final Mono<Void> initializeCollections =
      mongoTemplate
        .dropCollection(Employee.class)
        .then(mongoTemplate.createCollection(Employee.class))
        .then();

    final Mono<Void> initializeData =
      mongoTemplate
        .insert(Employees, Employee.class)
        .log(Employee.class.getName(), Level.INFO)
        .then();

    return initializeCollections.then(initializeData);

  }

}
