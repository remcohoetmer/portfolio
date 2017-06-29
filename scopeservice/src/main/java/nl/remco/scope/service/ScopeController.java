package nl.remco.scope.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/scope")
public final class ScopeController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScopeController.class);

  private final ScopeService service;

  @Autowired
  ScopeController(ScopeService service) {
    this.service = service;
  }

  @RequestMapping(method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @CrossOrigin(origins = "*")
  Mono<ScopeDTO> create(@RequestBody @Valid ScopeDTO scopeEntry) {
    LOGGER.info("Creating a new scope entry with information: {}", scopeEntry);

    Mono<ScopeDTO> created = service.create(scopeEntry);
    return created;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @CrossOrigin(origins = "*")
  Mono<ScopeDTO> delete(@PathVariable("id") String id) {
    LOGGER.info("Deleting scope entry with id: {}", id);

    Mono<ScopeDTO> deleted = service.delete(id);

    return deleted;
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @CrossOrigin(origins = "*")
  Flux<ScopeDTO> findAll(
    @RequestParam(value = "status", required = false) String status
  ) {
    LOGGER.info("Finding all scope entries");

    Flux<ScopeDTO> scopeEntries = service.findAll(status);

    return scopeEntries;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @CrossOrigin(origins = "*")
  Mono<ScopeDTO> findById(@PathVariable("id") String id) {
    LOGGER.info("Finding scope entry with id: {}", id);

    Mono<ScopeDTO> scopeEntry = service.findById(id);

    return scopeEntry;
  }

  @RequestMapping(value = "/initialise")
  String initialise() {
    LOGGER.info("initialise");

    service.initialise();

    return "";
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @CrossOrigin(origins = "*")
  Mono<ScopeDTO> update(@RequestBody @Valid ScopeDTO scopeEntry, @PathVariable("id") String id) {
    LOGGER.info("Updating scope entry with information: {}", scopeEntry);
    scopeEntry.setId(id);

    Mono<ScopeDTO> updated = service.update(scopeEntry);
    return updated;
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @CrossOrigin(origins = "*")
  public void handlescopeNotFound(ScopeNotFoundException ex) {
    LOGGER.error("Handling error with message: {}", ex.getMessage());
//TODO
  }
}
