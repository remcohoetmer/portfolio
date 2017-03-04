package nl.remco.scope.service;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


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
    @CrossOrigin(origins = "http://localhost:63062")
    ScopeDTO create(@RequestBody @Valid ScopeDTO scopeEntry) {
        LOGGER.info("Creating a new scope entry with information: {}", scopeEntry);

        ScopeDTO created = service.create(scopeEntry);
        LOGGER.info("Created a new scope entry with information: {}", created);

        return created;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "http://localhost:63062")
    ScopeDTO delete(@PathVariable("id") String id) {
        LOGGER.info("Deleting scope entry with id: {}", id);

        ScopeDTO deleted = service.delete(id);
        LOGGER.info("Deleted scope entry with information: {}", deleted);

        return deleted;
    }

    @RequestMapping(method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    List<ScopeDTO> findAll() {
        LOGGER.info("Finding all scope entries");

        List<ScopeDTO> scopeEntries = service.findAll();
        LOGGER.info("Found {} scope entries", scopeEntries.size());

        return scopeEntries;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:63062")
    ScopeDTO findById(@PathVariable("id") String id) {
        LOGGER.info("Finding scope entry with id: {}", id);

        ScopeDTO scopeEntry = service.findById(id);
        LOGGER.info("Found scope entry with information: {}", scopeEntry);

        return scopeEntry;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @CrossOrigin(origins = "*")
    ScopeDTO update(@RequestBody @Valid ScopeDTO scopeEntry, @PathVariable("id") String id) {
        LOGGER.info("Updating scope entry with information: {}", scopeEntry);
        scopeEntry.setId(id);

        ScopeDTO updated = service.update(scopeEntry);
        LOGGER.info("Updated scope entry with information: {}", updated);

        return updated;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @CrossOrigin(origins = "http://localhost:63062")
    public void handlescopeNotFound(ScopeNotFoundException ex) {
        LOGGER.error("Handling error with message: {}", ex.getMessage());
    }
}
