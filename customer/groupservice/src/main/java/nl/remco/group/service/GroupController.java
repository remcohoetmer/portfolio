package nl.remco.group.service;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;


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
  Mono<GroupDTO> create(@RequestBody @Valid GroupDTO groupEntry) {
    LOGGER.info("Creating a new group entry with information: {}", groupEntry);

    return groupService.create(groupEntry);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @CrossOrigin(origins = "*")
  Mono<GroupDTO> delete(@PathVariable("id") String id) {
    LOGGER.info("Deleting group entry with id: {}", id);

    return groupService.delete(id);
  }

  @RequestMapping(value = "/delay", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  Flux<GroupDTO> findWithDelay() {
    LOGGER.info("Finding all group entries with delay");
    Flux<GroupDTO> groups = groupService.find(new GroupFilter(), new GroupSelection());
    Flux<Long> d = Flux.interval(Duration.ofSeconds(1L), Duration.ofSeconds(1L));
    return Flux.zip(d, groups).map(g -> g.getT2());
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @CrossOrigin(origins = "*")
  Flux<GroupDTO> findAll(
    @RequestParam(value = "name", required = false) String name,
    @RequestParam(value = "code", required = false) String code,
    @RequestParam(value = "scopeId", required = false) String scopeId,
    @RequestParam(value = "personId", required = false) String personId,
    @RequestParam(value = "masterId", required = false) String masterId,
    @RequestParam(value = "organisationId", required = false) String organisationId,
    @RequestParam(value = "selectMasters", required = false) String selectMasters,
    @RequestParam(value = "selectPersons", required = false) String selectPersons,
    @RequestParam(value = "selectScopes", required = false) String selectScopes,
    @RequestParam(value = "selectOrganisations", required = false) String selectOrganisations
  ) {
    GroupFilter groupFilter = new GroupFilter();
    GroupSelection groupSelection = new GroupSelection();

    groupFilter.setName(name);
    groupFilter.setCode(code);
    groupFilter.setScopeId(scopeId);
    groupFilter.setPersonId(personId);
    groupFilter.setOrganisationId(organisationId);
    groupFilter.setMasterId(masterId);
    groupSelection.setSelectMasters();
    groupSelection.setSelectPersons();
    groupSelection.setSelectScopes();
    groupSelection.setSelectOrganisations();

    LOGGER.info("Finding all group entries");
    return groupService.find(groupFilter, groupSelection);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @CrossOrigin(origins = "*")
  Mono<GroupDTO> findById(@PathVariable("id") String id) {
    LOGGER.info("Finding group entry with id: {}", id);

    return groupService.findById(id);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @CrossOrigin(origins = "*")
  Mono<GroupDTO> update(@RequestBody @Valid GroupDTO groupEntry, @PathVariable("id") String id) {
    LOGGER.info("Updating group entry with information: {}", groupEntry);
    groupEntry.setId(id);

    return groupService.update(groupEntry);
  }

  @RequestMapping(value = "{id}/membership", method = RequestMethod.POST)
  @CrossOrigin(origins = "*")
  Mono<Void> addMembership(@RequestBody @Valid MembershipDTO mbsEntry, @PathVariable("id") String id) {
    LOGGER.info("Updating group id: {} add member {}", id, mbsEntry);

    return groupService.addMembership(id, mbsEntry);
  }

  @RequestMapping(value = "{id}/membership/{memid}", method = RequestMethod.DELETE)
  @CrossOrigin(origins = "*")
  Mono<Void> deleteMembership(@PathVariable("id") String id, @PathVariable("memid") String memid) {
    LOGGER.info("Updating group id: {} delete member {}", id, memid);

    return groupService.deleteMembership(id, memid);
  }


  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @CrossOrigin(origins = "*")
  public void handlegroupNotFound(GroupNotFoundException ex) {
    LOGGER.error("Handling error with message: {}", ex.getMessage());
  }
}
