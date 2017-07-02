package nl.remco.employee.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/employee")
public final class EmployeeController {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

  private final EmployeeService service;

  @Autowired
  EmployeeController(EmployeeService service) {
    this.service = service;
  }

  @RequestMapping(method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @CrossOrigin(origins = "*")
  Mono<EmployeeDTO> create(@RequestBody @Valid EmployeeDTO scopeEntry) {
    LOGGER.info("Creating a new employee entry with information: {}", scopeEntry);

    Mono<EmployeeDTO> created = service.create(scopeEntry);
    return created;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @CrossOrigin(origins = "*")
  Mono<EmployeeDTO> delete(@PathVariable("id") String id) {
    LOGGER.info("Deleting employee entry with id: {}", id);

    Mono<EmployeeDTO> deleted = service.delete(id);

    return deleted;
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @CrossOrigin(origins = "*")
  Flux<EmployeeDTO> findAll(
    @RequestParam(value = "status", required = false) String status
  ) {
    LOGGER.info("Finding all employee entries");

    Flux<EmployeeDTO> scopeEntries = service.findAll(status);

    return scopeEntries;
  }

  @RequestMapping(value = "/list")
  @CrossOrigin(origins = "*")
  List<EmployeeDTO> findList(
    @RequestParam(value = "status", required = false) String status
  ) {
    LOGGER.info("Finding all employee entries");

    List<EmployeeDTO> scopeEntries = service.findAll(status).collectList().block();

    return scopeEntries;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @CrossOrigin(origins = "*")
  Mono<EmployeeDTO> findById(@PathVariable("id") String id) {
    LOGGER.info("Finding employee entry with id: {}", id);

    Mono<EmployeeDTO> scopeEntry = service.findById(id);

    return scopeEntry;
  }

  @RequestMapping(value = "/initialise")
  Mono<Void> initialise() {
    LOGGER.info("initialise");
    return service.initialise();
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @CrossOrigin(origins = "*")
  Mono<EmployeeDTO> update(@RequestBody @Valid EmployeeDTO scopeEntry, @PathVariable("id") String id) {
    LOGGER.info("Updating employee entry with information: {}", scopeEntry);
    scopeEntry.setId(id);

    Mono<EmployeeDTO> updated = service.update(scopeEntry);
    return updated;
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @CrossOrigin(origins = "*")
  public void handlescopeNotFound(EmployeeNotFoundException ex) {
    LOGGER.error("Handling error with message: {}", ex.getMessage());
//TODO
  }
}
