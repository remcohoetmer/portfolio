package nl.remco.group.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import nl.remco.group.service.dto.GroupDTO;


@RestController
@RequestMapping("/api/group")
public final class GroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;

    @Autowired
    GroupController(GroupService service) {
        this.groupService = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "*")
    CompletableFuture<GroupDTO> create(@RequestBody @Valid GroupDTO groupEntry) {
        LOGGER.info("Creating a new group entry with information: {}", groupEntry);
    
        return groupService.create(groupEntry);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*")
    CompletableFuture<GroupDTO> delete(@PathVariable("id") String id) {
        LOGGER.info("Deleting group entry with id: {}", id);

        return groupService.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    @Async
    CompletableFuture<List<GroupDTO>> findAll(
    		@RequestParam(value="name", required=false) String name,
    		@RequestParam(value="code", required=false) String code,
    		@RequestParam(value="scopeId", required=false) String scopeId,
    		@RequestParam(value="masterId", required=false) String masterId,
    		@RequestParam(value="organisationId", required=false) String organisationId,
    		@RequestParam(value="selectMasters", required=false) String selectMasters,
    		@RequestParam(value="selectPersons", required=false) String selectPersons,
    		@RequestParam(value="selectScopes", required=false) String selectScopes,
    		@RequestParam(value="selectOrganisations", required=false) String selectOrganisations
    		) {
    	GroupFilter groupFilter= new GroupFilter();
    	GroupSelection groupSelection= new GroupSelection();
    	if (name!=null)
    		groupFilter.setName(name);
    	if (code!=null)
    		groupFilter.setCode(code);
    	if (scopeId!=null)
    		groupFilter.setScopeId(scopeId);
    	if (organisationId!=null)
    		groupFilter.setOrganisationId(organisationId);
    	if (masterId!=null)
    		groupFilter.setMasterId(masterId);
    	if (selectMasters!=null)
    		groupSelection.setSelectMasters();
    	if (selectPersons!=null)
    		groupSelection.setSelectPersons();
    	if (selectScopes!=null)
    		groupSelection.setSelectScopes();
    	if (selectOrganisations!=null)
    		groupSelection.setSelectOrganisations();
    	
        LOGGER.info("Finding all group entries");
        return groupService.find(groupFilter,groupSelection);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    CompletableFuture<GroupDTO> findById(@PathVariable("id") String id) {
        LOGGER.info("Finding group entry with id: {}", id);

        return groupService.findById(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @CrossOrigin(origins = "*")
    CompletableFuture<GroupDTO> update(@RequestBody @Valid GroupDTO groupEntry, @PathVariable("id") String id) {
        LOGGER.info("Updating group entry with information: {}", groupEntry);
        groupEntry.setId(id);

        return groupService.update(groupEntry);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @CrossOrigin(origins = "*")
    public void handlegroupNotFound(GroupNotFoundException ex) {
        LOGGER.error("Handling error with message: {}", ex.getMessage());
    }
}
